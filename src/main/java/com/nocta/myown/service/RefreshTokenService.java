package com.nocta.myown.service;

import com.nocta.myown.entity.RefreshToken;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.RefreshTokenRequest;
import com.nocta.myown.response.AuthResponse;

public interface RefreshTokenService {
	 public RefreshToken crearRefreshToken(Usuario usuario);
	 
	 public AuthResponse refrescarToken(RefreshTokenRequest rtr);
	 
	 public void logout(Usuario usuario);
}
