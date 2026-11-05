# MS Alertas y Notificaciones

Gestiona alertas operativas de stock bajo y las notificaciones dirigidas a usuarios.

## Responsabilidades

- Propietario de `alertas` y `notificaciones`.
- Mantiene una alerta activa por producto y permite resolverla.
- Evita notificar dos veces al mismo usuario por una alerta.
- Valida referencias de producto y usuario contra el modelo consolidado.
- Impide que un usuario lea notificaciones pertenecientes a otro.

## API

- `POST/GET /api/v1/alertas`.
- `PATCH /api/v1/alertas/{id}/resolver`.
- `POST /api/v1/alertas/{id}/notificaciones`.
- `GET /api/v1/notificaciones` y `PATCH /api/v1/notificaciones/{id}/leer`.

## Configuración y ejecución

Se registra como `ms-alertas-notificaciones`, puerto `8087`, y obtiene `ms-alertas-notificaciones.yml` desde Config Server. Requiere PostgreSQL y Eureka.

```bash
./mvnw test
./mvnw spring-boot:run
```
