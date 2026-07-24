package com.nocta.myown.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nocta.myown.entity.TarjetaConfiguracion;
import com.nocta.myown.repository.TarjetaConfiguracionRepository;
import com.nocta.myown.request.TarjetaConfiguracionRequest;
import com.nocta.myown.response.LogoTarjetaResponse;
import com.nocta.myown.response.TarjetaConfiguracionResponse;
import com.nocta.myown.service.ImagenService;
import com.nocta.myown.service.TarjetaConfiguracionService;

@Service
public class TarjetaConfiguracionServiceImpl implements TarjetaConfiguracionService{

	private final TarjetaConfiguracionRepository repository;
	private final ImagenService imagenService;

    public TarjetaConfiguracionServiceImpl(TarjetaConfiguracionRepository repository,
    		ImagenService imagenService) {
        this.repository = repository;
        this.imagenService = imagenService;
    }

    @Override
    @Transactional(readOnly = true)
    public TarjetaConfiguracionResponse obtenerPorUsuario(Integer usuarioId) {
        TarjetaConfiguracion configuracion = repository.findById(usuarioId)
                        .orElseGet(() -> crearConfiguracionDefault(usuarioId));

        return convertirAResponse(configuracion);
    }

    @Override
    @Transactional
    public TarjetaConfiguracionResponse guardar(Integer usuarioId, TarjetaConfiguracionRequest request) {
        TarjetaConfiguracion configuracion = repository.findById(usuarioId)
                        .orElseGet(() -> crearConfiguracionDefault(usuarioId));

        configuracion.setDisenio(normalizarDisenio(request.disenio()));

        configuracion.setColorPrincipal(normalizarColor(request.colorPrincipal()));

        configuracion.setFormatoLogo(normalizarFormatoLogo(request.formatoLogo()));

        configuracion.setMostrarTelefono(request.mostrarTelefono());

        configuracion.setMostrarEmail(request.mostrarEmail());

        configuracion.setMostrarLocalidad(request.mostrarLocalidad());

        configuracion.setMostrarMatricula(request.mostrarMatricula());

        configuracion.setMostrarNombreComercial(request.mostrarNombreComercial());

        configuracion.setMostrarQr(request.mostrarQr());

        TarjetaConfiguracion guardada = repository.save(configuracion);

        return convertirAResponse(guardada);
    }
    
    @Override
    @Transactional
    public LogoTarjetaResponse guardarLogo(Integer usuarioId, MultipartFile archivo) {
        String logoUrl = imagenService.subirLogoTarjeta(usuarioId, archivo);

        TarjetaConfiguracion configuracion = repository.findById(usuarioId)
                .orElseGet(() -> crearConfiguracionDefault(usuarioId));

        configuracion.setLogoUrl(logoUrl);
        repository.save(configuracion);

        return new LogoTarjetaResponse(logoUrl);
    }

    private TarjetaConfiguracion crearConfiguracionDefault(Integer usuarioId) {
        TarjetaConfiguracion configuracion =  new TarjetaConfiguracion();

        configuracion.setUsuarioId(usuarioId);
        configuracion.setDisenio("MODELO_1");
        configuracion.setColorPrincipal("AZUL");
        configuracion.setFormatoLogo("CUADRADO");

        configuracion.setMostrarTelefono(true);
        configuracion.setMostrarEmail(true);
        configuracion.setMostrarLocalidad(false);
        configuracion.setMostrarMatricula(false);
        configuracion.setMostrarNombreComercial(false);
        configuracion.setMostrarQr(true);

        return configuracion;
    }

    private TarjetaConfiguracionResponse convertirAResponse(TarjetaConfiguracion configuracion) {
        return new TarjetaConfiguracionResponse(
                configuracion.getUsuarioId(),
                configuracion.getDisenio(),
                configuracion.getColorPrincipal(),
                configuracion.getLogoUrl(),
                configuracion.getFormatoLogo(),
                configuracion.isMostrarTelefono(),
                configuracion.isMostrarEmail(),
                configuracion.isMostrarLocalidad(),
                configuracion.isMostrarMatricula(),
                configuracion.isMostrarNombreComercial(),
                configuracion.isMostrarQr()
        );
    }

    private String normalizarDisenio(String disenio) {
        if ("MODELO_2".equals(disenio)) {
            return "MODELO_2";
        }

        return "MODELO_1";
    }

    private String normalizarColor(String color) {
        if (color == null) {
            return "AZUL";
        }

        switch (color.trim().toUpperCase()) {
            case "GRIS":
            case "VERDE":
            case "BORDO":
            case "VIOLETA":
            case "AZUL":
                return color.trim().toUpperCase();

            default:
                return "AZUL";
        }
    }

    private String normalizarFormatoLogo(String formatoLogo) {
        if ("HORIZONTAL".equals(formatoLogo)) {
            return "HORIZONTAL";
        }

        return "CUADRADO";
    }

}
