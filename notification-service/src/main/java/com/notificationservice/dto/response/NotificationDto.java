package com.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDto {

    private String recipientAddress;
    private String subject;
    private String previewText;
    private String body;
    private LocalDateTime sentAt;
    private String status;
}
