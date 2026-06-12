package com.nocta.myown.error;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorApi {
	
	private LocalDateTime hora;
	private int status;
	private String error;
	private String message;
	private String path;
	
	

}
