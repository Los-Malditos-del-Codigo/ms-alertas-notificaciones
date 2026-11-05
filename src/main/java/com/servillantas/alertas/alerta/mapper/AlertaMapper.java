package com.servillantas.alertas.alerta.mapper;
import com.servillantas.alertas.alerta.dto.response.AlertaResponse;
import com.servillantas.alertas.alerta.entity.Alerta;
import org.springframework.stereotype.Component;
@Component public class AlertaMapper {
    public AlertaResponse toResponse(Alerta v) { return new AlertaResponse(v.id, v.productoId, v.tipo, v.estado,
            v.mensaje, v.createdAt, v.updatedAt, v.resueltaAt); }
}
