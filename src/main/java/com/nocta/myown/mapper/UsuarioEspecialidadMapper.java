package com.nocta.myown.mapper;

import com.nocta.myown.entity.UsuarioEspecialidad;
import com.nocta.myown.response.UsuarioEspecialidadResponse;

public class UsuarioEspecialidadMapper {
	
	public static UsuarioEspecialidadResponse toResponse(UsuarioEspecialidad ue) {
		return new UsuarioEspecialidadResponse(
				ue.getUsuarioId(),
				ue.getEspecialidadId(),
				ue.getCreatedAt());
	}

}
