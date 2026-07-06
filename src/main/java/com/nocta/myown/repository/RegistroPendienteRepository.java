package com.nocta.myown.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nocta.myown.entity.RegistroPendiente;

public interface RegistroPendienteRepository extends JpaRepository<RegistroPendiente, Long> {

    Optional<RegistroPendiente> findTopByEmailAndUtilizadoFalseOrderByCreatedAtDesc(String email);

}