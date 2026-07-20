package com.nocta.myown.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nocta.myown.mapper.EspecialidadMapper;
import com.nocta.myown.repository.EspecialidadRepository;
import com.nocta.myown.response.EspecialidadResponse;
import com.nocta.myown.response.EspecialidadSyncResponse;
import com.nocta.myown.service.EspecialidadService;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {
	
	private final EspecialidadRepository especialidadRepository;
	
	public EspecialidadServiceImpl (EspecialidadRepository especialidadRepository) {
		this.especialidadRepository = especialidadRepository;
	}

	@Override
	public List<EspecialidadResponse> trearEspecialidades() {
		
		return especialidadRepository.findAll()
				.stream()
				.map(EspecialidadMapper::toResponse)
				.toList();
		
	}

	@Transactional(readOnly = true)
    @Override
    public EspecialidadSyncResponse traerEspecialidadesActivas() {
        LocalDateTime serverTime = LocalDateTime.now();

        List<EspecialidadResponse> especialidades = especialidadRepository
                .findByActivaTrueOrderByNombreAsc()
                .stream()
                .map(EspecialidadMapper::toResponse)
                .toList();

        return new EspecialidadSyncResponse(serverTime, especialidades);
    }

    @Transactional(readOnly = true)
    @Override
    public EspecialidadSyncResponse sincronizarEspecialidades(LocalDateTime desde) {
        LocalDateTime serverTime = LocalDateTime.now();

        List<EspecialidadResponse> especialidades = especialidadRepository
                .findByUpdatedAtAfterOrderByUpdatedAtAsc(desde)
                .stream()
                .map(EspecialidadMapper::toResponse)
                .toList();

        return new EspecialidadSyncResponse(serverTime, especialidades);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EspecialidadResponse> buscarEspecialidades(String texto) {
        if (texto == null || texto.isBlank()) {
            return especialidadRepository.findByActivaTrueOrderByNombreAsc()
                    .stream()
                    .map(EspecialidadMapper::toResponse)
                    .toList();
        }

        return especialidadRepository
                .findByActivaTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(texto.trim())
                .stream()
                .map(EspecialidadMapper::toResponse)
                .toList();
    }

}
