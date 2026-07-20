package com.nocta.myown.request;

import java.util.List;

public record GuardarEspecialidadesUsuarioRequest(
        List<Integer> especialidadesIds
) {}