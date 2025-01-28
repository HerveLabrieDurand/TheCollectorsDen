package com.collectorsden.demo.exception.auth;

public class EmailNotRegisteredException extends RuntimeException {
    public EmailNotRegisteredException(String email) {
        super("User with email " + email + " is not registered.");
    }
}
