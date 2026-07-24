package com.nocta.myown.response;

public record TarjetaConfiguracionResponse(
        Integer usuarioId,
        String disenio,
        String colorPrincipal,
        String logoUrl,
        String formatoLogo,
        boolean mostrarTelefono,
        boolean mostrarEmail,
        boolean mostrarLocalidad,
        boolean mostrarMatricula,
        boolean mostrarNombreComercial,
        boolean mostrarQr
) {
}