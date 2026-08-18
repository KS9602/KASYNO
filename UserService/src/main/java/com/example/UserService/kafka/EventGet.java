package com.example.UserService.kafka;


import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record EventGet(
        String eventType,
        UUID eventId,
        String occurredAt,
        JsonNode payload
) {
}
