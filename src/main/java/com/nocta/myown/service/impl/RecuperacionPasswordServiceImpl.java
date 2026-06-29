package com.nocta.myown.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.entity.RecuperacionPassword;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.repository.RecuperacionPasswordRepository;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.OlvidePasswordRequest;
import com.nocta.myown.request.RestablecerPasswordRequest;
import com.nocta.myown.service.EmailService;
import com.nocta.myown.service.RecuperacionPasswordService;

@Service
public class RecuperacionPasswordServiceImpl implements RecuperacionPasswordService {
	 private static final int MINUTOS_EXPIRACION = 15;
	    private static final int MAX_INTENTOS = 5;

	    private final UsuarioRepository usuarioRepository;
	    private final RecuperacionPasswordRepository recuperacionPasswordRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final EmailService emailService;

	    public RecuperacionPasswordServiceImpl(
	            UsuarioRepository usuarioRepository,
	            RecuperacionPasswordRepository recuperacionPasswordRepository,
	            PasswordEncoder passwordEncoder,
	            EmailService emailService
	    ) {
	        this.usuarioRepository = usuarioRepository;
	        this.recuperacionPasswordRepository = recuperacionPasswordRepository;
	        this.passwordEncoder = passwordEncoder;
	        this.emailService = emailService;
	    }

	    @Override
	    @Transactional
	    public void solicitarRecuperacion(OlvidePasswordRequest request) {
	        String email = normalizarEmail(request.email());

	        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

	        // Importante: no avisamos si el email no existe.
	        // Esto evita que alguien pueda probar emails y descubrir usuarios registrados.
	        if (usuarioOpt.isEmpty()) {
	            return;
	        }

	        Usuario usuario = usuarioOpt.get();

	        String codigo = generarCodigo();

	        // Invalidamos códigos anteriores pendientes
	        recuperacionPasswordRepository
	                .findTopByUsuarioAndUtilizadoFalseOrderByCreatedAtDesc(usuario)
	                .ifPresent(recuperacionAnterior -> {
	                    recuperacionAnterior.setUtilizado(true);
	                    recuperacionPasswordRepository.save(recuperacionAnterior);
	                });

	        RecuperacionPassword recuperacion = new RecuperacionPassword();
	        recuperacion.setUsuario(usuario);
	        recuperacion.setCodigoHash(passwordEncoder.encode(codigo));
	        recuperacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACION));
	        recuperacion.setIntentos(0);
	        recuperacion.setUtilizado(false);
	        recuperacion.setCreatedAt(LocalDateTime.now());

	        recuperacionPasswordRepository.save(recuperacion);

	        String nombre = usuario.getNombre() != null ? usuario.getNombre() : "usuario";

	        emailService.enviarCodigoRecuperacion(
	                usuario.getEmail(),
	                nombre,
	                codigo
	        );
	    }

	    @Override
	    @Transactional
	    public void restablecerPassword(RestablecerPasswordRequest request) {
	        String email = normalizarEmail(request.email());

	        Usuario usuario = usuarioRepository.findByEmail(email)
	                .orElseThrow(() -> new IllegalArgumentException("Código inválido o vencido"));

	        RecuperacionPassword recuperacion = recuperacionPasswordRepository
	                .findTopByUsuarioAndUtilizadoFalseOrderByCreatedAtDesc(usuario)
	                .orElseThrow(() -> new IllegalArgumentException("Código inválido o vencido"));

	        if (Boolean.TRUE.equals(recuperacion.getUtilizado())) {
	            throw new IllegalArgumentException("Código inválido o vencido");
	        }

	        if (recuperacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
	            recuperacion.setUtilizado(true);
	            recuperacionPasswordRepository.save(recuperacion);
	            throw new IllegalArgumentException("Código inválido o vencido");
	        }

	        if (recuperacion.getIntentos() >= MAX_INTENTOS) {
	            recuperacion.setUtilizado(true);
	            recuperacionPasswordRepository.save(recuperacion);
	            throw new IllegalArgumentException("Código inválido o vencido");
	        }

	        boolean codigoCorrecto = passwordEncoder.matches(
	                request.codigo(),
	                recuperacion.getCodigoHash()
	        );

	        if (!codigoCorrecto) {
	            recuperacion.setIntentos(recuperacion.getIntentos() + 1);
	            recuperacionPasswordRepository.save(recuperacion);
	            throw new IllegalArgumentException("Código inválido o vencido");
	        }

	        usuario.setPasswordHash(passwordEncoder.encode(request.nuevaPassword()));
	        usuario.setUpdatedAt(LocalDateTime.now());

	        usuarioRepository.save(usuario);

	        recuperacion.setUtilizado(true);
	        recuperacionPasswordRepository.save(recuperacion);
	    }

	    private String generarCodigo() {
	        int numero = ThreadLocalRandom.current().nextInt(100000, 1000000);
	        return String.valueOf(numero);
	    }

	    private String normalizarEmail(String email) {
	        return email.trim().toLowerCase();
	    }
}
