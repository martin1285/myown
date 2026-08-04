package com.nocta.myown.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.enums.EstadoEspecialidadPendiente;
import com.nocta.myown.request.RechazarEspecialidadPendienteRequest;
import com.nocta.myown.response.AdminEspecialidadPendienteResponse;
import com.nocta.myown.response.EspecialidadPendienteResponse;
import com.nocta.myown.service.EspecialidadPendienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/especialidades/sugerencias")
public class AdminEspecialidadPendienteController {

    private final EspecialidadPendienteService service;

    public AdminEspecialidadPendienteController(EspecialidadPendienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AdminEspecialidadPendienteResponse>>
    obtenerSugerencias(@RequestParam(required = false)EstadoEspecialidadPendiente estado) {
    	
        List<AdminEspecialidadPendienteResponse> response = service.obtenerSugerenciasAdmin(estado);

        return ResponseEntity.ok(response);
    }
    
    
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<EspecialidadPendienteResponse>
    aprobarSugerencia(@PathVariable Integer id, Authentication authentication) {
        
    	Usuario administrador = (Usuario) authentication.getPrincipal();

        EspecialidadPendienteResponse response = service.aprobarSugerencia(id, administrador.getUsuarioId());

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<EspecialidadPendienteResponse>
    rechazarSugerencia(@PathVariable Integer id, Authentication authentication, 
    		@Valid @RequestBody RechazarEspecialidadPendienteRequest request) {
        
    	Usuario administrador = (Usuario) authentication.getPrincipal();

        EspecialidadPendienteResponse response = service.rechazarSugerencia(id, administrador.getUsuarioId(),
                        request);

        return ResponseEntity.ok(response);
    }
}