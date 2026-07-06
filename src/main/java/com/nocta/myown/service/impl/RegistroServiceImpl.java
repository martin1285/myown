package com.nocta.myown.service.impl;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.entity.RefreshToken;
import com.nocta.myown.entity.RegistroPendiente;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.repository.RegistroPendienteRepository;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.SolicitarRegistroRequest;
import com.nocta.myown.request.VerificarRegistroRequest;
import com.nocta.myown.response.AuthResponse;
import com.nocta.myown.service.EmailService;
import com.nocta.myown.service.JwtService;
import com.nocta.myown.service.RefreshTokenService;
import com.nocta.myown.service.RegistroService;

@Service
public class RegistroServiceImpl implements RegistroService {

    private static final int MINUTOS_EXPIRACION = 15;
    private static final int MAX_INTENTOS = 5;

    private final UsuarioRepository usuarioRepository;
    private final RegistroPendienteRepository registroPendienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public RegistroServiceImpl(
            UsuarioRepository usuarioRepository,
            RegistroPendienteRepository registroPendienteRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.registroPendienteRepository = registroPendienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Transactional
    public void solicitarRegistro(SolicitarRegistroRequest request) {
        String email = normalizarEmail(request.email());

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese email");
        }

        registroPendienteRepository
                .findTopByEmailAndUtilizadoFalseOrderByCreatedAtDesc(email)
                .ifPresent(anterior -> {
                    anterior.setUtilizado(true);
                    registroPendienteRepository.save(anterior);
                });

        String codigo = generarCodigo();

        RegistroPendiente pendiente = new RegistroPendiente();
        pendiente.setNombre(request.nombre().trim());
        pendiente.setEmail(email);
        pendiente.setTelefono(request.telefono());
        pendiente.setPasswordHash(passwordEncoder.encode(request.password()));
        pendiente.setCodigoHash(passwordEncoder.encode(codigo));
        pendiente.setFechaExpiracion(LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACION));
        pendiente.setIntentos(0);
        pendiente.setUtilizado(false);
        pendiente.setCreatedAt(LocalDateTime.now());

        registroPendienteRepository.save(pendiente);

        emailService.enviarCodigoRecuperacion(
                email,
                pendiente.getNombre(),
                codigo
        );
    }

    @Override
    @Transactional
    public AuthResponse verificarRegistro(VerificarRegistroRequest request) {
        String email = normalizarEmail(request.email());

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese email");
        }

        RegistroPendiente pendiente = registroPendienteRepository
                .findTopByEmailAndUtilizadoFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("Código inválido o vencido"));

        if (pendiente.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            pendiente.setUtilizado(true);
            registroPendienteRepository.save(pendiente);
            throw new IllegalArgumentException("Código inválido o vencido");
        }

        if (pendiente.getIntentos() >= MAX_INTENTOS) {
            pendiente.setUtilizado(true);
            registroPendienteRepository.save(pendiente);
            throw new IllegalArgumentException("Código inválido o vencido");
        }

        boolean codigoCorrecto = passwordEncoder.matches(
                request.codigo(),
                pendiente.getCodigoHash()
        );

        if (!codigoCorrecto) {
            pendiente.setIntentos(pendiente.getIntentos() + 1);
            registroPendienteRepository.save(pendiente);
            throw new IllegalArgumentException("Código inválido o vencido");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(pendiente.getNombre());
        usuario.setEmail(pendiente.getEmail());
        usuario.setTelefono(pendiente.getTelefono());
        usuario.setPasswordHash(pendiente.getPasswordHash());

        usuario.setProveedorAuth("EMAIL");
        usuario.setActivo(true);
        usuario.setPerfilCompleto(false);
        usuario.setPlan("FREE");
        usuario.setSuscripcionActiva(false);

        LocalDateTime ahora = LocalDateTime.now();
        usuario.setFechaAlta(ahora);
        usuario.setUltimoLogin(ahora);
        usuario.setUpdatedAt(ahora);

        usuario = usuarioRepository.save(usuario);

        pendiente.setUtilizado(true);
        registroPendienteRepository.save(pendiente);

        String accessToken = jwtService.generarToken(usuario);
        RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario);

        return new AuthResponse(
                true,
                "Registro verificado correctamente",
                accessToken,
                refreshToken.getToken(),
                usuario.getUsuarioId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }

    private String generarCodigo() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }
}