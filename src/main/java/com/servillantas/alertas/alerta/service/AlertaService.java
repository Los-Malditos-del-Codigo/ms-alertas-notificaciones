package com.servillantas.alertas.alerta.service;

import com.servillantas.alertas.alerta.dto.request.CrearAlertaRequest;
import com.servillantas.alertas.alerta.dto.request.NotificarAlertaRequest;
import com.servillantas.alertas.alerta.dto.response.AlertaResponse;
import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertaService {
    AlertaResponse crear(CrearAlertaRequest request);
    Page<AlertaResponse> listar(String estado, Pageable pageable);
    AlertaResponse resolver(UUID id);
    NotificacionResponse notificar(UUID alertaId, NotificarAlertaRequest request);
}
