package com.notificationservice.service;

import com.notificationservice.entity.UserNotification;
import com.notificationservice.repository.UserNotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    UserNotificationRepository notificationRepo;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void testSendNotification() {
        UserNotification notification = new UserNotification();
        when(notificationRepo.save(notification)).thenReturn(notification);

        UserNotification saved = notificationService.sendNotification(notification);

        assertEquals(notification, saved);
        verify(notificationRepo).save(notification);
    }

    @Test
    void testGetAllNotifications() {
        List<UserNotification> list = List.of(new UserNotification());
        when(notificationRepo.findAll()).thenReturn(list);

        List<UserNotification> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        verify(notificationRepo).findAll();
    }

    @Test
    void testGetNotificationsByRecipientAsDto() {
        UserNotification notif = new UserNotification();
        notif.setRecipientIdentifier("user@example.com");
        notif.setSubject("Тема");
        notif.setPreviewText("Превью");
        notif.setBody("Текст сообщения");
        notif.setSentAt(LocalDateTime.now());
        notif.setStatus(UserNotification.NotificationStatus.SENT);

        when(notificationRepo.findAllByRecipientIdentifierIgnoreCase("user@example.com"))
                .thenReturn(List.of(notif));

        var result = notificationService
                .getNotificationsByRecipientAsDto("user@example.com");

        assertEquals(1, result.size());
        assertEquals("Тема", result.getFirst().getSubject());
        verify(notificationRepo).findAllByRecipientIdentifierIgnoreCase("user@example.com");
    }
}
