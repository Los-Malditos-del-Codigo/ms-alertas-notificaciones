package com.servillantas.alertas.notificacion.service;
import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import java.util.UUID;
import org.springframework.data.domain.*;
public interface NotificacionService {
    Page<NotificacionResponse> listar(UUID usuarioId, Boolean leida, Pageable pageable);
    NotificacionResponse leer(UUID id, UUID usuarioId);
}
