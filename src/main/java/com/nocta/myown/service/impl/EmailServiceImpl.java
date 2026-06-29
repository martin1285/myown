package com.nocta.myown.service.impl;

import org.springframework.stereotype.Service;

import com.nocta.myown.service.EmailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

@Service
public class EmailServiceImpl implements EmailService {
	
	 private final Resend resend;

	    public EmailServiceImpl(Resend resend) {
	        this.resend = resend;
	    }

	@Override
	public void enviarCodigoRecuperacion(String email, String nombre, String codigo) {
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from("My Own <no-reply@noctasf.online>")
                .to(email)
                .subject("Código para recuperar tu contraseña")
                .html("""
                    <h2>Recuperación de contraseña</h2>
                    <p>Hola, %s.</p>
                    <p>Tu código para restablecer la contraseña es:</p>
                    <h1 style="letter-spacing:5px">%s</h1>
                    <p>Este código vence en 15 minutos.</p>
                    <p>Si no solicitaste el cambio, ignorá este mensaje.</p>
                    """.formatted(nombre, codigo))
                .build();

        try {
            resend.emails().send(emailOptions);
        } catch (ResendException e) {
            throw new IllegalStateException("No se pudo enviar el correo", e);
        }
    }

}
