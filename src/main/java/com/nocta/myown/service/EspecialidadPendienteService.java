package com.nocta.myown.service;

import java.util.List;

import com.nocta.myown.enums.EstadoEspecialidadPendiente;
import com.nocta.myown.request.EspecialidadSugerenciaRequest;
import com.nocta.myown.request.RechazarEspecialidadPendienteRequest;
import com.nocta.myown.response.AdminEspecialidadPendienteResponse;
import com.nocta.myown.response.EspecialidadPendienteResponse;

public interface EspecialidadPendienteService {
	
	EspecialidadPendienteResponse crearSugerencia(Integer usuarioId, EspecialidadSugerenciaRequest request);
	 
	 List<EspecialidadPendienteResponse> obtenerMisSugerencias(Integer usuarioId);
	 
	 List<AdminEspecialidadPendienteResponse> obtenerSugerenciasAdmin(EstadoEspecialidadPendiente estado);
	 
	 EspecialidadPendienteResponse aprobarSugerencia(Integer sugerenciaId, Integer administradorId);
	 
	 EspecialidadPendienteResponse rechazarSugerencia(Integer sugerenciaId, Integer administradorId,
		        RechazarEspecialidadPendienteRequest request);

}
