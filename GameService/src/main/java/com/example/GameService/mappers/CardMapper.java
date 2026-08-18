package com.example.GameService.mappers;

import com.example.GameService.dto.CardDTO;
import com.example.GameService.entities.CardModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDTO toDTO(CardModel card);
    CardModel toModel(CardDTO cardDTO);
}
