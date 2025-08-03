package com.notificationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(
        name = "notification_templates",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"event_id", "channel_id", "language_code", "version"}
        )
)
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 5)
    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false)
    private String subject;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @NotNull
    @Min(1)
    private Integer version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private NotificationEvent event;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private DeliveryChannel channel;
}
