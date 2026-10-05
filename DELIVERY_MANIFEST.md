# TLV-RCG-26 — Manifest de Entrega

## Alcance obligatorio

- [x] Java 21
- [x] Spring Boot 3
- [x] Spring Data JPA
- [x] PostgreSQL
- [x] Entidad `Recharge`
- [x] `cardNumber` de 16 dígitos
- [x] `amount` entre 2.000 y 200.000
- [x] `PaymentMethod`: PSE, NEQUI, DAVIPLATA, CREDIT_CARD
- [x] `createdAt` autogenerado
- [x] POST `/api/v1/recharges`
- [x] GET `/api/v1/getRecharges`
- [x] DELETE `/api/v1/recharges/{id}`
- [x] DTOs y validaciones
- [x] HTTP 201 / 400 / 204 / 404
- [x] `TlvErrorAdvisor`
- [x] paquete raíz `co.tullave.rcg`
- [x] SLF4J
- [x] Controller / Service / Repository
- [x] Dockerfile
- [x] docker-compose.yml
- [x] PostgreSQL en Docker
- [x] puerto 8080
- [x] README
- [x] colección Postman `TLV-RCG-26`
- [x] Conventional Commits
- [x] ramas `feature/recharge-api` y `feature/docker`
- [x] descripción de Pull Request preparada

## Bonus

- [x] Swagger/OpenAPI
- [x] JUnit 5
- [x] Mockito
- [x] pruebas unitarias reales generadas y no ejecutadas

## Entregables adicionales

- `docs/PULL_REQUEST.md`: descripción lista para un PR remoto.
- `docs/STATIC_REVIEW.md`: registro de revisión estática.
- `.git/`: historial Git completo con commits graduales y ramas.

## Restricción de ejecución

No se ejecutaron pruebas, Docker, Postman/Newman ni la aplicación durante la preparación de esta entrega. La revisión realizada fue estática.
