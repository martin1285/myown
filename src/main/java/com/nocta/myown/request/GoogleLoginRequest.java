package com.nocta.myown.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank(message = "El idToken es requerido")
        String idToken
) {}
