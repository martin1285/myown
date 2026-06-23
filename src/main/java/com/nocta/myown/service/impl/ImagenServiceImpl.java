package com.nocta.myown.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nocta.myown.entity.Usuario;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.response.UsuarioResponse;
import com.nocta.myown.service.ImagenService;

@Service
public class ImagenServiceImpl implements ImagenService {

    private static final long TAMANIO_MAXIMO = 5 * 1024 * 1024; // 5 MB
    private static final List<String> TIPOS_PERMITIDOS = List.of("image/jpeg", "image/png", "image/webp");

    private final Cloudinary cloudinary;

    public ImagenServiceImpl(Cloudinary cloudinary, UsuarioRepository usuarioRepository) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String subirFotoPerfil(MultipartFile archivo, Integer usuarioId) {
        validarArchivo(archivo);

        try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", "myown/perfiles",
                    "public_id", "usuario_" + usuarioId,
                    "overwrite", true,
                    "resource_type", "image"
            );

            Map<?, ?> resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            return (String) resultado.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen", e);
        }
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        if (archivo.getSize() > TAMANIO_MAXIMO) {
            throw new IllegalArgumentException("El archivo no puede superar los 5MB");
        }

        String tipo = archivo.getContentType();
        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo)) {
            throw new IllegalArgumentException("Formato de imagen no permitido. Solo JPG, PNG o WEBP");
        }
    }
    
   
}