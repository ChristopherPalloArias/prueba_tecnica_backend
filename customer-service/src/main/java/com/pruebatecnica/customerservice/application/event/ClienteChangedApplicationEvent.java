package com.pruebatecnica.customerservice.application.event;

import com.pruebatecnica.customerservice.domain.model.Cliente;
public record ClienteChangedApplicationEvent(
        ClienteApplicationEventType eventType,
        Cliente cliente
) {
}
