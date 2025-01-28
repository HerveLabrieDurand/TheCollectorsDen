package com.collectorsden.demo.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token is expired");
    }
}
