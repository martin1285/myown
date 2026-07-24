package com.nocta.myown.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.TarjetaConfiguracionRequest;
import com.nocta.myown.response.LogoTarjetaResponse;
import com.nocta.myown.response.TarjetaConfiguracionResponse;
import com.nocta.myown.service.TarjetaConfiguracionService;
import com.nocta.myown.response.LogoTarjetaResponse;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/tarjeta-configuracion")
public class TarjetaConfiguracionController {

    private final TarjetaConfiguracionService service;

    public TarjetaConfiguracionController(TarjetaConfiguracionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<TarjetaConfiguracionResponse> obtener(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        TarjetaConfiguracionResponse response = service.obtenerPorUsuario(usuario.getUsuarioId());

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<TarjetaConfiguracionResponse> guardar(Authentication authentication, 
    		@RequestBody TarjetaConfiguracionRequest request) {
    	Usuario usuario = (Usuario) authentication.getPrincipal();

        TarjetaConfiguracionResponse response = service.guardar(usuario.getUsuarioId(), request);

        return ResponseEntity.ok(response);
    }
    
    @PutMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LogoTarjetaResponse> guardarLogo(Authentication authentication,
            @RequestPart("archivo") MultipartFile archivo ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        LogoTarjetaResponse response = service.guardarLogo(usuario.getUsuarioId(), archivo);

        return ResponseEntity.ok(response);
    }

    
}