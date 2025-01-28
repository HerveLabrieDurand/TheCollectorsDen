package com.collectorsden.demo.exception.auth;

public class EmailAlreadyConfirmedException extends RuntimeException {
    public EmailAlreadyConfirmedException() {
        super("Email already confirmed");
    }
}
