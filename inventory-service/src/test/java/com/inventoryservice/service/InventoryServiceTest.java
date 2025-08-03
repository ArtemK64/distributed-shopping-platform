package com.inventoryservice.service;

import com.inventoryservice.dto.event.OrderCreatedEvent;
import com.inventoryservice.dto.response.ProductInfoDto;
import com.inventoryservice.entity.Category;
import com.inventoryservice.entity.Manufacturer;
import com.inventoryservice.entity.Product;
import com.inventoryservice.entity.Stock;
import com.inventoryservice.kafka.InventoryProducer;
import com.inventoryservice.repository.ProductRepository;
import com.inventoryservice.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    StockRepository stockRepository;

    @Mock
    InventoryProducer inventoryProducer;

    @InjectMocks
    InventoryService inventoryService;

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = inventoryService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product saved = inventoryService.saveProduct(product);

        assertEquals(product, saved);
        verify(productRepository).save(product);
    }

    @Test
    void testGetStockForProduct() {
        Product product = new Product();
        product.setId(1L);

        Stock stock = new Stock();
        stock.setProduct(product);

        when(stockRepository.findAll()).thenReturn(List.of(stock));

        Optional<Stock> result = inventoryService.getStockForProduct(1L);

        assertTrue(result.isPresent());
        assertEquals(stock, result.get());
    }

    @Test
    void testProcessOrderInsufficientStock() {
        Product product = new Product();
        product.setName("Ноутбук");
        product.setPrice(BigDecimal.valueOf(75000));

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(1);

        OrderCreatedEvent event = new OrderCreatedEvent(
                1L,
                "user@example.com",
                List.of(new OrderCreatedEvent.OrderItemDto("Ноутбук", 10))
        );

        when(productRepository.findByName("Ноутбук")).thenReturn(Optional.of(product));
        when(stockRepository.findByProduct(product)).thenReturn(Optional.of(stock));

        inventoryService.processOrder(event);

        verify(inventoryProducer).sendOrderResult(argThat(result ->
                !result.isConfirmed()
                        && result.getMessage().contains("Недостаточное количество")
                        && result.getOrderId().equals(1L)
        ));
    }

    @Test
    void testGetProductInfoList() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Ноутбук");

        Category category = new Category();
        category.setName("Электроника");
        product.setCategory(category);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("Apple");
        product.setManufacturer(manufacturer);

        when(productRepository.findAll()).thenReturn(List.of(product));

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setProduct(product);
        stock.setQuantity(5);

        when(stockRepository.findByProduct(product)).thenReturn(Optional.of(stock));

        List<ProductInfoDto> result = inventoryService.getProductInfoList();

        assertEquals(1, result.size());

        ProductInfoDto dto = result.getFirst();
        assertEquals("Ноутбук", dto.getName());
        assertEquals("Электроника", dto.getCategory());
        assertEquals("Apple", dto.getManufacturer());
        assertEquals(5, dto.getQuantity());
    }
}
