package com.example.AuthService.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "kafka")
@Component
public class KafkaProperties {
    private String usersTopic;
    private String usersGroupId;
}
