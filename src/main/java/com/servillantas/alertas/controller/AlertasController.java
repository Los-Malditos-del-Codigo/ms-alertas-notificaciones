package com.servillantas.alertas.controller;

import com.servillantas.alertas.domain.*;
import com.servillantas.alertas.repository.*;
import com.servillantas.alertas.service.ReferenceValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/alertas")
public class AlertasController {
    private final AlertaRepository alertas; private final NotificacionRepository notificaciones; private final ReferenceValidator referencias;
    public AlertasController(AlertaRepository a, NotificacionRepository n,ReferenceValidator r){alertas=a;notificaciones=n;referencias=r;}

    public record CrearAlerta(@NotNull UUID productoId, @NotBlank @Size(max=500) String mensaje) {}
    public record Notificar(@NotNull UUID usuarioId, @NotBlank @Size(max=500) String mensaje) {}

    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Transactional
    public Alerta crear(@Valid @RequestBody CrearAlerta r){
        referencias.producto(r.productoId());
        return alertas.findByProductoIdAndEstado(r.productoId(), "ACTIVA").orElseGet(() -> alertas.save(new Alerta(r.productoId(), r.mensaje())));
    }
    @GetMapping public Page<Alerta> listar(@RequestParam(required=false) String estado, @PageableDefault(sort="createdAt", direction=Sort.Direction.DESC) Pageable p){
        Pageable segura=PageRequest.of(Math.max(p.getPageNumber(),0),Math.min(Math.max(p.getPageSize(),1),100),p.getSort());
        if(estado!=null&&!java.util.Set.of("ACTIVA","RESUELTA").contains(estado.toUpperCase()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Estado de alerta inválido");
        return estado == null ? alertas.findAll(segura) : alertas.findByEstado(estado.toUpperCase(), segura);
    }
    @PatchMapping("/{id}/resolver") @Transactional public Alerta resolver(@PathVariable UUID id){
        Alerta a=alertas.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Alerta no encontrada"));
        a.estado="RESUELTA"; a.resueltaAt=OffsetDateTime.now(); return alertas.save(a);
    }
    @PostMapping("/{id}/notificaciones") @ResponseStatus(HttpStatus.CREATED) @Transactional
    public Notificacion notificar(@PathVariable UUID id, @Valid @RequestBody Notificar r){
        if(!alertas.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Alerta no encontrada");
        referencias.usuario(r.usuarioId());
        if(notificaciones.existsByAlertaIdAndUsuarioId(id,r.usuarioId()))throw new ResponseStatusException(HttpStatus.CONFLICT,"El usuario ya fue notificado para esta alerta");
        return notificaciones.save(new Notificacion(id,r.usuarioId(),r.mensaje()));
    }
}
