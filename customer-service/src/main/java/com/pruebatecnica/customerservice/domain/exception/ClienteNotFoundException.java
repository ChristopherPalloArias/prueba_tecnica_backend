package com.pruebatecnica.customerservice.domain.exception;

public class ClienteNotFoundException extends DomainException {

    public ClienteNotFoundException(String clienteId) {
        super("Cliente no encontrado: " + clienteId);
    }
}
