package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.EstadoRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoCreateRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoResponse;
import com.pruebatecnica.accountservice.application.dto.MovimientoUpdateRequest;

import java.util.List;

public interface MovimientoService {

    MovimientoResponse create(MovimientoCreateRequest request);

    List<MovimientoResponse> findAll(boolean incluirInactivos);

    MovimientoResponse findById(Long id);

    MovimientoResponse update(Long id, MovimientoUpdateRequest request);

    MovimientoResponse updateEstado(Long id, EstadoRequest request);

    void delete(Long id);
}
