package com.nocta.myown.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nocta.myown.entity.EspecialidadPendiente;
import com.nocta.myown.enums.EstadoEspecialidadPendiente;

public interface EspecialidadPendienteRepository extends JpaRepository<EspecialidadPendiente, Integer> {

	boolean existsByNombreNormalizadoAndEstado(String nombreNormalizado, EstadoEspecialidadPendiente estado);

	boolean existsByUsuarioUsuarioIdAndNombreNormalizadoAndEstado(Integer usuarioId, String nombreNormalizado,
			EstadoEspecialidadPendiente estado);

	List<EspecialidadPendiente> findAllByUsuarioUsuarioIdOrderByCreatedAtDesc(Integer usuarioId);

	List<EspecialidadPendiente> findAllByEstadoOrderByCreatedAtAsc(EstadoEspecialidadPendiente estado);
}