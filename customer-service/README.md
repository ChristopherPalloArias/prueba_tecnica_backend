# customer-service

Microservicio responsable de Persona y Cliente.

## Endpoints

- `POST /clientes`: crea un cliente activo.
- `GET /clientes`: lista clientes activos por defecto.
- `GET /clientes?incluirInactivos=true`: lista clientes activos e inactivos.
- `GET /clientes/{clienteId}`: obtiene un cliente por identificador de negocio.
- `PUT /clientes/{clienteId}`: actualiza datos del cliente.
- `PATCH /clientes/{clienteId}/estado`: activa o desactiva el cliente.
- `DELETE /clientes/{clienteId}`: realiza borrado lógico con `estado=false`.

La API usa `clienteId` como identificador de negocio. El `id` técnico queda reservado para JPA y la herencia `JOINED`.
