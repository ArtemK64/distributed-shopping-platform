package com.notificationservice.repository;

import com.notificationservice.entity.DeliveryChannel;
import com.notificationservice.entity.NotificationEvent;
import com.notificationservice.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByEventAndChannelAndLanguageCodeAndVersion(
            NotificationEvent event,
            DeliveryChannel channel,
            String languageCode,
            Integer version
    );
}
