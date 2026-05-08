package com.pruebatecnica.customerservice.application.service;

import com.pruebatecnica.customerservice.application.event.ClienteApplicationEventType;
import com.pruebatecnica.customerservice.application.event.ClienteChangedApplicationEvent;
import com.pruebatecnica.customerservice.application.dto.ClienteCreateRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteEstadoRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteResponse;
import com.pruebatecnica.customerservice.application.dto.ClienteUpdateRequest;
import com.pruebatecnica.customerservice.domain.exception.ClienteNotFoundException;
import com.pruebatecnica.customerservice.domain.exception.DuplicateClienteException;
import com.pruebatecnica.customerservice.domain.model.Cliente;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepositoryPort clienteRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;

    public ClienteServiceImpl(
            ClienteRepositoryPort clienteRepository,
            ApplicationEventPublisher applicationEventPublisher,
            PasswordEncoder passwordEncoder
    ) {
        this.clienteRepository = clienteRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ClienteResponse create(ClienteCreateRequest request) {
        if (clienteRepository.existsByClienteId(request.clienteId())) {
            throw new DuplicateClienteException("El clienteId ya existe");
        }
        if (clienteRepository.existsByIdentificacion(request.identificacion())) {
            throw new DuplicateClienteException("La identificación ya existe");
        }

        Cliente cliente = Cliente.crear(
                request.clienteId(),
                request.nombre(),
                request.genero(),
                request.edad(),
                request.identificacion(),
                request.direccion(),
                request.telefono(),
                passwordEncoder.encode(request.contrasena())
        );

        Cliente saved = clienteRepository.save(cliente);
        publishAfterCommit(ClienteApplicationEventType.CREATED, saved);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> findAll(boolean incluirInactivos) {
        List<Cliente> clientes = incluirInactivos
                ? clienteRepository.findAll()
                : clienteRepository.findAllActive();

        return clientes
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse findByClienteId(String clienteId) {
        Cliente cliente = getCliente(clienteId);
        return toResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse update(String clienteId, ClienteUpdateRequest request) {
        Cliente cliente = getCliente(clienteId);

        if (clienteRepository.existsByIdentificacionAndClienteIdNot(request.identificacion(), clienteId)) {
            throw new DuplicateClienteException("La identificación ya existe");
        }

        cliente.actualizarDatos(
                request.nombre(),
                request.genero(),
                request.edad(),
                request.identificacion(),
                request.direccion(),
                request.telefono(),
                passwordEncoder.encode(request.contrasena())
        );

        Cliente saved = clienteRepository.save(cliente);
        publishAfterCommit(ClienteApplicationEventType.UPDATED, saved);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public ClienteResponse updateEstado(String clienteId, ClienteEstadoRequest request) {
        Cliente cliente = getCliente(clienteId);
        cliente.cambiarEstado(request.estado());

        Cliente saved = clienteRepository.save(cliente);
        publishAfterCommit(ClienteApplicationEventType.STATUS_CHANGED, saved);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(String clienteId) {
        Cliente cliente = getCliente(clienteId);
        cliente.desactivar();

        Cliente saved = clienteRepository.save(cliente);
        publishAfterCommit(ClienteApplicationEventType.DELETED, saved);
    }

    private Cliente getCliente(String clienteId) {
        return clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));
    }

    private void publishAfterCommit(ClienteApplicationEventType eventType, Cliente cliente) {
        applicationEventPublisher.publishEvent(new ClienteChangedApplicationEvent(eventType, cliente));
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.isEstado()
        );
    }
}
