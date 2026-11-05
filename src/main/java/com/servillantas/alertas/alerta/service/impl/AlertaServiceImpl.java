package com.servillantas.alertas.alerta.service.impl;

import com.servillantas.alertas.alerta.dto.request.*;
import com.servillantas.alertas.alerta.dto.response.AlertaResponse;
import com.servillantas.alertas.alerta.entity.Alerta;
import com.servillantas.alertas.alerta.mapper.AlertaMapper;
import com.servillantas.alertas.alerta.repository.AlertaRepository;
import com.servillantas.alertas.alerta.service.AlertaService;
import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import com.servillantas.alertas.notificacion.entity.Notificacion;
import com.servillantas.alertas.notificacion.mapper.NotificacionMapper;
import com.servillantas.alertas.notificacion.repository.NotificacionRepository;
import com.servillantas.alertas.shared.validation.ReferenceValidator;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlertaServiceImpl implements AlertaService {
    private static final Set<String> ESTADOS = Set.of("ACTIVA", "RESUELTA");
    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "updatedAt", "estado", "tipo");
    private final AlertaRepository alertas;
    private final NotificacionRepository notificaciones;
    private final ReferenceValidator referencias;
    private final AlertaMapper alertaMapper;
    private final NotificacionMapper notificacionMapper;

    public AlertaServiceImpl(AlertaRepository alertas, NotificacionRepository notificaciones,
            ReferenceValidator referencias, AlertaMapper alertaMapper, NotificacionMapper notificacionMapper) {
        this.alertas = alertas; this.notificaciones = notificaciones; this.referencias = referencias;
        this.alertaMapper = alertaMapper; this.notificacionMapper = notificacionMapper;
    }

    @Override @Transactional
    public AlertaResponse crear(CrearAlertaRequest request) {
        referencias.producto(request.productoId());
        Alerta value = alertas.findByProductoIdAndEstado(request.productoId(), "ACTIVA")
                .orElseGet(() -> alertas.save(new Alerta(request.productoId(), request.mensaje().trim())));
        return alertaMapper.toResponse(value);
    }

    @Override @Transactional(readOnly = true)
    public Page<AlertaResponse> listar(String estado, Pageable pageable) {
        validarOrden(pageable, SORT_FIELDS);
        Pageable segura = PageRequest.of(Math.max(pageable.getPageNumber(), 0),
                Math.min(Math.max(pageable.getPageSize(), 1), 100), pageable.getSort());
        String normalizado = estado == null ? null : estado.trim().toUpperCase();
        if (normalizado != null && !ESTADOS.contains(normalizado))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de alerta inválido");
        Page<Alerta> values = normalizado == null ? alertas.findAll(segura) : alertas.findByEstado(normalizado, segura);
        return values.map(alertaMapper::toResponse);
    }

    @Override @Transactional
    public AlertaResponse resolver(UUID id) {
        Alerta value = alertas.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta no encontrada"));
        if ("RESUELTA".equals(value.estado)) return alertaMapper.toResponse(value);
        value.estado = "RESUELTA"; value.resueltaAt = OffsetDateTime.now();
        return alertaMapper.toResponse(alertas.save(value));
    }

    @Override @Transactional
    public NotificacionResponse notificar(UUID alertaId, NotificarAlertaRequest request) {
        Alerta alerta = alertas.findById(alertaId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta no encontrada"));
        if (!"ACTIVA".equals(alerta.estado))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede notificar una alerta resuelta");
        referencias.usuario(request.usuarioId());
        if (notificaciones.existsByAlertaIdAndUsuarioId(alertaId, request.usuarioId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya fue notificado para esta alerta");
        Notificacion value = notificaciones.save(new Notificacion(alertaId, request.usuarioId(), request.mensaje().trim()));
        return notificacionMapper.toResponse(value);
    }

    private void validarOrden(Pageable pageable, Set<String> permitidos) {
        pageable.getSort().forEach(order -> {
            if (!permitidos.contains(order.getProperty()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Campo de ordenamiento inválido: " + order.getProperty());
        });
    }
}
