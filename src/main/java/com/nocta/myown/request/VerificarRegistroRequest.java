package com.nocta.myown.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerificarRegistroRequest(
        @NotBlank @Email String email,
        @NotBlank String codigo
) {}