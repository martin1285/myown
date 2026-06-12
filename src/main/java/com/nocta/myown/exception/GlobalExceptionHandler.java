package com.nocta.myown.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nocta.myown.error.ErrorApi;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorApi> argumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest req ){
		 
		String mensaje = ex.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(e -> e.getField() + ": " + e.getDefaultMessage())
	            .collect(Collectors.joining(", "));

		
		ErrorApi error = new ErrorApi(
		            LocalDateTime.now(),
		            HttpStatus.BAD_REQUEST.value(),
		            "Bad Request",
		            mensaje,
		            req.getRequestURI()
		    );
		 
		 return ResponseEntity.badRequest().body(error);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorApi> notFoundException(EntityNotFoundException ex, HttpServletRequest req){
		ErrorApi error = new ErrorApi(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"Not Fouwnd",
				ex.getMessage(),
				req.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorApi> illegalArgumentError(IllegalArgumentException ex, HttpServletRequest req){
		ErrorApi error = new ErrorApi(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Bad request",
				ex.getMessage(),
				req.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorApi> genericError(Exception ex, HttpServletRequest req){
		ErrorApi error = new ErrorApi(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				ex.getMessage(),
				req.getRequestURI());
				
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
