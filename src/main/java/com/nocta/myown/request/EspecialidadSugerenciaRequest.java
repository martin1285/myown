package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EspecialidadSugerenciaRequest(

        @NotBlank(message = "El nombre de la especialidad es obligatorio")
        @Size(
                min = 3,
                max = 100,
                message = "El nombre debe tener entre 3 y 100 caracteres"
        )
        String nombre,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(
                min = 10,
                max = 500,
                message = "La descripción debe tener entre 10 y 500 caracteres"
        )
        String descripcion

) {
}