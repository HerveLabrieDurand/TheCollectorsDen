package com.collectorsden.demo.exception.auth;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Invalid token");
    }
}
