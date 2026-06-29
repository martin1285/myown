package com.nocta.myown.service;

import com.nocta.myown.request.OlvidePasswordRequest;
import com.nocta.myown.request.RestablecerPasswordRequest;

public interface RecuperacionPasswordService {
	
	void solicitarRecuperacion(OlvidePasswordRequest request);

    void restablecerPassword(RestablecerPasswordRequest request);

}
