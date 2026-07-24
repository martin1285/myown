package com.nocta.myown.request;

public record TarjetaConfiguracionRequest(
        String disenio,
        String colorPrincipal,
        String formatoLogo,
        boolean mostrarTelefono,
        boolean mostrarEmail,
        boolean mostrarLocalidad,
        boolean mostrarMatricula,
        boolean mostrarNombreComercial,
        boolean mostrarQr
) {
}