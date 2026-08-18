package com.example.AuthService.kafka.events;

import com.example.AuthService.enums.EventType;

import java.time.LocalDate;
import java.util.UUID;

public record Event(
        EventType eventType,
        UUID eventId,
        String occuredAt,
        Object payload
) {
}
