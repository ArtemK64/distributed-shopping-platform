package com.orderservice.service;

import com.orderservice.dto.event.OrderCreatedEvent;
import com.orderservice.dto.event.OrderResultEvent;
import com.orderservice.dto.request.CreateOrderRequest;
import com.orderservice.dto.request.CustomerDto;
import com.orderservice.dto.request.ShippingAddressDto;
import com.orderservice.entity.Customer;
import com.orderservice.entity.Order;
import com.orderservice.entity.OrderItem;
import com.orderservice.entity.ShippingAddress;
import com.orderservice.kafka.OrderProducer;
import com.orderservice.repository.CustomerRepository;
import com.orderservice.repository.OrderRepository;
import com.orderservice.repository.ShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final OrderProducer orderProducer;

    @Transactional
    public void createOrder(CreateOrderRequest request) {
        Customer customer = findOrCreateCustomer(request.getCustomer());
        ShippingAddress address = findOrCreateAddress(request.getShippingAddress());

        List<OrderItem> items = request.getItems()
                .stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setProductName(dto.getProductName());
                    item.setQuantity(dto.getQuantity());
                    item.setUnitPrice(BigDecimal.ZERO);
                    return item;
                })
                .collect(Collectors.toList());

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setItems(items);
        order.setStatus("PENDING");
        order.setTotalPrice(BigDecimal.ZERO);

        items.forEach(item -> item.setOrder(order));

        orderRepository.saveAndFlush(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                customer.getEmail(),
                request.getItems()
        );

        orderProducer.sendOrderCreatedEvent(event);
    }

    private Customer findOrCreateCustomer(CustomerDto dto) {
        return customerRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setFirstName(dto.getFirstName());
                    c.setLastName(dto.getLastName());
                    c.setMiddleName(dto.getMiddleName());
                    c.setAge(dto.getAge());
                    c.setEmail(dto.getEmail());
                    c.setPhone(dto.getPhone());
                    return customerRepository.save(c);
                });
    }

    private ShippingAddress findOrCreateAddress(ShippingAddressDto dto) {
        return shippingAddressRepository
                .findByCountryAndCityAndStreetAndHouseAndPostalCode(
                        dto.getCountry(),
                        dto.getCity(),
                        dto.getStreet(),
                        dto.getHouse(),
                        dto.getPostalCode()
                )
                .orElseGet(() -> {
                    ShippingAddress sa = new ShippingAddress();
                    sa.setCountry(dto.getCountry());
                    sa.setCity(dto.getCity());
                    sa.setStreet(dto.getStreet());
                    sa.setHouse(dto.getHouse());
                    sa.setPostalCode(dto.getPostalCode());
                    return shippingAddressRepository.save(sa);
                });
    }

    @Transactional
    public void handleOrderResult(OrderResultEvent result) {
        Order order = orderRepository.findById(result.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (result.isConfirmed()) {
            order.setStatus("CONFIRMED");
            order.setTotalPrice(result.getTotalPrice());

            Map<String, BigDecimal> priceMap = result.getItemPrices();
            order.getItems().forEach(item -> {
                BigDecimal price = priceMap.getOrDefault(
                        item.getProductName(),
                        BigDecimal.ZERO
                );
                item.setUnitPrice(price);
            });
        } else {
            order.setStatus("CANCELLED");
        }

        orderRepository.saveAndFlush(order);
    }
}
