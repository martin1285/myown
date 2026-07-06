package com.nocta.myown.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitarRegistroRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String telefono,
        @NotBlank String password
) {}