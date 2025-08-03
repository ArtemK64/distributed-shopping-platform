package com.notificationservice.service;

import com.notificationservice.dto.response.NotificationDto;
import com.notificationservice.entity.UserNotification;
import com.notificationservice.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserNotificationRepository notificationRepo;

    @Transactional
    public UserNotification sendNotification(UserNotification notification) {
        return notificationRepo.save(notification);
    }

    public List<UserNotification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    public List<NotificationDto> getNotificationsByRecipientAsDto(String email) {
        return notificationRepo
                .findAllByRecipientIdentifierIgnoreCase(email)
                .stream()
                .map(n -> new NotificationDto(
                        n.getRecipientIdentifier(),
                        n.getSubject(),
                        n.getPreviewText(),
                        n.getBody(),
                        n.getSentAt(),
                        n.getStatus().name()
                ))
                .toList();
    }
}
