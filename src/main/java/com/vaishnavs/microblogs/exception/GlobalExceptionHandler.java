package com.vaishnavs.microblogs.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaishnavs.microblogs.principal.UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private String getCurrentUserId() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
      }
    } catch (Exception e) {
      //
    }

    return "guest";
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());

    log.error("Resource not found for userId: {} :: {}", getCurrentUserId(), error.toString());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());

    log.error("Bad request for userId: {} :: {}", getCurrentUserId(), error.toString());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {

    Map<String, Object> extra = new HashMap<>();

    ex.getBindingResult().getFieldErrors()
        .forEach(err -> extra.put(err.getField(), err.getDefaultMessage()));

    ErrorResponse error = new ErrorResponse(
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "Validation failed",
        extra);

    log.error("Validation failed for userId: {} :: {}", getCurrentUserId(), error.toString());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobal(Exception ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());

    log.error("Internal server error for userId: {} :: {}", getCurrentUserId(), error.toString());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
