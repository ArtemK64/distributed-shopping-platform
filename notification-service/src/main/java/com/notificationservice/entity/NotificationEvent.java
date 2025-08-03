package com.notificationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(
        name = "notification_events",
        uniqueConstraints = @UniqueConstraint(columnNames = "event_code")
)
public class NotificationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "event_code", nullable = false)
    private String eventCode;

    @NotBlank
    @Size(max = 100)
    private String name;
}
