package com.example.cashcard;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
    	System.out.println(ex);
        return new ResponseEntity<>("Internal Server Error: An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
