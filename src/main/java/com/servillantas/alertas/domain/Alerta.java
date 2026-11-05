package com.servillantas.alertas.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "alertas")
public class Alerta {
    @Id @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @Column(name="producto_id", nullable=false) public UUID productoId;
    @Column(nullable=false, length=40) public String tipo = "STOCK_BAJO";
    @Column(nullable=false, length=20) public String estado = "ACTIVA";
    @Column(nullable=false, length=500) public String mensaje;
    @Column(name="created_at", nullable=false, insertable=false, updatable=false) public OffsetDateTime createdAt;
    @Column(name="updated_at", nullable=false, insertable=false, updatable=false) public OffsetDateTime updatedAt;
    @Column(name="resuelta_at", insertable=false) public OffsetDateTime resueltaAt;
    protected Alerta() {}
    public Alerta(UUID productoId, String mensaje) { this.productoId=productoId; this.mensaje=mensaje; }
}
