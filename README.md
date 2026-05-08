# Prueba Tecnica Backend

Backend para una prueba tecnica SemiSenior implementado con dos microservicios Spring Boot:

- `customer-service`: gestiona Persona y Cliente.
- `account-service`: gestiona Cuenta, Movimiento, Reportes y una replica minima de Cliente.

La solucion prioriza claridad, separacion de responsabilidades, JPA, REST, pruebas automatizadas y ejecucion reproducible con Docker Compose.

## Arquitectura

Se usan exactamente dos microservicios:

1. `customer-service`
   - Expone la API publica `/clientes`.
   - Modela la herencia `Persona -> Cliente` con JPA `JOINED`.
   - Usa `clienteId` como identificador de negocio en la API.
   - Mantiene el `id` tecnico solo para JPA.
   - Realiza borrado logico con `estado=false`.
   - Publica eventos RabbitMQ despues del commit de la transaccion.

2. `account-service`
   - Expone `/cuentas`, `/movimientos` y `/reportes`.
   - Mantiene `ClienteReplica` con la informacion minima recibida por RabbitMQ.
   - No depende de llamadas sincronas al `customer-service`.
   - Valida que una cuenta solo se cree para clientes existentes y activos en la replica.
   - Registra movimientos y actualiza saldo en una transaccion.

Esta division es suficiente para el nivel SemiSenior porque separa clientes de productos financieros sin introducir API Gateway, service discovery, Kubernetes ni patrones mas complejos.

## Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Maven
- Docker Compose
- JUnit 5, MockMvc y `@DataJpaTest`

## Ejecucion con Docker Compose

Desde la raiz del repositorio:

```bash
docker compose up --build
```

Servicios expuestos:

- `customer-service`: `http://localhost:8081`
- `account-service`: `http://localhost:8082`
- `customer-db`: `localhost:5433`
- `account-db`: `localhost:5434`
- RabbitMQ AMQP: `localhost:5672`
- RabbitMQ Management: `http://localhost:15672` (`guest` / `guest`)

Para reiniciar completamente las bases y evitar datos duplicados de pruebas anteriores:

```bash
docker compose down -v
docker compose up --build
```

## BaseDatos.sql

La estrategia final es mantener un `BaseDatos.sql` por microservicio:

- `customer-service/BaseDatos.sql`
- `account-service/BaseDatos.sql`

Esto mantiene aislado el modelo de datos de cada servicio y es coherente con la separacion de bases por microservicio. Docker Compose monta cada script en su respectivo contenedor PostgreSQL para inicializar las tablas.

## Orden recomendado de prueba

Importar la coleccion Postman y ejecutar primero la carpeta `Flujo recomendado`.

1. Crear cliente en `POST /clientes`.
2. Esperar unos segundos para que RabbitMQ replique el cliente en `account-service`.
3. Crear cuenta en `POST /cuentas`.
4. Registrar deposito en `POST /movimientos`.
5. Registrar retiro valido en `POST /movimientos`.
6. Probar retiro sin saldo suficiente y validar el mensaje `Saldo no disponible`.
7. Consultar `GET /reportes?clienteId=...&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD`.

La coleccion Postman esta en:

```text
postman/PruebaTecnicaBackend.postman_collection.json
```

La creacion de cuenta usa consistencia eventual: `customer-service` publica el evento del cliente por RabbitMQ y `account-service` lo consume para actualizar `ClienteReplica`; si se prueba demasiado rapido, basta esperar unos segundos y reintentar la creacion de cuenta.

Las operaciones de cambio de estado y deletes logicos estan separadas en la carpeta `Operaciones de mantenimiento` para no romper el flujo principal de prueba.

## Endpoints principales

### Clientes

- `POST /clientes`
- `GET /clientes`
- `GET /clientes?incluirInactivos=true`
- `GET /clientes/{clienteId}`
- `PUT /clientes/{clienteId}`
- `PATCH /clientes/{clienteId}/estado`
- `DELETE /clientes/{clienteId}`

