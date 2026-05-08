package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.ReporteResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {

    List<ReporteResponse> generate(String clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}
