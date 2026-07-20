package com.nocta.myown.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.entity.UsuarioEspecialidad;
import com.nocta.myown.entity.UsuarioEspecialidadId;
import com.nocta.myown.mapper.EspecialidadMapper;
import com.nocta.myown.mapper.UsuarioEspecialidadMapper;
import com.nocta.myown.repository.EspecialidadRepository;
import com.nocta.myown.repository.UsuarioEspecialidadRepository;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.GuardarEspecialidadesUsuarioRequest;
import com.nocta.myown.response.EspecialidadResponse;
import com.nocta.myown.response.UsuarioEspecialidadResponse;
import com.nocta.myown.service.UsuarioEspecialidadService;


@Service
public class UsuarioEspecialidadServiceImpl implements UsuarioEspecialidadService {
	
	private final UsuarioEspecialidadRepository usuarioEspecialidadRepository;
	private final UsuarioRepository usuarioRepository;
	private final EspecialidadRepository especialidadRepository;
	
	public UsuarioEspecialidadServiceImpl(UsuarioEspecialidadRepository usuarioEspecialidadRepository,
			UsuarioRepository usuarioRepository,EspecialidadRepository especialidadRepository ) {
		this.usuarioEspecialidadRepository = usuarioEspecialidadRepository;
		this.usuarioRepository = usuarioRepository;
		this.especialidadRepository = especialidadRepository;
	}
	

	@Transactional
	@Override
	public UsuarioEspecialidadResponse agregarEspecialidadParaUsuario(Integer usuarioId, Integer especialidadId) {
		boolean existeUsuario = usuarioRepository.existsById(usuarioId);
		
		if(!existeUsuario) throw new IllegalArgumentException("No existe el usuario");
		
		
		boolean existeEspecialiad = especialidadRepository.existsById(especialidadId);
		if (!existeEspecialiad) throw new IllegalArgumentException("No existe la especialidad"); 
		
		boolean existe = usuarioEspecialidadRepository.existsByUsuarioIdAndEspecialidadId(usuarioId,
				especialidadId);
		
		if (existe)throw new IllegalArgumentException("la especialidad ya existe para ese usuario");
		
		
		UsuarioEspecialidad ue = new UsuarioEspecialidad();
		ue.setUsuarioId(usuarioId);
		ue.setEspecialidadId(especialidadId);
		UsuarioEspecialidad ueresp = usuarioEspecialidadRepository.save(ue);
		
		return UsuarioEspecialidadMapper.toResponse(ueresp);
	}

	
	@Transactional(readOnly = true)
	@Override
	public List<EspecialidadResponse> traerEspecialidadesByUsuario(Integer usuarioId) {
		boolean existeUsuario = usuarioRepository.existsById(usuarioId);
		
		if(!existeUsuario) throw new IllegalArgumentException("No existe el usuario");
		
		
		return usuarioEspecialidadRepository.findEspecialidadesByUsuarioId(usuarioId)
				.stream()
				.map(EspecialidadMapper::toResponse)
				.toList();
	}

	@Transactional
	@Override
	public void borrarEspecialidadEnUsuario(Integer usuarioId, Integer especialidadId) {
		boolean existeUsuario = usuarioRepository.existsById(usuarioId);
		
		if(!existeUsuario) throw new IllegalArgumentException("No existe el usuario");
		
		boolean existeRelacion = usuarioEspecialidadRepository.existsByUsuarioIdAndEspecialidadId(
				usuarioId, especialidadId);
		
		if (!existeRelacion) throw new IllegalArgumentException("no existe la especialidad " + especialidadId
		+ " para el usuario " + usuarioId);
		
		usuarioEspecialidadRepository.deleteById(new UsuarioEspecialidadId(usuarioId,especialidadId));
		
		
	}
	
	@Transactional
    @Override
    public void borrarTodasLasEspecialidadesDelUsuario(Integer usuarioId) {

        boolean existeUsuario = usuarioRepository.existsById(usuarioId);

        if (!existeUsuario) {
            throw new IllegalArgumentException("No existe el usuario");
        }

        usuarioEspecialidadRepository.deleteByUsuarioId(usuarioId);
    }
	
	@Transactional
	@Override
	public List<EspecialidadResponse> reemplazarEspecialidadesDelUsuario(Integer usuarioId,
	        GuardarEspecialidadesUsuarioRequest request) {
	    boolean existeUsuario = usuarioRepository.existsById(usuarioId);

	    if (!existeUsuario) {
	        throw new IllegalArgumentException("No existe el usuario");
	    }

	    List<Integer> ids = request.especialidadesIds() == null
	            ? List.of()
	            : request.especialidadesIds()
	                    .stream()
	                    .distinct()
	                    .toList();

	    if (!ids.isEmpty()) {
	        long cantidadExistente = especialidadRepository.countByEspecialidadIdInAndActivaTrue(ids);

	        if (cantidadExistente != ids.size()) {
	            throw new IllegalArgumentException("Una o más especialidades no existen o no están activas");
	        }
	    }

	    usuarioEspecialidadRepository.deleteByUsuarioId(usuarioId);

	    List<UsuarioEspecialidad> nuevas = ids.stream()
	            .map(especialidadId -> {
	                UsuarioEspecialidad ue = new UsuarioEspecialidad();
	                ue.setUsuarioId(usuarioId);
	                ue.setEspecialidadId(especialidadId);
	                return ue;
	            })
	            .toList();

	    usuarioEspecialidadRepository.saveAll(nuevas);

	    return usuarioEspecialidadRepository.findEspecialidadesByUsuarioId(usuarioId)
	            .stream()
	            .map(EspecialidadMapper::toResponse)
	            .toList();
	}

}
