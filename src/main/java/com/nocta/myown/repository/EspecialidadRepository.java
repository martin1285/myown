package com.nocta.myown.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nocta.myown.entity.Especialidad;


public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer>{
	
	List<Especialidad> findByActivaTrueOrderByNombreAsc();

    List<Especialidad> findByActivaTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);

    List<Especialidad> findByUpdatedAtAfterOrderByUpdatedAtAsc(LocalDateTime desde);

	
	long countByEspecialidadIdInAndActivaTrue(List<Integer> especialidadIds);	

}
