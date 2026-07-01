package com.nocta.myown.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.entity.RefreshToken;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.LoginRequest;
import com.nocta.myown.request.UsuarioARegistrarRequest;
import com.nocta.myown.response.AuthResponse;
import com.nocta.myown.response.GoogleUserInfo;
import com.nocta.myown.service.AuthService;
import com.nocta.myown.service.GoogleAuthService;
import com.nocta.myown.service.JwtService;
import com.nocta.myown.service.RefreshTokenService;




@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private GoogleAuthService googleAuthService;

	@Override
	@Transactional
	public AuthResponse registrarUsuario(UsuarioARegistrarRequest request) {

	    if (usuarioRepository.existsByEmail(request.getEmail())) {
	        return new AuthResponse(
	                false,
	                "El email ya está registrado",
	                null,
	                null,
	                null,
	                null,
	                null
	        );
	    }

	    Usuario usuario = new Usuario();
	    usuario.setNombre(request.getNombre());
	    usuario.setEmail(request.getEmail());
	    usuario.setTelefono(request.getTelefono());
	    usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
	    usuario.setActivo(true);
	    usuario.setPerfilCompleto(false);
	    usuario.setPlan("FREE");
	    usuario.setSuscripcionActiva(false);
	    usuario.setFechaAlta(LocalDateTime.now());
	    usuario.setUpdatedAt(LocalDateTime.now());
	    usuario.setProveedorAuth("EMAIL");

	    Usuario usuarioGuardado = usuarioRepository.save(usuario);

	    String accessToken = jwtService.generarToken(usuarioGuardado);

	    RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuarioGuardado);

	    return new AuthResponse(
	            true,
	            "Usuario registrado correctamente",
	            accessToken,
	            refreshToken.getToken(),
	            usuarioGuardado.getUsuarioId(),
	            usuarioGuardado.getNombre(),
	            usuarioGuardado.getEmail()
	    );
	}

	@Override
	public AuthResponse loginUsuario(LoginRequest loginRequest) {
		Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectos"));
		
		if(!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPasswordHash())) {
			throw new IllegalArgumentException("Email o contraseña incorrectos");
		}
		
		String token = jwtService.generarToken(usuario);
		
		RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario);
		return new AuthResponse(
				true,
				"login correcto",
				token,
				refreshToken.getToken(),
				usuario.getUsuarioId(),
				usuario.getNombre(),
				usuario.getEmail());
	}
	
	@Override
	@Transactional
	public AuthResponse loginConGoogle(String idToken) {
	    GoogleUserInfo googleUser = googleAuthService.verificarToken(idToken);

	    Usuario usuario = usuarioRepository.findByGoogleId(googleUser.googleId())
	            .or(() -> usuarioRepository.findByEmail(googleUser.email()))
	            .orElseGet(() -> crearUsuarioDesdeGoogle(googleUser));

	    if (usuario.getGoogleId() == null) {
	        usuario.setGoogleId(googleUser.googleId());
	        usuario.setProveedorAuth("GOOGLE");
	    }

	    if (Boolean.FALSE.equals(usuario.getActivo())) {
	        throw new IllegalArgumentException("La cuenta está desactivada");
	    }

	    usuario.setUltimoLogin(LocalDateTime.now());
	    usuario = usuarioRepository.save(usuario);

	    String accessToken = jwtService.generarToken(usuario);
	    RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario);

	    return new AuthResponse(
	            true,
	            "Login exitoso",
	            accessToken,
	            refreshToken.getToken(),
	            usuario.getUsuarioId(),
	            usuario.getNombre(),
	            usuario.getEmail());
	}

	private Usuario crearUsuarioDesdeGoogle(GoogleUserInfo googleUser) {
	    Usuario nuevo = new Usuario();
	    nuevo.setNombre(googleUser.nombre());
	    nuevo.setEmail(googleUser.email());
	    nuevo.setGoogleId(googleUser.googleId());
	    nuevo.setFotoUrl(googleUser.fotoUrl());
	    nuevo.setPasswordHash(null);
	    nuevo.setProveedorAuth("GOOGLE");
	    nuevo.setActivo(true);
	    nuevo.setPerfilCompleto(false);
	    nuevo.setFechaAlta(LocalDateTime.now());

	    return usuarioRepository.save(nuevo);
	}

}
