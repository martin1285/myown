package com.nocta.myown.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarjeta_configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaConfiguracion {

    @Id
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "disenio", nullable = false, length = 30)
    private String disenio = "MODELO_1";

    @Column(name = "color_principal", nullable = false, length = 30)
    private String colorPrincipal = "AZUL";

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "formato_logo", nullable = false, length = 20)
    private String formatoLogo = "CUADRADO";

    @Column(name = "mostrar_telefono", nullable = false)
    private boolean mostrarTelefono = true;

    @Column(name = "mostrar_email", nullable = false)
    private boolean mostrarEmail = true;

    @Column(name = "mostrar_localidad", nullable = false)
    private boolean mostrarLocalidad;

    @Column(name = "mostrar_matricula", nullable = false)
    private boolean mostrarMatricula;

    @Column(name = "mostrar_nombre_comercial", nullable = false)
    private boolean mostrarNombreComercial;

    @Column(name = "mostrar_qr", nullable = false)
    private boolean mostrarQr = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        createdAt = ahora;
        updatedAt = ahora;

        normalizarValores();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        normalizarValores();
    }

    private void normalizarValores() {
        if (disenio == null || disenio.isBlank()) {
            disenio = "MODELO_1";
        }

        if (colorPrincipal == null || colorPrincipal.isBlank()) {
            colorPrincipal = "AZUL";
        }

        if (formatoLogo == null || formatoLogo.isBlank()) {
            formatoLogo = "CUADRADO";
        }
    }

   
}