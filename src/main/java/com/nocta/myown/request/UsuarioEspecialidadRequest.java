package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;

public record UsuarioEspecialidadRequest(
		@NotBlank Integer usuarioId, 
		@NotBlank Integer especialidadId){

}
