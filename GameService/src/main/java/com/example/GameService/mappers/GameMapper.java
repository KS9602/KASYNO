package com.example.GameService.mappers;

import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.entities.GameModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameResponse toResponse(GameModel gameModel);
}
