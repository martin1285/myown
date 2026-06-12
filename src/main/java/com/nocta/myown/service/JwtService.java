package com.nocta.myown.service;

import com.nocta.myown.entity.Usuario;

public interface JwtService {

    String generarToken(Usuario usuario);
    
    String extraerUsuarioId(String token);

    boolean esTokenValido(String token);
}