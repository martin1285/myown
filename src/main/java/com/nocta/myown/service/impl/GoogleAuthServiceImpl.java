package com.nocta.myown.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nocta.myown.response.GoogleUserInfo;
import com.nocta.myown.service.GoogleAuthService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuthServiceImpl implements GoogleAuthService {
	

    @Value("${google.client-id}")
    private String clientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    private void init() {
        verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

	@Override
	public GoogleUserInfo verificarToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new IllegalArgumentException("Token de Google inválido");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String nombre = (String) payload.get("name");
            String fotoUrl = (String) payload.get("picture");

            return new GoogleUserInfo(googleId, email, nombre, fotoUrl);

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error al verificar token de Google", e);
        }
    }

}
