package com.servillantas.alertas.alerta.dto.request;
import jakarta.validation.constraints.*;
import java.util.UUID;
public record CrearAlertaRequest(@NotNull UUID productoId, @NotBlank @Size(max = 500) String mensaje) {}
