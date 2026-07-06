package com.nocta.myown.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private boolean success;
    private String message;

    private String accessToken;
    private String refreshToken;

    private Integer usuarioId;
    private String nombre;
    private String email;
    private String telefono;
}