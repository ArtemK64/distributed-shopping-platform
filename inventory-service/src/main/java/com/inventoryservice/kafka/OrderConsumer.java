package com.inventoryservice.kafka;

import com.inventoryservice.dto.event.OrderCreatedEvent;
import com.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile("!test")
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final InventoryService inventoryService;
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.topic.order-created}",
            groupId = "inventory",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderCreatedEvent event) {
        log.info("Получено OrderCreatedEvent: {}", event);
        inventoryService.processOrder(event);
    }
}
