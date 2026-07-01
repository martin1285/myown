package com.nocta.myown.service;

import com.nocta.myown.request.LoginRequest;
import com.nocta.myown.request.UsuarioARegistrarRequest;
import com.nocta.myown.response.AuthResponse;

public interface AuthService {
	
	public AuthResponse registrarUsuario(UsuarioARegistrarRequest uarr);
	
	
	public AuthResponse loginUsuario(LoginRequest loginRequest);
	
	public AuthResponse loginConGoogle(String idToken);

}
