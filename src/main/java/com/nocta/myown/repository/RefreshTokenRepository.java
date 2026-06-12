package com.nocta.myown.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nocta.myown.entity.RefreshToken;
import com.nocta.myown.entity.Usuario;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
    
    List<RefreshToken> findActivosByUsuario(Usuario usuario);
    
    void deleteByUsuario(Usuario usuario);
    
    void deleteByFechaExpiracionBefore(LocalDateTime fecha);
    
    List<RefreshToken>  findByUsuarioAndRevocadoFalse(Usuario usuario);

}