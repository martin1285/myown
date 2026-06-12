package com.nocta.myown.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioARegistrarRequest {

	@NotBlank
	private String nombre;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String telefono;
	
	@NotBlank
	@Size(min = 8, max = 50)
	private String password;
}
