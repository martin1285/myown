package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RechazarEspecialidadPendienteRequest(

        @NotBlank(message = "El motivo de rechazo es obligatorio")
        @Size(
                min = 5,
                max = 500,
                message = "El motivo debe tener entre 5 y 500 caracteres"
        )
        String motivo

) {
}