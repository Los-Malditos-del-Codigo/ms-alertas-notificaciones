package com.servillantas.alertas.alerta.dto.request;
import jakarta.validation.constraints.*;
import java.util.UUID;
public record NotificarAlertaRequest(@NotNull UUID usuarioId, @NotBlank @Size(max = 500) String mensaje) {}
