package com.nocta.myown.response;

import java.time.LocalDateTime;
import java.util.List;

public record EspecialidadSyncResponse(
        LocalDateTime serverTime,
        List<EspecialidadResponse> especialidades
) {}