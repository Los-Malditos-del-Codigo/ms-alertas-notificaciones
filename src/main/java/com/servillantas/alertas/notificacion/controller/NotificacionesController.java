package com.servillantas.alertas.notificacion.controller;

import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import com.servillantas.alertas.notificacion.service.NotificacionService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionesController {
    private final NotificacionService service;
    public NotificacionesController(NotificacionService service) { this.service = service; }

    @GetMapping
    public Page<NotificacionResponse> listar(@RequestHeader("X-User-Id") UUID usuarioId,
            @RequestParam(required = false) Boolean leida,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.listar(usuarioId, leida, pageable);
    }

    @PatchMapping("/{id}/leer")
    public NotificacionResponse leer(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID usuarioId) {
        return service.leer(id, usuarioId);
    }
}
