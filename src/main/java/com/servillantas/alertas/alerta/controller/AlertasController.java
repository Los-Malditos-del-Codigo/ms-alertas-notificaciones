package com.servillantas.alertas.alerta.controller;

import com.servillantas.alertas.alerta.dto.request.CrearAlertaRequest;
import com.servillantas.alertas.alerta.dto.request.NotificarAlertaRequest;
import com.servillantas.alertas.alerta.dto.response.AlertaResponse;
import com.servillantas.alertas.alerta.service.AlertaService;
import com.servillantas.alertas.notificacion.dto.response.NotificacionResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertasController {
    private final AlertaService service;
    public AlertasController(AlertaService service) { this.service = service; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public AlertaResponse crear(@Valid @RequestBody CrearAlertaRequest request) { return service.crear(request); }

    @GetMapping
    public Page<AlertaResponse> listar(@RequestParam(required = false) String estado,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.listar(estado, pageable);
    }

    @PatchMapping("/{id}/resolver")
    public AlertaResponse resolver(@PathVariable UUID id) { return service.resolver(id); }

    @PostMapping("/{id}/notificaciones") @ResponseStatus(HttpStatus.CREATED)
    public NotificacionResponse notificar(@PathVariable UUID id, @Valid @RequestBody NotificarAlertaRequest request) {
        return service.notificar(id, request);
    }
}
