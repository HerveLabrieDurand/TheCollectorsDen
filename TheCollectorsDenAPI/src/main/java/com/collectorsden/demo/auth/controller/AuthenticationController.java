package com.collectorsden.demo.auth.controller;

import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.auth.service.AuthenticationService;
import com.collectorsden.demo.auth.service.EmailService;
import com.collectorsden.demo.model.ConfirmationToken;
import com.collectorsden.demo.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or email not confirmed", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request
    ) {
        return ResponseEntity.ok(this.authenticationService.authenticate(request));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Email already in use", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error when saving user", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequest request
    ) {
        User user = this.authenticationService.register(request);
        this.emailService.sendConfirmationEmail(user);

        return ResponseEntity.ok().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully confirmed email with token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Token is associated with an already confirmed email", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token is expired or invalid", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
        this.emailService.confirmEmail(token);

        return ResponseEntity.ok().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully resent confirmation email", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Email is either not registered or already confirmed", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/resend-confirmation-email")
    public ResponseEntity<Void> resendConfirmEmail(@RequestParam String email) {
        this.emailService.resendConfirmationEmail(email);

        return ResponseEntity.ok().build();
    }
}
