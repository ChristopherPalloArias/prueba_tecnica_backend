package com.pruebatecnica.accountservice.infrastructure.messaging.listener;

import com.pruebatecnica.accountservice.application.service.ClienteReplicaService;
import com.pruebatecnica.accountservice.infrastructure.messaging.event.CustomerEventPayload;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class CustomerEventListenerTest {

    @Test
    void handleDebeInactivarReplicaCuandoEventoEsDeleted() {
        ClienteReplicaService clienteReplicaService = mock(ClienteReplicaService.class);
        CustomerEventListener listener = new CustomerEventListener(clienteReplicaService);

        listener.handle(new CustomerEventPayload(
                UUID.randomUUID(),
                "CUSTOMER_DELETED",
                LocalDateTime.now(),
                "CLI-001",
                "Jose Lema",
                "1234567890",
                true
        ));

        verify(clienteReplicaService).upsert("CLI-001", "Jose Lema", "1234567890", false);
    }

    @Test
    void handleDebeIgnorarEventoDesconocidoSinActualizarReplica() {
        ClienteReplicaService clienteReplicaService = mock(ClienteReplicaService.class);
        CustomerEventListener listener = new CustomerEventListener(clienteReplicaService);

        listener.handle(new CustomerEventPayload(
                UUID.randomUUID(),
                "CUSTOMER_PASSWORD_CHANGED",
                LocalDateTime.now(),
                "CLI-001",
                "Jose Lema",
                "1234567890",
                true
        ));

        verifyNoInteractions(clienteReplicaService);
    }
}
