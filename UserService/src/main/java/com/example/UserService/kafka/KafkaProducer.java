package com.example.UserService.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, EventGet> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

//    public void publish(EventGet eventGet){
//        String key = eventGet.getKey();
//        kafkaTemplate.send(kafkaProperties.getTopic(), key, eventGet);
//        log.info("Sent message to topic: {}", key);
//    }
}
