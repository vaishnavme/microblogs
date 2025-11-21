package com.vaishnavs.microblogs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("Validation error");

        Map<String, Object> error = new HashMap<>();
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("message", message);
        error.put("error", errorDetails);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
