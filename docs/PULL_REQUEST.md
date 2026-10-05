# Pull Request — TLV-RCG-26

## Título
feat: implement tuLlave recharge API

## Resumen
Implementación completa de la API REST de recargas digitales tuLlave con Spring Boot 3, Java 21, Spring Data JPA, PostgreSQL y Docker.

## Cambios principales
- Modelo `Recharge` y `PaymentMethod`.
- DTOs con Bean Validation.
- Servicio y repositorio con paginación y filtro por tarjeta.
- Endpoints REST requeridos.
- Manejo centralizado mediante `TlvErrorAdvisor`.
- Logs con SLF4J.
- Dockerfile y Docker Compose con PostgreSQL.
- Swagger/OpenAPI.
- Pruebas unitarias JUnit 5 + Mockito generadas, sin ejecución durante la construcción de esta entrega.
- Colección Postman `TLV-RCG-26`.
- README de arquitectura, ejecución y decisiones técnicas.

## Validación estática
Se revisaron paquetes, referencias, nombres obligatorios, DTO/entity mapping, endpoints, dependencias, configuración, Docker, Postman y documentación sin ejecutar pruebas ni levantar la aplicación.

## Notas
El repositorio conserva el remoto existente del proyecto. Esta descripción queda lista para abrir el Pull Request después de publicar la rama correspondiente en el proveedor Git con las credenciales del propietario.
