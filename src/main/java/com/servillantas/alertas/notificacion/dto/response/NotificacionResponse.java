package com.servillantas.alertas.notificacion.dto.response;
import java.time.OffsetDateTime;
import java.util.UUID;
public record NotificacionResponse(UUID id, UUID alertaId, UUID usuarioId, String mensaje, boolean leida,
        OffsetDateTime readAt, OffsetDateTime createdAt) {}
