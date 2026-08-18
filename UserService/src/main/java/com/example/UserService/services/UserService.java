package com.example.UserService.services;

import com.example.UserService.entities.UserModel;
import com.example.UserService.kafka.CreateUserPayload;
import com.example.UserService.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;

    public void handleCreateUser(CreateUserPayload createUserPayload) {
        log.info("Received ==== {}", createUserPayload);
        UserModel userModel = new UserModel();
        userModel.setUsername(createUserPayload.getUsername());
        userModel.setUserId(createUserPayload.getUserId());
        userModel.setEmail(createUserPayload.getEmail());
        userRepository.saveAndFlush(userModel);
        log.info("Saved user {}", userModel);
        walletService.createWallet(userModel);
    }
}
