package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarPasswordRequest (
	 @NotBlank(message = "La contraseña actual es requerida")
     String passwordActual,

     @NotBlank(message = "La nueva contraseña es requerida")
     @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
     String passwordNueva,

     @NotBlank(message = "La confirmación es requerida")
     String confirmarPassword
) {}