package com.nocta.myown.response;

import java.time.LocalDateTime;

import com.nocta.myown.enums.EstadoEspecialidadPendiente;

public record AdminEspecialidadPendienteResponse(
        Integer id,
        Integer usuarioId,
        String usuarioNombre,
        String usuarioEmail,
        String nombre,
        String descripcion,
        EstadoEspecialidadPendiente estado,
        String motivoRechazo,
        Integer especialidadId,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt
) {
}