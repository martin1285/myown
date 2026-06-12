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
public class LoginRequest {
	
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "el email no tiene un formato válido")
	private String email;
	
	@NotBlank(message = "la contraseña es obligatoria")
	@Size(min = 8, max = 50, message = "la contraseña debe tener un mínimo de 8 caracteres")
	private String password;

}
