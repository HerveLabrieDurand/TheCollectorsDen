package com.collectorsden.demo.exception.auth;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("The provided email " + email + " is already in use.");
    }
}
