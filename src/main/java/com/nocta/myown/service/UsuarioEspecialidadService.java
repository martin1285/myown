package com.nocta.myown.service;

import java.util.List;

import com.nocta.myown.request.GuardarEspecialidadesUsuarioRequest;
import com.nocta.myown.response.EspecialidadResponse;
import com.nocta.myown.response.UsuarioEspecialidadResponse;

public interface UsuarioEspecialidadService {
	
	public UsuarioEspecialidadResponse agregarEspecialidadParaUsuario(Integer usuarioId, Integer especialidadId);
	
	public List<EspecialidadResponse> traerEspecialidadesByUsuario(Integer usuarioId);
	
	public List<EspecialidadResponse> reemplazarEspecialidadesDelUsuario(Integer usuarioId,
	        GuardarEspecialidadesUsuarioRequest request);
	
	public void borrarEspecialidadEnUsuario(Integer usuarioId, Integer especialidadId);
	
	public void borrarTodasLasEspecialidadesDelUsuario(Integer usuarioId);

}
