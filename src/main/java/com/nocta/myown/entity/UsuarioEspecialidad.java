package com.nocta.myown.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario_especialidad")
@IdClass(UsuarioEspecialidadId.class)
public class UsuarioEspecialidad {
	
	@Id
	@Column(name = "usuario_id")
	private Integer usuarioId;
	
	@Id
	@Column(name = "especialidad_id")
	private Integer especialidadId;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
