package com.notificationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "user_subscriptions")
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private NotificationEvent event;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private DeliveryChannel channel;
}
