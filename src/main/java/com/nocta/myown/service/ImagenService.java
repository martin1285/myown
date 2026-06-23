package com.nocta.myown.service;

import org.springframework.web.multipart.MultipartFile;


public interface ImagenService {
    String subirFotoPerfil(MultipartFile archivo, Integer usuarioId);
    
}
