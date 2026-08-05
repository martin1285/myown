package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CambiarPasswordRequest (
	 @NotBlank(message = "La contraseña actual es requerida")
     String passwordActual,

     @Pattern(
             regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
             message = "La contraseña debe contener al menos una mayúscula y un número"
         )
	 String passwordNueva,

     @NotBlank(message = "La confirmación es requerida")
     String confirmarPassword
) {}