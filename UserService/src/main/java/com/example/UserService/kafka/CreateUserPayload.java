package com.example.UserService.kafka;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserPayload{
    private String userId;
    private String username;
    private String email;
}


