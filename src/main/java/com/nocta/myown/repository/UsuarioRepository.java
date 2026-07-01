package com.nocta.myown.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nocta.myown.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
	

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByGoogleId(String googleId);

}
