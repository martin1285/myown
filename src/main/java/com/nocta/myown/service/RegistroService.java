package com.nocta.myown.service;

import com.nocta.myown.request.SolicitarRegistroRequest;
import com.nocta.myown.request.VerificarRegistroRequest;
import com.nocta.myown.response.AuthResponse;

public interface RegistroService {

	public void solicitarRegistro(SolicitarRegistroRequest request);
	
	public AuthResponse verificarRegistro(VerificarRegistroRequest request);
	
}
