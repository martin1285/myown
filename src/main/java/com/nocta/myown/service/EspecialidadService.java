package com.nocta.myown.service;

import java.time.LocalDateTime;
import java.util.List;

import com.nocta.myown.response.EspecialidadResponse;
import com.nocta.myown.response.EspecialidadSyncResponse;

public interface EspecialidadService {
	
	public List<EspecialidadResponse> trearEspecialidades();
	
	EspecialidadSyncResponse traerEspecialidadesActivas();

    EspecialidadSyncResponse sincronizarEspecialidades(LocalDateTime desde);

    List<EspecialidadResponse> buscarEspecialidades(String texto);

}
