package com.nocta.myown.service;

import org.springframework.web.multipart.MultipartFile;

import com.nocta.myown.request.TarjetaConfiguracionRequest;
import com.nocta.myown.response.LogoTarjetaResponse;
import com.nocta.myown.response.TarjetaConfiguracionResponse;

public interface TarjetaConfiguracionService {
	
	public TarjetaConfiguracionResponse obtenerPorUsuario(Integer usuarioId);
	
	public TarjetaConfiguracionResponse guardar(Integer usuarioId, TarjetaConfiguracionRequest request);
	
	public LogoTarjetaResponse guardarLogo(Integer usuarioId, MultipartFile archivo); 

}