### Cuentas

- `POST /cuentas`
- `GET /cuentas`
- `GET /cuentas?incluirInactivas=true`
- `GET /cuentas/{id}`
- `PUT /cuentas/{id}`
- `PATCH /cuentas/{id}/estado`
- `DELETE /cuentas/{id}`

### Movimientos

- `POST /movimientos`
- `GET /movimientos`
- `GET /movimientos?incluirInactivos=true`
- `GET /movimientos/{id}`
- `PUT /movimientos/{id}`
- `PATCH /movimientos/{id}/estado`
- `DELETE /movimientos/{id}`

`PUT /movimientos/{id}` solo actualiza metadata (`descripcion`) para no alterar el valor financiero original ni romper trazabilidad.

### Reportes

- `GET /reportes?clienteId={clienteId}&fechaInicio={YYYY-MM-DD}&fechaFin={YYYY-MM-DD}`

El reporte devuelve un JSON plano con fecha, cliente, numero de cuenta, tipo, saldo inicial, estado, movimiento y saldo disponible. Por defecto considera solo movimientos activos.

## RabbitMQ y ClienteReplica

`customer-service` publica eventos cuando un cliente se crea, actualiza, cambia de estado o se elimina logicamente:

- `CustomerCreatedEvent`
- `CustomerUpdatedEvent`
- `CustomerStatusChangedEvent`
- `CustomerDeletedEvent`

`account-service` consume esos eventos y actualiza `clientes_replica` con:

- `clienteId`
- `nombre`
- `identificacion`
- `estado`

La replica evita acoplamiento sincronico entre servicios y permite validar reglas de Cuenta sin llamar al `customer-service`.

## Reglas de movimientos

- Valor positivo: deposito.
- Valor negativo: retiro.
- Valor cero: invalido.
- No se permiten movimientos sobre cuentas inactivas.
- En un retiro se valida que el saldo no quede negativo.
- Si no hay saldo suficiente, la respuesta usa el mensaje exacto: `Saldo no disponible`.
- El movimiento guarda el saldo resultante.
- La cuenta usa optimistic locking con `@Version` para proteger el saldo ante actualizaciones concurrentes.

## Tests

Ejecutar pruebas de cada microservicio:

```bash
cd customer-service
mvn test

cd ../account-service
mvn test
```

Las pruebas cubren controllers con MockMvc, reglas de dominio/servicio, persistencia JPA, constraints principales, eventos RabbitMQ y reglas financieras.

## Estructura del repositorio

```text
.
├── account-service
│   ├── BaseDatos.sql
│   ├── Dockerfile
│   ├── pom.xml
│   └── src
├── customer-service
│   ├── BaseDatos.sql
│   ├── Dockerfile
│   ├── pom.xml
│   └── src
├── postman
│   └── PruebaTecnicaBackend.postman_collection.json
├── docker-compose.yml
└── README.md
```

## Decisiones defendibles en entrevista

- Dos microservicios porque el reto separa cliente/persona de cuentas/movimientos.
- Comunicacion asincrona con RabbitMQ solo para replicar datos minimos de cliente.
- `clienteId` es identificador de negocio; el `id` tecnico queda interno para JPA.
- `JOINED` modela correctamente la herencia `Persona -> Cliente`.
- Borrado logico para no perder informacion relevante.
- `PUT` de movimiento no modifica el valor financiero aplicado.
- Optimistic locking protege saldo sin introducir una solucion sobredimensionada.
- Se usan DTOs y mappers simples; las entidades JPA no se exponen directamente.

## Mejoras futuras no bloqueantes

- Agregar migraciones con Flyway o Liquibase.
- Agregar `docker compose` profile para ejecutar tests en contenedor.
- Agregar reintentos o dead-letter queue para eventos RabbitMQ.
- Agregar paginacion en listados si el volumen de datos creciera.
