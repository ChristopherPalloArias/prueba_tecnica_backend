package com.pruebatecnica.customerservice.application.service;

import com.pruebatecnica.customerservice.application.dto.ClienteCreateRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteEstadoRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteResponse;
import com.pruebatecnica.customerservice.application.dto.ClienteUpdateRequest;

import java.util.List;

public interface ClienteService {

    ClienteResponse create(ClienteCreateRequest request);

    List<ClienteResponse> findAll();

    ClienteResponse findByClienteId(String clienteId);

    ClienteResponse update(String clienteId, ClienteUpdateRequest request);

    ClienteResponse updateEstado(String clienteId, ClienteEstadoRequest request);

    void delete(String clienteId);
}
