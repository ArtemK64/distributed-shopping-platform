package com.inventoryservice.kafka;

import com.inventoryservice.dto.event.OrderResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, OrderResultEvent> kafkaTemplate;

    public void sendOrderResult(OrderResultEvent result) {
        String topic = result.isConfirmed() ? "order-confirmed" : "order-failed";
        kafkaTemplate.send(topic, result);
    }
}
