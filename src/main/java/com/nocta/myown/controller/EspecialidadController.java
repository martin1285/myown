package com.nocta.myown.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nocta.myown.response.EspecialidadResponse;
import com.nocta.myown.response.EspecialidadSyncResponse;
import com.nocta.myown.service.EspecialidadService;

@RestController
@RequestMapping("/especialidad")
public class EspecialidadController {
	
	private final EspecialidadService especialidadService;
	
	public EspecialidadController (EspecialidadService especialidadService) {
		this.especialidadService = especialidadService;
	}
	
	
	
	
	@GetMapping
    public ResponseEntity<EspecialidadSyncResponse> traerEspecialidades() {
        return ResponseEntity.ok(especialidadService.traerEspecialidadesActivas());
    }

    @GetMapping("/sync")
    public ResponseEntity<EspecialidadSyncResponse> sincronizarEspecialidades(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime desde) {
        return ResponseEntity.ok(especialidadService.sincronizarEspecialidades(desde));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EspecialidadResponse>> buscarEspecialidades(
            @RequestParam String texto) {
        return ResponseEntity.ok(especialidadService.buscarEspecialidades(texto));
    }

}
