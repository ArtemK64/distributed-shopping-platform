package com.notificationservice.kafka;

import com.notificationservice.dto.event.OrderResultEvent;
import com.notificationservice.entity.DeliveryChannel;
import com.notificationservice.entity.NotificationEvent;
import com.notificationservice.entity.NotificationTemplate;
import com.notificationservice.entity.UserNotification;
import com.notificationservice.repository.DeliveryChannelRepository;
import com.notificationservice.repository.NotificationEventRepository;
import com.notificationservice.repository.NotificationTemplateRepository;
import com.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Profile("!test")
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final NotificationService notificationService;
    private final NotificationTemplateRepository templateRepo;
    private final DeliveryChannelRepository channelRepo;
    private final NotificationEventRepository eventRepo;

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.topic.confirmed}",
            groupId = "notification",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeConfirmed(OrderResultEvent event) {
        processEvent(event, true);
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.failed}",
            groupId = "notification",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeFailed(OrderResultEvent event) {
        processEvent(event, false);
    }

    private void processEvent(OrderResultEvent event, boolean confirmed) {
        log.info("Получено событие заказа: {}", event);

        String recipientEmail = event.getCustomerEmail();
        if (recipientEmail == null || recipientEmail.isBlank()) {
            log.warn("Email отсутствует, уведомление не будет создано");
            return;
        }

        DeliveryChannel channel = channelRepo
                .findByChannelType(DeliveryChannel.ChannelType.EMAIL)
                .orElseThrow(() -> new RuntimeException("Не найден EMAIL канал"));

        String eventCode = confirmed ? "ORDER_CONFIRMED" : "ORDER_FAILED";

        NotificationEvent notificationEvent = eventRepo
                .findByEventCode(eventCode)
                .orElseThrow(() -> new RuntimeException("Не найден код события: " + eventCode));

        NotificationTemplate template = templateRepo
                .findByEventAndChannelAndLanguageCodeAndVersion(
                        notificationEvent, channel, "ru", 1
                )
                .orElseThrow(() -> new RuntimeException("Не найден шаблон письма"));

        String subject = template.getSubject();
        String body = template.getBody()
                .replace("#{orderId}", String.valueOf(event.getOrderId()))
                .replace("#{message}", event.getMessage());

        UserNotification notification = new UserNotification();
        notification.setRecipientIdentifier(recipientEmail);
        notification.setSubject(subject);
        notification.setPreviewText(event.getMessage());
        notification.setBody(body);
        notification.setSentAt(LocalDateTime.now());
        notification.setStatus(UserNotification.NotificationStatus.SENT);
        notification.setTemplate(template);
        notification.setChannel(channel);

        notificationService.sendNotification(notification);
    }
}
