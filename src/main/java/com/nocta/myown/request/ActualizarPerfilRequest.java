package com.nocta.myown.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ActualizarPerfilRequest(
        @Pattern(regexp = "^[0-9+\\s\\-]{7,20}$", message = "Formato de teléfono inválido")
        String telefono,

        @Size(max = 150, message = "Localidad demasiado larga")
        String localidad,
        
        @Pattern(regexp = "^[0-9\\-]{8,20}$", message = "Formato de CUIT/CUIL inválido")
        String cuilCuit,

        @Size(max = 100, message = "Matrícula demasiado larga")
        String matricula,

        @Size(max = 500, message = "Descripción demasiado larga")
        String descripcion,

        @Size(max = 150, message = "Nombre comercial demasiado largo")
        String nombreComercial
){}