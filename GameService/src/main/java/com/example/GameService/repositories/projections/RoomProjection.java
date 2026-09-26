package com.example.GameService.repositories.projections;

import com.example.GameService.enums.RoomStatus;

public interface RoomProjection {

    Long getId();

    String getName();

    Long getPlayersCount();

    Integer getMaxPlayers();

    RoomStatus getStatus();
}