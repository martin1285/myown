package com.nocta.myown.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.service.ImagenService;

@Service
public class ImagenServiceImpl implements ImagenService {

    private static final long TAMANIO_MAXIMO = 5 * 1024 * 1024; // 5 MB
    private static final long TAMANIO_MAXIMO_LOGO = 2L * 1024L * 1024L;
    private static final List<String> TIPOS_PERMITIDOS = List.of("image/jpeg", "image/png", "image/webp");
    
    private static final Logger log = LoggerFactory.getLogger(ImagenServiceImpl.class);

    private final Cloudinary cloudinary;

    public ImagenServiceImpl(Cloudinary cloudinary, UsuarioRepository usuarioRepository) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String subirFotoPerfil(MultipartFile archivo, Integer usuarioId) {
    	
    	log.info("Inicio subida foto perfil. usuarioId={}", usuarioId);

        if (archivo != null) {
            log.info(
                    "Archivo recibido. usuarioId={}, nombre={}, contentType={}, size={}",
                    usuarioId,
                    archivo.getOriginalFilename(),
                    archivo.getContentType(),
                    archivo.getSize()
            );
        } else {
            log.warn("Archivo recibido null. usuarioId={}", usuarioId);
        }
        validarArchivo(archivo, TAMANIO_MAXIMO);

        try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", "myown/perfiles",
                    "public_id", "usuario_" + usuarioId,
                    "overwrite", true,
                    "resource_type", "image"
            );
            
            log.info("Enviando imagen a Cloudinary. usuarioId={}", usuarioId);

            Map<?, ?> resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            String url = (String) resultado.get("secure_url");
            
            log.info("Foto subida correctamente a Cloudinary. usuarioId={}, url={}", usuarioId, url);
            return (String) resultado.get("secure_url");

        } catch (IOException e) {
            log.error("Error IO al subir imagen a Cloudinary. usuarioId={}", usuarioId, e);
            throw new RuntimeException("Error al subir la imagen", e);
        } catch (Exception e) {
            log.error("Error inesperado al subir imagen a Cloudinary. usuarioId={}", usuarioId, e);
            throw new RuntimeException("Error al subir la imagen", e);
        }
    }
    
    @Override
    public String subirLogoTarjeta(Integer usuarioId, MultipartFile archivo) {
    	validarArchivo(archivo, TAMANIO_MAXIMO_LOGO);

        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "myown/tarjetas",
                            "public_id", "logo_tarjeta_" + usuarioId,
                            "overwrite", true,
                            "resource_type", "image"
                    )
            );

            Object secureUrl = resultado.get("secure_url");

            if (secureUrl == null) {
                throw new RuntimeException("Cloudinary no devolvió la URL del logo");
            }

            return secureUrl.toString();

        } catch (IOException e) {
            throw new RuntimeException("No se pudo subir el logo", e);
        }
    }

    private void validarArchivo(MultipartFile archivo, long tamanio) {
        if (archivo == null || archivo.isEmpty()) {
            log.warn("Validación fallida: archivo vacío o null");
            throw new IllegalArgumentException("El archivo está vacío");
        }

        if (archivo.getSize() > tamanio) {
            log.warn(
                    "Validación fallida: archivo supera tamaño máximo. size={}, max={}",
                    archivo.getSize(),
                    tamanio
            );
            throw new IllegalArgumentException("El archivo no puede superar los 5MB");
        }

        String tipo = archivo.getContentType();
        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo)) {
            log.warn("Validación fallida: formato no permitido. contentType={}", tipo);
            throw new IllegalArgumentException("Formato de imagen no permitido. Solo JPG, PNG o WEBP");
        }

        log.info(
                "Archivo validado correctamente. nombre={}, contentType={}, size={}",
                archivo.getOriginalFilename(),
                tipo,
                archivo.getSize()
        );
    }
    
    
   
}