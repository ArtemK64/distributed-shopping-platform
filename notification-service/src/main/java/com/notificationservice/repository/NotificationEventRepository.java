package com.notificationservice.repository;

import com.notificationservice.entity.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {
    Optional<NotificationEvent> findByEventCode(String eventCode);
}
