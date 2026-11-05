package com.servillantas.alertas.notificacion.mapper;
import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import com.servillantas.alertas.notificacion.entity.Notificacion;
import org.springframework.stereotype.Component;
@Component public class NotificacionMapper {
    public NotificacionResponse toResponse(Notificacion v) { return new NotificacionResponse(v.id, v.alertaId,
            v.usuarioId, v.mensaje, v.leida, v.readAt, v.createdAt); }
}
