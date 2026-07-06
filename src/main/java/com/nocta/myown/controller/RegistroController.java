package com.nocta.myown.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nocta.myown.request.SolicitarRegistroRequest;
import com.nocta.myown.request.VerificarRegistroRequest;
import com.nocta.myown.response.AuthResponse;
import com.nocta.myown.service.RegistroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/registro")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitarRegistro(
            @RequestBody @Valid SolicitarRegistroRequest request
    ) {
        registroService.solicitarRegistro(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verificar")
    public ResponseEntity<AuthResponse> verificarRegistro(
            @RequestBody @Valid VerificarRegistroRequest request
    ) {
        AuthResponse response = registroService.verificarRegistro(request);
        return ResponseEntity.ok(response);
    }
}