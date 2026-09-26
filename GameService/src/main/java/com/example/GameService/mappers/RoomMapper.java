package com.example.GameService.mappers;


import com.example.GameService.dto.room.RoomResponse;
import com.example.GameService.repositories.projections.RoomProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomResponse projectionToResponse(RoomProjection projection);
}
