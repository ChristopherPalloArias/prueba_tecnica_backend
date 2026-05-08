package com.pruebatecnica.customerservice.application.service;

import com.pruebatecnica.customerservice.application.dto.ClienteCreateRequest;
import com.pruebatecnica.customerservice.domain.exception.DuplicateClienteException;
import com.pruebatecnica.customerservice.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void createDebeRechazarClienteIdDuplicado() {
        ClienteCreateRequest request = validCreateRequest();
        when(clienteRepository.existsByClienteId("CLI-001")).thenReturn(true);

        assertThrows(DuplicateClienteException.class, () -> clienteService.create(request));

        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @Test
    void createDebeRechazarIdentificacionDuplicada() {
        ClienteCreateRequest request = validCreateRequest();
        when(clienteRepository.existsByClienteId("CLI-001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        assertThrows(DuplicateClienteException.class, () -> clienteService.create(request));

        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @Test
    void createDebeGuardarContrasenaHasheada() {
        ClienteCreateRequest request = validCreateRequest();
        when(clienteRepository.existsByClienteId("CLI-001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("$2a$hash");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        clienteService.create(request);

        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(clienteCaptor.capture());
        assertEquals("$2a$hash", clienteCaptor.getValue().getContrasena());
    }

    private ClienteCreateRequest validCreateRequest() {
        return new ClienteCreateRequest(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "1234"
        );
    }
}
