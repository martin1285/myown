package com.nocta.myown.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.EspecialidadSugerenciaRequest;
import com.nocta.myown.response.EspecialidadPendienteResponse;
import com.nocta.myown.service.EspecialidadPendienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/especialidades/sugerencias")
public class EspecialidadPendienteController {

    private final EspecialidadPendienteService service;

    public EspecialidadPendienteController(EspecialidadPendienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EspecialidadPendienteResponse> crearSugerencia(Authentication authentication,
            @Valid @RequestBody EspecialidadSugerenciaRequest request ) {
    	
        Usuario usuario = (Usuario) authentication.getPrincipal();

        EspecialidadPendienteResponse response = service.crearSugerencia(usuario.getUsuarioId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/mis-sugerencias")
    public ResponseEntity<List<EspecialidadPendienteResponse>>
    obtenerMisSugerencias(Authentication authentication) {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        List<EspecialidadPendienteResponse> response = service.obtenerMisSugerencias(usuario.getUsuarioId());

        return ResponseEntity.ok(response);
    }
}