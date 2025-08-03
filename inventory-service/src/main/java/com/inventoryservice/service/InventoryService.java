package com.inventoryservice.service;

import com.inventoryservice.dto.event.OrderCreatedEvent;
import com.inventoryservice.dto.event.OrderResultEvent;
import com.inventoryservice.dto.response.ProductInfoDto;
import com.inventoryservice.entity.Product;
import com.inventoryservice.entity.Stock;
import com.inventoryservice.kafka.InventoryProducer;
import com.inventoryservice.repository.ProductRepository;
import com.inventoryservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final InventoryProducer producer;

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public Optional<Stock> getStockForProduct(Long productId) {
        return stockRepository.findAll().stream()
                .filter(stock -> stock.getProduct().getId().equals(productId))
                .findFirst();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void processOrder(OrderCreatedEvent event) {
        Map<String, BigDecimal> itemPrices = new HashMap<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (var item : event.getItems()) {
            Product product = productRepository.findByName(item.getProductName())
                    .orElseThrow(() -> new RuntimeException(
                            "Товар не найден: " + item.getProductName())
                    );

            Stock stock = stockRepository.findByProduct(product)
                    .orElseThrow(() -> new RuntimeException(
                            "Остаток не найден для товара: " + item.getProductName())
                    );

            if (stock.getQuantity() < item.getQuantity()) {
                producer.sendOrderResult(new OrderResultEvent(
                        event.getOrderId(),
                        false,
                        "Недостаточное количество на складе товара: " + item.getProductName(),
                        BigDecimal.ZERO,
                        Collections.emptyMap(),
                        event.getCustomerEmail()
                ));
                return;
            }

            BigDecimal price = product.getPrice();
            itemPrices.put(item.getProductName(), price);
            totalPrice = totalPrice.add(
                    price.multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        for (var item : event.getItems()) {
            Product product = productRepository.findByName(item.getProductName())
                    .orElseThrow(() -> new RuntimeException(
                            "Товар не найден: " + item.getProductName()));

            Stock stock = stockRepository.findByProduct(product)
                    .orElseThrow(() -> new RuntimeException(
                            "Остаток не найден для товара: " + product.getName()));

            stock.setQuantity(stock.getQuantity() - item.getQuantity());
            stockRepository.save(stock);
        }

        producer.sendOrderResult(new OrderResultEvent(
                event.getOrderId(),
                true,
                "Резервирование успешно",
                totalPrice,
                itemPrices,
                event.getCustomerEmail()
        ));
    }

    public List<ProductInfoDto> getProductInfoList() {
        List<ProductInfoDto> result = new ArrayList<>();

        for (Product product : productRepository.findAll()) {
            Integer quantity = stockRepository.findByProduct(product)
                    .map(Stock::getQuantity)
                    .orElse(0);

            result.add(new ProductInfoDto(
                    product.getId(),
                    product.getName(),
                    product.getCategory().getName(),
                    product.getManufacturer().getName(),
                    quantity,
                    product.getPrice()
            ));
        }

        return result;
    }
}
