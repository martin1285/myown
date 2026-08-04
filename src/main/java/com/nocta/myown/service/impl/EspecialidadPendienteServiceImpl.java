package com.nocta.myown.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.entity.Especialidad;
import com.nocta.myown.entity.EspecialidadPendiente;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.enums.EstadoEspecialidadPendiente;
import com.nocta.myown.enums.RolUsuario;
import com.nocta.myown.exception.OperacionInvalidaException;
import com.nocta.myown.exception.RecursoDuplicadoException;
import com.nocta.myown.exception.RecursoNoEncontradoException;
import com.nocta.myown.repository.EspecialidadPendienteRepository;
import com.nocta.myown.repository.EspecialidadRepository;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.EspecialidadSugerenciaRequest;
import com.nocta.myown.request.RechazarEspecialidadPendienteRequest;
import com.nocta.myown.response.AdminEspecialidadPendienteResponse;
import com.nocta.myown.response.EspecialidadPendienteResponse;
import com.nocta.myown.service.EspecialidadPendienteService;

import utils.TextoUtils;

@Service
public class EspecialidadPendienteServiceImpl implements EspecialidadPendienteService {

    private final EspecialidadPendienteRepository especialidadPendienteRepository;
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository;

    public EspecialidadPendienteServiceImpl(EspecialidadPendienteRepository especialidadPendienteRepository,
            EspecialidadRepository especialidadRepository,
            UsuarioRepository usuarioRepository) {
        this.especialidadPendienteRepository = especialidadPendienteRepository;
        this.especialidadRepository = especialidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public EspecialidadPendienteResponse crearSugerencia(Integer usuarioId, EspecialidadSugerenciaRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El usuario no existe")
                );

        String nombre = request.nombre().trim();
        String descripcion = request.descripcion().trim();

        String nombreNormalizado = TextoUtils.normalizarNombre(nombre);

        if (especialidadRepository.existsByNombreIgnoreCase(nombre)) {
            throw new RecursoDuplicadoException("La especialidad ya existe en el listado");
        }

        boolean sugerenciaPendienteExistente =
                especialidadPendienteRepository.existsByNombreNormalizadoAndEstado(
                                nombreNormalizado,
                                EstadoEspecialidadPendiente.PENDIENTE);

        if (sugerenciaPendienteExistente) {
            throw new RecursoDuplicadoException("Esta especialidad ya fue sugerida y está pendiente de revisión");
        }

        EspecialidadPendiente sugerencia = new EspecialidadPendiente();

        sugerencia.setUsuario(usuario);
        sugerencia.setNombre(nombre);
        sugerencia.setNombreNormalizado(nombreNormalizado);
        sugerencia.setDescripcion(descripcion);
        sugerencia.setEstado(EstadoEspecialidadPendiente.PENDIENTE);

        EspecialidadPendiente guardada = especialidadPendienteRepository.save(sugerencia);

        return new EspecialidadPendienteResponse(
                guardada.getEspecialidadPendienteId(),
                guardada.getNombre(),
                guardada.getDescripcion(),
                guardada.getEstado(),
                null,
                null,
                guardada.getCreatedAt(),
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadPendienteResponse> obtenerMisSugerencias(Integer usuarioId
    ) {
        return especialidadPendienteRepository
                .findByUsuarioUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }
    
    private EspecialidadPendienteResponse convertirResponse(EspecialidadPendiente sugerencia) {
        Integer especialidadId = sugerencia.getEspecialidad() != null
                ? sugerencia.getEspecialidad().getEspecialidadId()
                : null;

        return new EspecialidadPendienteResponse(
                sugerencia.getEspecialidadPendienteId(),
                sugerencia.getNombre(),
                sugerencia.getDescripcion(),
                sugerencia.getEstado(),
                sugerencia.getMotivoRechazo(),
                especialidadId,
                sugerencia.getCreatedAt(),
                sugerencia.getReviewedAt()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminEspecialidadPendienteResponse> obtenerSugerenciasAdmin(
            EstadoEspecialidadPendiente estado
    ) {
        List<EspecialidadPendiente> sugerencias;

        if (estado == null) {
            sugerencias = especialidadPendienteRepository.findAllByOrderByCreatedAtDesc();
            
        } else {
            sugerencias = especialidadPendienteRepository.findByEstadoOrderByCreatedAtAsc(estado);
            
        }

        return sugerencias.stream()
                .map(this::convertirAdminResponse)
                .toList();
    }
    
    private AdminEspecialidadPendienteResponse convertirAdminResponse(
            EspecialidadPendiente sugerencia
    ) {
        Integer especialidadId = sugerencia.getEspecialidad() != null
                ? sugerencia.getEspecialidad().getEspecialidadId()
                : null;

        return new AdminEspecialidadPendienteResponse(
                sugerencia.getEspecialidadPendienteId(),
                sugerencia.getUsuario().getUsuarioId(),
                sugerencia.getUsuario().getNombre(),
                sugerencia.getUsuario().getEmail(),
                sugerencia.getNombre(),
                sugerencia.getDescripcion(),
                sugerencia.getEstado(),
                sugerencia.getMotivoRechazo(),
                especialidadId,
                sugerencia.getCreatedAt(),
                sugerencia.getReviewedAt()
        );
    }
    
    @Override
    @Transactional
    public EspecialidadPendienteResponse aprobarSugerencia( Integer sugerenciaId, Integer administradorId) {
        EspecialidadPendiente sugerencia = especialidadPendienteRepository.findById(sugerenciaId)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("La sugerencia no existe")
                        );

        if (sugerencia.getEstado() != EstadoEspecialidadPendiente.PENDIENTE) {

            throw new OperacionInvalidaException("La sugerencia ya fue revisada");
        }

        Usuario administrador = usuarioRepository.findById(administradorId)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("El administrador no existe")
                        );

        if (administrador.getRol() != RolUsuario.ADMIN) {
            throw new OperacionInvalidaException("El usuario no tiene permisos de administrador");
        }

        boolean especialidadExistente = especialidadRepository.existsByNombreIgnoreCase(sugerencia.getNombre());

        if (especialidadExistente) {
            throw new RecursoDuplicadoException("Ya existe una especialidad con ese nombre" );
        }

        Especialidad especialidad = new Especialidad();

        especialidad.setNombre(sugerencia.getNombre());
        especialidad.setDescripcion(sugerencia.getDescripcion());
        especialidad.setActiva(true);

        Especialidad especialidadGuardada = especialidadRepository.save(especialidad);

        sugerencia.setEstado(EstadoEspecialidadPendiente.APROBADA);
        sugerencia.setEspecialidad(especialidadGuardada);
        sugerencia.setReviewedAt(LocalDateTime.now());
        sugerencia.setReviewedBy(administrador);
        sugerencia.setMotivoRechazo(null);

        EspecialidadPendiente sugerenciaGuardada = especialidadPendienteRepository.save(sugerencia);

        return convertirResponse(sugerenciaGuardada);
    }
    
    @Override
    @Transactional
    public EspecialidadPendienteResponse rechazarSugerencia(Integer sugerenciaId, Integer administradorId,
            RechazarEspecialidadPendienteRequest request) {
    	
        EspecialidadPendiente sugerencia = especialidadPendienteRepository.findById(sugerenciaId)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("La sugerencia no existe")
                        );

        if (sugerencia.getEstado() != EstadoEspecialidadPendiente.PENDIENTE) {

            throw new OperacionInvalidaException("La sugerencia ya fue revisada");
        }

        Usuario administrador = usuarioRepository.findById(administradorId)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("El administrador no existe")
                        );

        if (administrador.getRol() != RolUsuario.ADMIN) {
            throw new OperacionInvalidaException("El usuario no tiene permisos de administrador");
        }

        String motivo = request.motivo().trim();

        sugerencia.setEstado(EstadoEspecialidadPendiente.RECHAZADA);
        sugerencia.setMotivoRechazo(motivo);
        sugerencia.setReviewedAt(LocalDateTime.now());
        sugerencia.setReviewedBy(administrador);
        sugerencia.setEspecialidad(null);

        EspecialidadPendiente guardada = especialidadPendienteRepository.save(sugerencia);

        return convertirResponse(guardada);
    }
}