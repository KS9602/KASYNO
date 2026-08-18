package com.example.AuthService.kafka;

import com.example.AuthService.enums.Topic;
import com.example.AuthService.kafka.events.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void publish(Topic topic, Event event) {
        String key = event.eventId().toString();
        kafkaTemplate.send(setTopic(topic), key, event);
    }

    private String setTopic(Topic topic) {
        switch (topic) {
            case USERS_TOPIC:
                return "users";
        }
        return "";
    }

}
