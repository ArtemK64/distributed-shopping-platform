package com.notificationservice.repository;

import com.notificationservice.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findAllByRecipientIdentifierIgnoreCase(String recipientIdentifier);
}
