package com.notificationservice.controller;

import com.notificationservice.dto.response.NotificationDto;
import com.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/email/{email}")
    public List<NotificationDto> getByEmail(@PathVariable String email) {
        return notificationService.getNotificationsByRecipientAsDto(email);
    }
}
