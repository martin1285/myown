package com.nocta.myown.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "especialidad")
@Data
public class Especialidad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "especialidad_id")
	private Integer especialidadId;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "activa")
	private Boolean activa;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	
	@PrePersist
	public void prePersist() {
	    LocalDateTime ahora = LocalDateTime.now();

	    if (createdAt == null) {
	        createdAt = ahora;
	    }

	    if (updatedAt == null) {
	        updatedAt = ahora;
	    }

	    if (activa == null) {
	        activa = true;
	    }
	}

	@PreUpdate
	public void preUpdate() {
	    updatedAt = LocalDateTime.now();
	}

}
