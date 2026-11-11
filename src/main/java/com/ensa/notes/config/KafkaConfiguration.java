package com.ensa.notes.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic notesTopic() {
        return TopicBuilder.name("note-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
