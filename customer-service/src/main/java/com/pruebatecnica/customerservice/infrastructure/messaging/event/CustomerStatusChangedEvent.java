package com.pruebatecnica.customerservice.infrastructure.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerStatusChangedEvent(
        UUID eventId,
        CustomerEventType eventType,
        LocalDateTime occurredAt,
        String clienteId,
        String nombre,
        String identificacion,
        Boolean estado
) {
}
