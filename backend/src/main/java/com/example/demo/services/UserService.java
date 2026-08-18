package com.example.demo.services;

import com.example.demo.DTO.SimpleUserDTO;
import com.example.demo.entities.BaseUser;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.UserRegisterDTO;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

        public SimpleUserDTO registerUser(UserRegisterDTO userRegisterDTO){
            BaseUser baseUser = userRepository.save(userMapper.toEntity(userRegisterDTO));
            return userMapper.toResponse(baseUser);
    }

}
