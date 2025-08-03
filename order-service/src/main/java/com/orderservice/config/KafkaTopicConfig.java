package com.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderCreatedTopic(
            @Value("${spring.kafka.topic.order-created}") String name
    ) {
        return TopicBuilder.name(name).build();
    }

    @Bean
    public NewTopic orderConfirmedTopic(
            @Value("${spring.kafka.topic.order-confirmed}") String name
    ) {
        return TopicBuilder.name(name).build();
    }

    @Bean
    public NewTopic orderFailedTopic(
            @Value("${spring.kafka.topic.order-failed}") String name
    ) {
        return TopicBuilder.name(name).build();
    }
}
