package com.example.demo.mappers;

import com.example.demo.DTO.SimpleUserDTO;
import com.example.demo.DTO.UserRegisterDTO;
import com.example.demo.entities.BaseUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    BaseUser toEntity(UserRegisterDTO userRegisterDTO);
    SimpleUserDTO toResponse(BaseUser baseUser);
}
