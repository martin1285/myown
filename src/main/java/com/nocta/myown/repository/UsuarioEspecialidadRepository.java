package com.nocta.myown.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nocta.myown.entity.Especialidad;
import com.nocta.myown.entity.UsuarioEspecialidad;
import com.nocta.myown.entity.UsuarioEspecialidadId;

public interface UsuarioEspecialidadRepository extends JpaRepository<UsuarioEspecialidad, UsuarioEspecialidadId>{

	List<UsuarioEspecialidad> findByUsuarioId(Integer usuarioId);
	
	@Query("""
	        SELECT e
	        FROM Especialidad e
	        JOIN UsuarioEspecialidad ue 
	            ON ue.especialidadId = e.especialidadId
	        WHERE ue.usuarioId = :usuarioId
	          AND e.activa = true
	        ORDER BY e.nombre ASC
	    """)
	    List<Especialidad> findEspecialidadesByUsuarioId(@Param("usuarioId") Integer usuarioId);
	
	void deleteByUsuarioId(Integer usuarioId);
	
	Optional<UsuarioEspecialidad> findById(UsuarioEspecialidadId id);
	
	boolean existsByUsuarioIdAndEspecialidadId(Integer usuarioId, Integer especialidadId);
}
