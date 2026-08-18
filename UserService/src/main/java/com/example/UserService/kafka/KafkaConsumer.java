package com.example.UserService.kafka;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UserMessageHandler userMessageHandler;

    @KafkaListener(topics = "${kafka.users-topic}", groupId = "${kafka.users-group-id}")
    public void userListener(EventGet eventGet){
        userMessageHandler.handle(eventGet);
        log.info("Received event: {}", eventGet);
    }

}
