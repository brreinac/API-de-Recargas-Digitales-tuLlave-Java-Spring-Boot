# API de Recargas Digitales tuLlave

API REST para registrar recargas digitales de tarjetas tuLlave. La solución utiliza Java 21, Spring Boot 3, Spring Data JPA, PostgreSQL y Docker.

## 1. Tecnologías

- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Validation
- Spring Data JPA / Hibernate
- PostgreSQL 17
- Docker / Docker Compose
- OpenAPI / Swagger UI
- JUnit 5 / Mockito
- Maven

## 2. Arquitectura

La aplicación aplica una arquitectura por capas:

```text
HTTP
  -> Controller
  -> Service
  -> Repository
  -> PostgreSQL
```

Paquete raíz obligatorio:

```text
co.tullave.rcg
```

Responsabilidades principales:

- `controller`: contratos HTTP y validación de entrada.
- `service`: reglas y flujo de negocio.
- `repository`: persistencia mediante Spring Data JPA.
- `entity`: modelo persistente.
- `dto`: contratos de entrada/salida.
- `mapper`: transformación DTO/entity.
- `exception`: errores de dominio y manejo centralizado.

La entidad JPA no se expone directamente en la API.

## 3. Modelo de datos

`Recharge` contiene:

| Campo | Tipo | Regla |
|---|---|---|
| id | Long | Autogenerado |
| cardNumber | String | Exactamente 16 dígitos |
| amount | BigDecimal | 2.000 a 200.000 |
| paymentMethod | Enum | PSE, NEQUI, DAVIPLATA, CREDIT_CARD |
| createdAt | LocalDateTime | Generado automáticamente |

## 4. Endpoints

### Crear recarga

`POST /api/v1/recharges`

Respuesta exitosa: `201 Created`.

```json
{
  "cardNumber": "1010000012345678",
  "amount": 50000,
  "paymentMethod": "NEQUI"
}
```

### Listar recargas

`GET /api/v1/getRecharges?page=0&size=20`

Filtro opcional:

`GET /api/v1/getRecharges?page=0&size=20&cardNumber=1010000012345678`

La consulta se ejecuta con paginación en la capa de persistencia; no se carga la tabla completa en memoria.

### Eliminar recarga

`DELETE /api/v1/recharges/{id}`

- `204 No Content` si existe.
- `404 Not Found` si no existe.

## 5. Validaciones

- `cardNumber`: requerido y exactamente 16 dígitos numéricos.
- `amount`: requerido, mínimo 2.000 y máximo 200.000.
- `paymentMethod`: requerido y limitado al enum definido.
- `page`: mayor o igual a 0.
- `size`: entre 1 y 100.

Los errores se centralizan en `TlvErrorAdvisor` y se devuelven con una estructura consistente.

## 6. Manejo de errores

Ejemplo conceptual de respuesta:

```json
{
  "timestamp": "2026-10-04T21:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Request inválido",
  "path": "/api/v1/recharges",
  "details": [
    "cardNumber: cardNumber must contain exactly 16 numeric digits"
  ]
}
```

No se exponen stack traces al consumidor.

## 7. Variables de entorno

La configuración soporta:

- `DB_URL` — por defecto `jdbc:postgresql://localhost:5432/tullave`.
- `DB_USERNAME` — por defecto `tullave`.
- `DB_PASSWORD` — por defecto `tullave`.
- `SERVER_PORT` — por defecto `8080`.

Para Docker Compose:

- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`

No se almacenan secretos reales en el repositorio.

## 8. Docker

El `Dockerfile` utiliza dos etapas:

1. Construcción Maven con JDK 21.
2. Ejecución con JRE 21 y usuario no privilegiado.

`docker-compose.yml` levanta:

- PostgreSQL 17.
- API Spring Boot.

PostgreSQL dispone de healthcheck y la API espera a que la base de datos esté saludable.

La API queda disponible en:

`http://localhost:8080`

### Ejecución

```bash
docker compose up --build
```

## 9. Swagger UI

La documentación OpenAPI se encuentra en:

`http://localhost:8080/swagger-ui.html`

OpenAPI JSON:

`http://localhost:8080/v3/api-docs`

## 10. Pruebas unitarias

Se incluyen pruebas unitarias del servicio utilizando JUnit 5 y Mockito.

Cubren, entre otros:

- creación exitosa.
- consulta paginada.
- consulta filtrada por cardNumber.
- eliminación exitosa.
- eliminación de recurso inexistente.

Las pruebas forman parte del código fuente y fueron generadas como entregable. No se ejecutaron durante la construcción de esta entrega, conforme a la instrucción de la prueba asistida.

## 11. Postman

La colección está en:

`postman/TLV-RCG-26.postman_collection.json`

Nombre exacto de la colección:

`TLV-RCG-26`

Incluye casos exitosos y de error para POST, GET y DELETE.

## 12. Git

Se utilizó una estrategia de ramas y commits graduales con Conventional Commits.

Ramas principales preparadas:

- `main`
- `feature/recharge-api`
- `feature/docker`
- `feature/documentation`

Ejemplos de commits:

- `chore: initialize spring boot project`
- `feat: add recharge domain model`
- `feat: add recharge persistence layer`
- `feat: add recharge service`
- `feat: add recharge rest endpoints`
- `fix: validate optional card number filter`
- `test: add recharge service unit tests`
- `feat: add docker configuration`

La descripción preparada para Pull Request está en `docs/PULL_REQUEST.md`. El repositorio conserva el remoto existente del proyecto. La historia parte del commit inicial ya existente y los cambios de la prueba se agregan como commits posteriores. La creación del PR remoto queda pendiente de publicar las ramas en el proveedor Git con las credenciales del propietario.

## 13. Decisiones técnicas

### BigDecimal para dinero
Evita los problemas de precisión propios de `float` y `double`.

### DTOs
Separan el contrato HTTP del modelo persistente y evitan exponer detalles de JPA.

### Paginación en Repository
La consulta usa `Pageable` y delega la paginación a PostgreSQL mediante Spring Data JPA.

### Enum persistido como texto
`PaymentMethod` se almacena con `EnumType.STRING`, evitando dependencia del orden ordinal del enum.

### Manejo centralizado de errores
`TlvErrorAdvisor` mantiene un contrato uniforme para validaciones, recursos inexistentes, solicitudes malformadas y errores inesperados.

### Constructor injection
Las dependencias son explícitas, facilitan pruebas unitarias y evitan inyección por atributos.

### Docker multi-stage
Se separa la construcción de la ejecución y la imagen final no necesita Maven.

## 14. Mejoras futuras

En una evolución productiva podrían incorporarse migraciones versionadas con Flyway, observabilidad, métricas, autenticación/autorización y una estrategia más estricta de `ddl-auto` según el ciclo de despliegue.

## 15. Alcance de esta entrega

La solución implementa el alcance funcional obligatorio y los extras de Swagger y pruebas unitarias indicados en la prueba técnica.

Ref. interna: TLV-RCG-26
