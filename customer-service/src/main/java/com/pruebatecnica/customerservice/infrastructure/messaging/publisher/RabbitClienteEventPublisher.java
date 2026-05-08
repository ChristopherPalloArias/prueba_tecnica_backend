package com.pruebatecnica.customerservice.infrastructure.messaging.publisher;

import com.pruebatecnica.customerservice.application.service.ClienteEventPublisher;
import com.pruebatecnica.customerservice.domain.model.Cliente;
import com.pruebatecnica.customerservice.infrastructure.messaging.event.CustomerCreatedEvent;
import com.pruebatecnica.customerservice.infrastructure.messaging.event.CustomerDeletedEvent;
import com.pruebatecnica.customerservice.infrastructure.messaging.event.CustomerEventType;
import com.pruebatecnica.customerservice.infrastructure.messaging.event.CustomerStatusChangedEvent;
import com.pruebatecnica.customerservice.infrastructure.messaging.event.CustomerUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RabbitClienteEventPublisher implements ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String createdRoutingKey;
    private final String updatedRoutingKey;
    private final String statusChangedRoutingKey;
    private final String deletedRoutingKey;

    public RabbitClienteEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${messaging.customer.exchange}") String exchange,
            @Value("${messaging.customer.routing-keys.created}") String createdRoutingKey,
            @Value("${messaging.customer.routing-keys.updated}") String updatedRoutingKey,
            @Value("${messaging.customer.routing-keys.status-changed}") String statusChangedRoutingKey,
            @Value("${messaging.customer.routing-keys.deleted}") String deletedRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.createdRoutingKey = createdRoutingKey;
        this.updatedRoutingKey = updatedRoutingKey;
        this.statusChangedRoutingKey = statusChangedRoutingKey;
        this.deletedRoutingKey = deletedRoutingKey;
    }

    @Override
    public void publishCreated(Cliente cliente) {
        CustomerCreatedEvent event = new CustomerCreatedEvent(
                UUID.randomUUID(),
                CustomerEventType.CUSTOMER_CREATED,
                LocalDateTime.now(),
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
        rabbitTemplate.convertAndSend(exchange, createdRoutingKey, event);
    }

    @Override
    public void publishUpdated(Cliente cliente) {
        CustomerUpdatedEvent event = new CustomerUpdatedEvent(
                UUID.randomUUID(),
                CustomerEventType.CUSTOMER_UPDATED,
                LocalDateTime.now(),
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
        rabbitTemplate.convertAndSend(exchange, updatedRoutingKey, event);
    }

    @Override
    public void publishStatusChanged(Cliente cliente) {
        CustomerStatusChangedEvent event = new CustomerStatusChangedEvent(
                UUID.randomUUID(),
                CustomerEventType.CUSTOMER_STATUS_CHANGED,
                LocalDateTime.now(),
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
        rabbitTemplate.convertAndSend(exchange, statusChangedRoutingKey, event);
    }

    @Override
    public void publishDeleted(Cliente cliente) {
        CustomerDeletedEvent event = new CustomerDeletedEvent(
                UUID.randomUUID(),
                CustomerEventType.CUSTOMER_DELETED,
                LocalDateTime.now(),
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
        rabbitTemplate.convertAndSend(exchange, deletedRoutingKey, event);
    }
}
