package com.pruebatecnica.customerservice.application.service;

import com.pruebatecnica.customerservice.domain.model.Cliente;

public interface ClienteEventPublisher {

    void publishCreated(Cliente cliente);

    void publishUpdated(Cliente cliente);

    void publishStatusChanged(Cliente cliente);

    void publishDeleted(Cliente cliente);
}
