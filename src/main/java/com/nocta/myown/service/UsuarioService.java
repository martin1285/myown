package com.nocta.myown.service;

import org.springframework.web.multipart.MultipartFile;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.request.ActualizarPerfilRequest;
import com.nocta.myown.request.CambiarPasswordRequest;
import com.nocta.myown.response.UsuarioResponse;

public interface UsuarioService {
	
	public UsuarioResponse actualizarUsuario(Usuario usuario, ActualizarPerfilRequest actualizarUsuarioRequest);
	
	public void cambiarPassword(Usuario usuario, CambiarPasswordRequest request);
	
	public void eliminarCuenta(Usuario usuario, String passwordActual);
	
	public UsuarioResponse actualizarFotoPerfil(Usuario usuario, MultipartFile foto);

}
