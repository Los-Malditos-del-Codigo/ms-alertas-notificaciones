package com.servillantas.alertas.alerta.dto.response;
import java.time.OffsetDateTime;
import java.util.UUID;
public record AlertaResponse(UUID id, UUID productoId, String tipo, String estado, String mensaje,
        OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime resueltaAt) {}
