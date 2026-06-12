package com.nocta.myown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.LoginRequest;
import com.nocta.myown.request.RefreshTokenRequest;
import com.nocta.myown.request.UsuarioARegistrarRequest;
import com.nocta.myown.response.AuthResponse;
import com.nocta.myown.service.RefreshTokenService;
import com.nocta.myown.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final UsuarioService usuarioService;
	
	private final RefreshTokenService refreshTokenService;
	
	public AuthController(UsuarioService usuarioService, RefreshTokenService refreshTokenService) {
			this.usuarioService = usuarioService;
			this.refreshTokenService = refreshTokenService;
	}
	
	
	@PostMapping("/registro")
	public ResponseEntity<AuthResponse>registrarUsuario(@RequestBody @Valid UsuarioARegistrarRequest usuario){
		AuthResponse ar = usuarioService.registrarUsuario(usuario);
		return ResponseEntity.ok().body(ar);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginUsuario(@RequestBody @Valid LoginRequest login){
		AuthResponse response = usuarioService.loginUsuario(login);
		return ResponseEntity.ok().body(response);
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request){
		AuthResponse response = refreshTokenService.refrescarToken(request);
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout (Authentication authentication){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		refreshTokenService.logout(usuario);
		return ResponseEntity.noContent().build();
	}
	 

}
