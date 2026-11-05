package com.servillantas.alertas.notificacion.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "notificaciones")
public class Notificacion {
    @Id @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @Column(name="alerta_id", nullable=false) public UUID alertaId;
    @Column(name="usuario_id", nullable=false) public UUID usuarioId;
    @Column(nullable=false, length=500) public String mensaje;
    @Column(nullable=false) public boolean leida;
    @Column(name="read_at", insertable=false) public OffsetDateTime readAt;
    @Column(name="created_at", nullable=false, insertable=false, updatable=false) public OffsetDateTime createdAt;
    protected Notificacion() {}
    public Notificacion(UUID alertaId, UUID usuarioId, String mensaje) { this.alertaId=alertaId; this.usuarioId=usuarioId; this.mensaje=mensaje; }
}
