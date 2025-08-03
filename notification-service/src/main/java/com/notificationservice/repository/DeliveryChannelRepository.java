package com.notificationservice.repository;

import com.notificationservice.entity.DeliveryChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryChannelRepository extends JpaRepository<DeliveryChannel, Long> {
    Optional<DeliveryChannel> findByChannelType(DeliveryChannel.ChannelType type);
}
