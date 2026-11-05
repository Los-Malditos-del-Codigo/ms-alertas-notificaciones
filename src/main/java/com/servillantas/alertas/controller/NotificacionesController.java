package com.servillantas.alertas.controller;

import com.servillantas.alertas.domain.Notificacion;
import com.servillantas.alertas.repository.NotificacionRepository;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/notificaciones")
public class NotificacionesController {
    private final NotificacionRepository repository;
    public NotificacionesController(NotificacionRepository repository){this.repository=repository;}
    @GetMapping public Page<Notificacion> listar(@RequestHeader("X-User-Id") UUID usuarioId,@RequestParam(required=false) Boolean leida,@PageableDefault(sort="createdAt",direction=Sort.Direction.DESC) Pageable p){
        Pageable segura=PageRequest.of(Math.max(p.getPageNumber(),0),Math.min(Math.max(p.getPageSize(),1),100),p.getSort());
        return leida == null ? repository.findByUsuarioId(usuarioId,segura) : repository.findByUsuarioIdAndLeida(usuarioId,leida,segura);
    }
    @PatchMapping("/{id}/leer") @Transactional public Notificacion leer(@PathVariable UUID id,@RequestHeader("X-User-Id") UUID usuarioId){
        Notificacion n=repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Notificación no encontrada"));
        if(!n.usuarioId.equals(usuarioId))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"La notificación pertenece a otro usuario");
        n.leida=true; n.readAt=OffsetDateTime.now(); return repository.save(n);
    }
}
