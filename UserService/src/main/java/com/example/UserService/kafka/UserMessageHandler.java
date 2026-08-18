package com.example.UserService.kafka;

import com.example.UserService.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class UserMessageHandler {

    private final ObjectMapper mapper;
    private final UserService userService;

    public void handle(EventGet eventGet){
        switch (eventGet.eventType()){
            case "CREATE_USER" ->
                userService.handleCreateUser(mapper.treeToValue(eventGet.payload(), CreateUserPayload.class));
        }
    }

}
