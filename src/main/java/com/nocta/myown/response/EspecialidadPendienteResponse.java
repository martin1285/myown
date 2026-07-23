package com.nocta.myown.response;

import java.time.LocalDateTime;

import com.nocta.myown.enums.EstadoEspecialidadPendiente;

public record EspecialidadPendienteResponse(
        Integer id,
        String nombre,
        String descripcion,
        EstadoEspecialidadPendiente estado,
        String motivoRechazo,
        Integer especialidadId,
        LocalDateTime createdAt
) {
}