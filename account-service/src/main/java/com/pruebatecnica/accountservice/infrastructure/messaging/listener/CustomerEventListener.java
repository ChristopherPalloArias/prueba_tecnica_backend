package com.pruebatecnica.accountservice.infrastructure.messaging.listener;

import com.pruebatecnica.accountservice.application.service.ClienteReplicaService;
import com.pruebatecnica.accountservice.infrastructure.messaging.event.CustomerEventPayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerEventListener.class);

    private final ClienteReplicaService clienteReplicaService;

    public CustomerEventListener(ClienteReplicaService clienteReplicaService) {
        this.clienteReplicaService = clienteReplicaService;
    }

    @RabbitListener(queues = "${messaging.customer.queue}")
    public void handle(CustomerEventPayload event) {
        if (event == null || event.eventType() == null) {
            LOGGER.warn("Evento de cliente ignorado por payload inválido: {}", event);
            return;
        }

        if (!isKnownEvent(event.eventType())) {
            LOGGER.warn("Evento de cliente ignorado por tipo desconocido: {}", event.eventType());
            return;
        }

        boolean estado = switch (event.eventType()) {
            case "CUSTOMER_DELETED" -> false;
            case "CUSTOMER_CREATED", "CUSTOMER_UPDATED", "CUSTOMER_STATUS_CHANGED" -> Boolean.TRUE.equals(event.estado());
            default -> throw new IllegalStateException("Tipo de evento no soportado: " + event.eventType());
        };

        clienteReplicaService.upsert(
                event.clienteId(),
                event.nombre(),
                event.identificacion(),
                estado
        );
    }

    private boolean isKnownEvent(String eventType) {
        return "CUSTOMER_CREATED".equals(eventType)
                || "CUSTOMER_UPDATED".equals(eventType)
                || "CUSTOMER_STATUS_CHANGED".equals(eventType)
                || "CUSTOMER_DELETED".equals(eventType);
    }
}
