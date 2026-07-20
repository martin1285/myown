package com.nocta.myown.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEspecialidadId implements Serializable {

    private Integer usuarioId;
    private Integer especialidadId;
}