package com.orderservice.kafka;

import com.orderservice.dto.event.OrderResultEvent;
import com.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(
            topics = "order-confirmed",
            groupId = "order-group"
    )
    public void consumeOrderConfirmed(OrderResultEvent event) {
        event.setConfirmed(true);
        orderService.handleOrderResult(event);
    }

    @KafkaListener(
            topics = "order-failed",
            groupId = "order-group"
    )
    public void consumeOrderFailed(OrderResultEvent event) {
        event.setConfirmed(false);
        orderService.handleOrderResult(event);
    }
}
