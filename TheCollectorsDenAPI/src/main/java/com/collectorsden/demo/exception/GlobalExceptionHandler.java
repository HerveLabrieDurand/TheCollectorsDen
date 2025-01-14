package com.collectorsden.demo.exception;

import com.collectorsden.demo.exception.auth.EmailAlreadyInUseException;
import com.collectorsden.demo.exception.auth.InvalidCredentialsException;
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
