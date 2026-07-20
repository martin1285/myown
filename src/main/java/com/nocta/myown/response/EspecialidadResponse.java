package com.nocta.myown.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EspecialidadResponse {
	
	private Integer especialidadId;
	
	private String nombre;
	
	private String descripcion;
	
	private Boolean activa;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

}
