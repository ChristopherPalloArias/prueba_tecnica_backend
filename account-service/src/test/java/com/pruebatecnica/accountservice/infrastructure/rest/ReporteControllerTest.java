package com.pruebatecnica.accountservice.infrastructure.rest;

import com.pruebatecnica.accountservice.application.dto.ReporteResponse;
import com.pruebatecnica.accountservice.application.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    void generateDebeRetornarReportePlano() throws Exception {
        when(reporteService.generate(
                eq("CLI-001"),
                eq(LocalDate.of(2026, 5, 1)),
                eq(LocalDate.of(2026, 5, 31))
        )).thenReturn(List.of(new ReporteResponse(
                LocalDate.of(2026, 5, 8),
                "Jose Lema",
                "478758",
                "Ahorro",
                new BigDecimal("2000.00"),
                true,
                new BigDecimal("-575.00"),
                new BigDecimal("1425.00")
        )));

        mockMvc.perform(get("/reportes")
                        .param("clienteId", "CLI-001")
                        .param("fechaInicio", "2026-05-01")
                        .param("fechaFin", "2026-05-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fecha").value("2026-05-08"))
                .andExpect(jsonPath("$[0].cliente").value("Jose Lema"))
                .andExpect(jsonPath("$[0].numeroCuenta").value("478758"))
                .andExpect(jsonPath("$[0].tipo").value("Ahorro"))
                .andExpect(jsonPath("$[0].movimiento").value(-575.00))
                .andExpect(jsonPath("$[0].saldoDisponible").value(1425.00));
    }
}
