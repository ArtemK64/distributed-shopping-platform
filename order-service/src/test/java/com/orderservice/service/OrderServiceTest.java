package com.orderservice.service;

import com.orderservice.dto.event.OrderResultEvent;
import com.orderservice.entity.Order;
import com.orderservice.entity.OrderItem;
import com.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void testHandleOrderResultFailed() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResultEvent event = new OrderResultEvent(
                1L,
                false,
                "OUT_OF_STOCK",
                BigDecimal.ZERO,
                Map.of(),
                "john@example.com"
        );

        orderService.handleOrderResult(event);

        verify(orderRepository).saveAndFlush(order);
        assertEquals("CANCELLED", order.getStatus());
    }

    @Test
    void testHandleOrderResultSuccess() {
        Order order = new Order();
        order.setId(2L);

        OrderItem firstItem = new OrderItem();
        firstItem.setProductName("Ноутбук");

        OrderItem secondItem = new OrderItem();
        secondItem.setProductName("Мышь");

        order.setItems(List.of(firstItem, secondItem));

        when(orderRepository.findById(2L))
                .thenReturn(Optional.of(order));

        Map<String, BigDecimal> prices = Map.of(
                "Ноутбук", BigDecimal.valueOf(1200),
                "Мышь", BigDecimal.valueOf(25)
        );

        OrderResultEvent event = new OrderResultEvent(
                2L,
                true,
                "OK",
                BigDecimal.valueOf(1225),
                prices,
                "user@example.com"
        );

        orderService.handleOrderResult(event);

        verify(orderRepository).saveAndFlush(order);
        assertEquals("CONFIRMED", order.getStatus());
        assertEquals(BigDecimal.valueOf(1200), firstItem.getUnitPrice());
        assertEquals(BigDecimal.valueOf(25), secondItem.getUnitPrice());
    }
}
