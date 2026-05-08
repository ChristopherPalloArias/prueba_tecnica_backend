package com.pruebatecnica.accountservice.infrastructure.messaging.listener;

import com.pruebatecnica.accountservice.application.service.ClienteReplicaService;
import com.pruebatecnica.accountservice.infrastructure.messaging.event.CustomerEventPayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener {

    private final ClienteReplicaService clienteReplicaService;

    public CustomerEventListener(ClienteReplicaService clienteReplicaService) {
        this.clienteReplicaService = clienteReplicaService;
    }

    @RabbitListener(queues = "${messaging.customer.queue}")
    public void handle(CustomerEventPayload event) {
        boolean estado = switch (event.eventType()) {
            case "CUSTOMER_DELETED" -> false;
            default -> Boolean.TRUE.equals(event.estado());
        };

        clienteReplicaService.upsert(
                event.clienteId(),
                event.nombre(),
                event.identificacion(),
                estado
        );
    }
}
