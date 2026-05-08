package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.CuentaCreateRequest;
import com.pruebatecnica.accountservice.application.dto.CuentaResponse;
import com.pruebatecnica.accountservice.application.dto.CuentaUpdateRequest;
import com.pruebatecnica.accountservice.application.dto.EstadoRequest;

import java.util.List;

public interface CuentaService {

    CuentaResponse create(CuentaCreateRequest request);

    List<CuentaResponse> findAll(boolean incluirInactivas);

    CuentaResponse findById(Long id);

    CuentaResponse update(Long id, CuentaUpdateRequest request);

    CuentaResponse updateEstado(Long id, EstadoRequest request);

    void delete(Long id);
}
