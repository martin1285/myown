package com.nocta.myown.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.entity.RefreshToken;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.repository.RefreshTokenRepository;
import com.nocta.myown.request.RefreshTokenRequest;
import com.nocta.myown.response.AuthResponse;
import com.nocta.myown.service.JwtService;
import com.nocta.myown.service.RefreshTokenService;



@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
	
	
    private final RefreshTokenRepository refreshTokenRepository;
	
	
	private final JwtService jwtService;
	
	public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,JwtService jwtService) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtService = jwtService;
	}
	

	@Override
	public RefreshToken crearRefreshToken(Usuario usuario) {
		
		refreshTokenRepository.findActivosByUsuario(usuario)
        .forEach(t -> {
            t.setRevocado(true);
            refreshTokenRepository.save(t);
        });

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFechaExpiracion(LocalDateTime.now().plusDays(30));
        refreshToken.setRevocado(false);
        refreshToken.setCreatedAt(LocalDateTime.now());

        return refreshTokenRepository.save(refreshToken);
    }

	@Override
	@Transactional
	public AuthResponse refrescarToken(RefreshTokenRequest rtr) {
		RefreshToken rt = refreshTokenRepository.findByToken(rtr.getRefreshToken())
				.orElseThrow(() -> new IllegalArgumentException("Refresh token invalido"));
		
		if (rt.getRevocado()) {
			throw new IllegalArgumentException("El refresh token se encuentra revocado");
		}
		
		if (rt.getFechaExpiracion().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("El resfresh token se encuentra expirado");
		}
		
		rt.setRevocado(true);
		refreshTokenRepository.save(rt);
		
		Usuario usuario = rt.getUsuario();
		
		String nuevoAccessToken = jwtService.generarToken(usuario);
		
		RefreshToken nuevoRefreshToken = crearRefreshToken(usuario);
		
		return new AuthResponse(
				true,
				"token renovado correctamente",
				nuevoAccessToken,
				nuevoRefreshToken.getToken(),
				usuario.getUsuarioId(),
				usuario.getNombre(),
				usuario.getEmail(),
				usuario.getTelefono());
	}


	@Override
	@Transactional
	public void logout(Usuario usuario) {
	    refreshTokenRepository.findByUsuarioAndRevocadoFalse(usuario)
	            .forEach(t -> {
	                t.setRevocado(true);
	                refreshTokenRepository.save(t);
	            });
	}
	
	
	
	
}
