package com.pruebatecnica.customerservice.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebatecnica.customerservice.application.dto.ClienteCreateRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteResponse;
import com.pruebatecnica.customerservice.application.service.ClienteService;
import com.pruebatecnica.customerservice.domain.exception.DuplicateClienteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    void createDebeRetornar201YClienteCreado() throws Exception {
        ClienteCreateRequest request = new ClienteCreateRequest(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "1234"
        );

        ClienteResponse response = new ClienteResponse(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                true
        );

        when(clienteService.create(any(ClienteCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/clientes/CLI-001"))
                .andExpect(jsonPath("$.clienteId").value("CLI-001"))
                .andExpect(jsonPath("$.nombre").value("Jose Lema"))
                .andExpect(jsonPath("$.identificacion").value("1234567890"))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    void createDebeRetornar400CuandoRequestEsInvalido() throws Exception {
        ClienteCreateRequest request = new ClienteCreateRequest(
                "",
                "",
                "Masculino",
                -1,
                "",
                "Otavalo sn y principal",
                "0987654321",
                ""
        );

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void createDebeRetornar409CuandoClienteIdEstaDuplicado() throws Exception {
        ClienteCreateRequest request = new ClienteCreateRequest(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "1234"
        );

        when(clienteService.create(any(ClienteCreateRequest.class)))
                .thenThrow(new DuplicateClienteException("El clienteId ya existe"));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("El clienteId ya existe"));
    }

    @Test
    void deleteDebeDelegarBorradoLogicoYRetornar204() throws Exception {
        mockMvc.perform(delete("/clientes/CLI-001"))
                .andExpect(status().isNoContent());

        verify(clienteService).delete("CLI-001");
    }

    @Test
    void findAllDebeListarSoloActivosPorDefecto() throws Exception {
        when(clienteService.findAll(false)).thenReturn(List.of());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());

        verify(clienteService).findAll(false);
    }

    @Test
    void findAllDebePermitirIncluirInactivos() throws Exception {
        when(clienteService.findAll(true)).thenReturn(List.of());

        mockMvc.perform(get("/clientes?incluirInactivos=true"))
                .andExpect(status().isOk());

        verify(clienteService).findAll(true);
    }
}
