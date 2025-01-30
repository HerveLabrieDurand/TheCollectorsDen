package com.collectorsden.demo.exception.auth;

public class EmailNotConfirmedException extends RuntimeException {
    public EmailNotConfirmedException() {
        super("Email not confirmed.");
    }
}
