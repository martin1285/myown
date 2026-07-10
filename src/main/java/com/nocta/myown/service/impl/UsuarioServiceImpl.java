package com.nocta.myown.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.ActualizarPerfilRequest;
import com.nocta.myown.request.CambiarPasswordRequest;
import com.nocta.myown.response.UsuarioResponse;
import com.nocta.myown.service.ImagenService;
import com.nocta.myown.service.RefreshTokenService;
import com.nocta.myown.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
	
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	private final ImagenService imagenService;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder
			,RefreshTokenService refreshTokenService,ImagenService imagenService) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
		this.imagenService = imagenService;
	}

	@Override
	@Transactional
	public UsuarioResponse actualizarUsuario(Usuario usuario, ActualizarPerfilRequest request) {
		if (request.nombre() != null) usuario.setNombre(request.nombre());
		if (request.telefono() != null) usuario.setTelefono(request.telefono());
		if (request.descripcion() != null) usuario.setDescripcion(request.descripcion());
		if (request.localidad() != null) usuario.setLocalidad(request.localidad());
		if (request.cuilCuit() != null) usuario.setCuilCuit(request.cuilCuit());
		if (request.matricula() != null) usuario.setMatricula(request.matricula());
		if (request.nombreComercial() != null) usuario.setNombreComercial(request.nombreComercial());
		
		usuario.setUpdatedAt(LocalDateTime.now());
		Usuario actualizado = usuarioRepository.save(usuario);

		return new UsuarioResponse(actualizado);
	}

	@Override
	public void cambiarPassword(Usuario usuario, CambiarPasswordRequest request) {

		if(!request.confirmarPassword().equals(request.passwordNueva())) {
			throw new IllegalArgumentException("las contraseñas no coinciden.");
		}
		if (!passwordEncoder.matches(request.passwordActual(), usuario.getPasswordHash())) {
			throw new IllegalArgumentException("La contraseña actual es incorrecta");
		}
		usuario.setPasswordHash(passwordEncoder.encode(request.passwordNueva()));
		usuario.setUpdatedAt(LocalDateTime.now());
		
		usuarioRepository.save(usuario);
		
	}

	@Override
	@Transactional
	public void eliminarCuenta(Usuario usuario, String passwordActual) {
		if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
			throw new IllegalArgumentException("La contraseña es incorrecta");
		}
		
		usuario.setActivo(false);
		usuario.setUpdatedAt(LocalDateTime.now());
		usuarioRepository.save(usuario);
		
		refreshTokenService.logout(usuario);
	}
	
	 @Override
	    public UsuarioResponse actualizarFotoPerfil(Usuario usuario, MultipartFile foto) {
		 log.info("Inicio actualización foto perfil. usuarioId={}", usuario.getUsuarioId());
	        String url = imagenService.subirFotoPerfil(foto, usuario.getUsuarioId());

	        log.info("URL recibida desde ImagenService. usuarioId={}, url={}", usuario.getUsuarioId(), url);
	        
	        usuario.setFotoUrl(url);
	        usuario.setUpdatedAt(LocalDateTime.now());

	        Usuario actualizado = usuarioRepository.save(usuario);
	        return new UsuarioResponse(actualizado);
	    }
	

}
