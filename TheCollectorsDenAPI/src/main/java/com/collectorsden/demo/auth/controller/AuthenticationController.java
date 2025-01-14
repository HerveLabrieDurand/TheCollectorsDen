package com.collectorsden.demo.auth.controller;

import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.auth.service.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request
    ) {
        return ResponseEntity.ok(this.authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(this.authenticationService.register(request));
    }
}
