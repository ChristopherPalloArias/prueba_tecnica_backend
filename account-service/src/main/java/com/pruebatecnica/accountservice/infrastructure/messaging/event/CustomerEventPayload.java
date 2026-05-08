package com.pruebatecnica.accountservice.infrastructure.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerEventPayload(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        String clienteId,
        String nombre,
        String identificacion,
        Boolean estado
) {
}
