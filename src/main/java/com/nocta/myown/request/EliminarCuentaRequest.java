package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;

public record EliminarCuentaRequest (
		@NotBlank(message = "debe ingresar la contraseña")
		String password
){}
