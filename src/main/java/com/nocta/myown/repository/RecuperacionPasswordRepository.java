package com.nocta.myown.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nocta.myown.entity.RecuperacionPassword;
import com.nocta.myown.entity.Usuario;

public interface RecuperacionPasswordRepository extends JpaRepository<RecuperacionPassword, Long> {

	Optional<RecuperacionPassword>
	findTopByUsuarioAndUtilizadoFalseOrderByCreatedAtDesc(Usuario usuario);
	
}