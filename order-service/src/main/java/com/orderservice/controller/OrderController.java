package com.orderservice.controller;

import com.orderservice.dto.request.CreateOrderRequest;
import com.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        orderService.createOrder(request);
        return ResponseEntity.ok("Заказ успешно оформлен");
    }
}
