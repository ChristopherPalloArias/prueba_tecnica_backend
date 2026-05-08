package com.pruebatecnica.customerservice.infrastructure.messaging.listener;

import com.pruebatecnica.customerservice.application.event.ClienteChangedApplicationEvent;
import com.pruebatecnica.customerservice.application.service.ClienteEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ClienteChangedApplicationEventListener {

    private final ClienteEventPublisher clienteEventPublisher;

    public ClienteChangedApplicationEventListener(ClienteEventPublisher clienteEventPublisher) {
        this.clienteEventPublisher = clienteEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ClienteChangedApplicationEvent event) {
        switch (event.eventType()) {
            case CREATED -> clienteEventPublisher.publishCreated(event.cliente());
            case UPDATED -> clienteEventPublisher.publishUpdated(event.cliente());
            case STATUS_CHANGED -> clienteEventPublisher.publishStatusChanged(event.cliente());
            case DELETED -> clienteEventPublisher.publishDeleted(event.cliente());
        }
    }
}
