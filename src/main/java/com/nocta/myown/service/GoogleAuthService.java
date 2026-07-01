package com.nocta.myown.service;

import com.nocta.myown.response.GoogleUserInfo;

public interface GoogleAuthService {

	 public GoogleUserInfo verificarToken(String idTokenString) ;
}
