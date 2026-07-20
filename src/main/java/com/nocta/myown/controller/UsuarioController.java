package com.nocta.myown.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.ActualizarPerfilRequest;
import com.nocta.myown.request.CambiarPasswordRequest;
import com.nocta.myown.request.EliminarCuentaRequest;
import com.nocta.myown.response.UsuarioResponse;
import com.nocta.myown.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
	private final UsuarioService usuarioService;
	
	public UsuarioController (UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}


    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> perfil(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(new UsuarioResponse(usuario));
    }
    
    @PutMapping("/actualizarPerfil")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(Authentication authentication, 
    		@RequestBody @Valid ActualizarPerfilRequest request){
    	Usuario usuario = (Usuario)  authentication.getPrincipal();
    	UsuarioResponse response = usuarioService.actualizarUsuario(usuario,request);
    	return ResponseEntity.ok(response);
    }
    
    @PutMapping("/password")
    public ResponseEntity<Void> cambiarPassword(Authentication authentication,
    		@RequestBody @Valid CambiarPasswordRequest request){
    	Usuario usuario = (Usuario) authentication.getPrincipal();
    	usuarioService.cambiarPassword(usuario, request);
    	return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/eliminarUsuario")
    public ResponseEntity<Void> eliminarUsuario(Authentication authentication,
    		@Valid @RequestBody EliminarCuentaRequest request){
    	Usuario usuario = (Usuario) authentication.getPrincipal();
    	usuarioService.eliminarCuenta(usuario, request.password());
    	return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/foto")
    public ResponseEntity<UsuarioResponse> actualizarFoto(
            Authentication authentication,
            @RequestParam("foto") MultipartFile foto) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        log.info(
                "Request actualizar foto. usuarioId={}, nombreArchivo={}, contentType={}, size={}",
                usuario.getUsuarioId(),
                foto != null ? foto.getOriginalFilename() : null,
                foto != null ? foto.getContentType() : null,
                foto != null ? foto.getSize() : null
        );
        
        UsuarioResponse response = usuarioService.actualizarFotoPerfil(usuario, foto);
        return ResponseEntity.ok(response);
    }
}
    	
    	
