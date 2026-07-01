package com.nocta.myown.response;

public record GoogleUserInfo(
        String googleId,
        String email,
        String nombre,
        String fotoUrl
) {}