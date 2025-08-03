package com.notificationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_notifications")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "recipient_identifier", nullable = false)
    private String recipientIdentifier;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false)
    private String subject;

    @NotBlank
    @Size(max = 300)
    @Column(name = "preview_text", nullable = false)
    private String previewText;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @PastOrPresent
    @NotNull
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "template_id")
    private NotificationTemplate template;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private DeliveryChannel channel;

    public enum NotificationStatus {
        PENDING, SENT, FAILED
    }
}
