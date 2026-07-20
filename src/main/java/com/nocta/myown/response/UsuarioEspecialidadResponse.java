package com.nocta.myown.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor	
public class UsuarioEspecialidadResponse {
	
	private Integer usuarioId;
	private Integer especialidadId;
	private LocalDateTime createdAt;

}
