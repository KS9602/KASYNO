package com.example.AuthService.services;

import com.example.AuthService.DTO.RegisterRequestDTO;
import com.example.AuthService.entities.BaseUserModel;
import com.example.AuthService.enums.EventType;
import com.example.AuthService.enums.Topic;
import com.example.AuthService.exceptions.UsernameAlreadyExistsException;
import com.example.AuthService.kafka.events.CreateUserPayload;
import com.example.AuthService.kafka.events.Event;
import com.example.AuthService.kafka.KafkaProducer;
import com.example.AuthService.repositories.BaseUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final BaseUserRepository baseUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer kafkaProducer;


    public void addUser(RegisterRequestDTO registerRequestDTO) {
        BaseUserModel baseUserModel = new BaseUserModel();
        if (baseUserRepository.existsByUsername(registerRequestDTO.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (baseUserRepository.existsByEmail(registerRequestDTO.email())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        baseUserModel.setUsername(registerRequestDTO.username());
        baseUserModel.setEmail(registerRequestDTO.email());
        baseUserModel.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        baseUserRepository.saveAndFlush(baseUserModel);
        publishAddUserEvent(baseUserModel);
    }

    private void publishAddUserEvent(BaseUserModel baseUserModel) {
        CreateUserPayload createUserPayload = new CreateUserPayload(
                baseUserModel.getId(),
                baseUserModel.getUsername(),
                baseUserModel.getEmail()
        );

        UUID uuid = UUID.randomUUID();
        Event event = new Event(
                EventType.CREATE_USER,
                uuid,
                Instant.now().toString(),
                createUserPayload
        );
        log.info("Sending mesage create user:{}", event);
        kafkaProducer.publish(Topic.USERS_TOPIC, event);
    }
}
