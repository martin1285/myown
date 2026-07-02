package com.nocta.myown.response;

import java.time.LocalDateTime;

import com.nocta.myown.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record UsuarioResponse(
        Integer id,
        String nombre,
        String email,
        String telefono,
        String proveedor_auth,
        String localidad,
        String matricula,
        String descripcion,
        String nombreComercial,
        String fotoUrl,
        Boolean perfilCompleto,
        String plan,
        Boolean suscripcionActiva,
        LocalDateTime fechaAlta
) {
    public UsuarioResponse(Usuario usuario) {
        this(
                usuario.getUsuarioId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getProveedorAuth(),
                usuario.getLocalidad(),
                usuario.getMatricula(),
                usuario.getDescripcion(),
                usuario.getNombreComercial(),
                usuario.getFotoUrl(),
                usuario.getPerfilCompleto(),
                usuario.getPlan(),
                usuario.getSuscripcionActiva(),
                usuario.getFechaAlta()
        );
    }
}
