package com.nocta.myown.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.GuardarEspecialidadesUsuarioRequest;
import com.nocta.myown.response.EspecialidadResponse;
import com.nocta.myown.service.UsuarioEspecialidadService;

@RestController
@RequestMapping("/usuario/especialidades")
public class UsuarioEspecialidadController {
	
	private final UsuarioEspecialidadService usuarioEspecialidadService;
	
	public UsuarioEspecialidadController(UsuarioEspecialidadService usuarioEspecialidadService) {
		this.usuarioEspecialidadService = usuarioEspecialidadService;
	}
	
	@GetMapping()
	public ResponseEntity<List<EspecialidadResponse>> traerEspecialidadesPorUsuario(Authentication authentication){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		List<EspecialidadResponse> response = usuarioEspecialidadService.traerEspecialidadesByUsuario(usuario.getUsuarioId());
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping
	public ResponseEntity<List<EspecialidadResponse>> reemplazarEspecialidades(Authentication authentication,
	        @RequestBody GuardarEspecialidadesUsuarioRequest request) {
		
		Usuario usuario = (Usuario) authentication.getPrincipal();
	    return ResponseEntity.ok(usuarioEspecialidadService.reemplazarEspecialidadesDelUsuario(usuario.getUsuarioId(),request));
	}

}
