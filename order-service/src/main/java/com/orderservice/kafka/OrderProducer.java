package com.orderservice.kafka;

import com.orderservice.dto.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created", event);
    }
}
