package com.nocta.myown.mapper;

import com.nocta.myown.entity.Especialidad;
import com.nocta.myown.response.EspecialidadResponse;

public class EspecialidadMapper {
	
	public static EspecialidadResponse toResponse(Especialidad especialidad) {
		return new EspecialidadResponse(
				especialidad.getEspecialidadId(),
				especialidad.getNombre(),
				especialidad.getDescripcion(),
				especialidad.getActiva(),
				especialidad.getCreatedAt(),
				especialidad.getUpdatedAt());
	}

}
