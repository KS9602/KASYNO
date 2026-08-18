package com.example.GameService.mappers;

import com.example.GameService.dto.PlayerDTO;
import com.example.GameService.entities.PlayerModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerModel toEntity(PlayerDTO playerDTO);
    PlayerDTO toDTO(PlayerModel playerModel);
}
