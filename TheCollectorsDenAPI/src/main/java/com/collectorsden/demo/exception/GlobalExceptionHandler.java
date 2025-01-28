package com.collectorsden.demo.exception;

import com.collectorsden.demo.exception.auth.*;
import com.collectorsden.demo.exception.database.DatabaseOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "InvalidCredentialsException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(EmailAlreadyInUseException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "EmailAlreadyInUseException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(DatabaseOperationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "DatabaseOperationException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, String>> handleTokenExpiredException(TokenExpiredException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "TokenExpiredException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Map<String, String>> handleTokenInvalidException(TokenInvalidException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "TokenInvalidException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailNotRegisteredException.class)
    public ResponseEntity<Map<String, String>> handleEmailNotRegisteredException(EmailNotRegisteredException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "EmailNotRegisteredException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmailAlreadyConfirmedException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyConfirmedException(EmailAlreadyConfirmedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "EmailAlreadyConfirmedException");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
