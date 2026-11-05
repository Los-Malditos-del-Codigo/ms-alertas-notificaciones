package com.servillantas.alertas.notificacion.service.impl;
import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import com.servillantas.alertas.notificacion.entity.Notificacion;
import com.servillantas.alertas.notificacion.mapper.NotificacionMapper;
import com.servillantas.alertas.notificacion.repository.NotificacionRepository;
import com.servillantas.alertas.notificacion.service.NotificacionService;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service public class NotificacionServiceImpl implements NotificacionService {
    private final NotificacionRepository repository; private final NotificacionMapper mapper;
    public NotificacionServiceImpl(NotificacionRepository repository, NotificacionMapper mapper) {
        this.repository = repository; this.mapper = mapper;
    }
    @Override @Transactional(readOnly = true)
    public Page<NotificacionResponse> listar(UUID usuarioId, Boolean leida, Pageable pageable) {
        Pageable segura = PageRequest.of(Math.max(pageable.getPageNumber(), 0),
                Math.min(Math.max(pageable.getPageSize(), 1), 100), pageable.getSort());
        Page<Notificacion> values = leida == null ? repository.findByUsuarioId(usuarioId, segura)
                : repository.findByUsuarioIdAndLeida(usuarioId, leida, segura);
        return values.map(mapper::toResponse);
    }
    @Override @Transactional
    public NotificacionResponse leer(UUID id, UUID usuarioId) {
        Notificacion value = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificación no encontrada"));
        if (!value.usuarioId.equals(usuarioId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La notificación pertenece a otro usuario");
        value.leida = true; value.readAt = OffsetDateTime.now();
        return mapper.toResponse(repository.save(value));
    }
}
