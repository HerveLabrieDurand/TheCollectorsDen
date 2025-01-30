package com.collectorsden.demo.auth.service;

import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.config.security.JwtService;
import com.collectorsden.demo.exception.auth.EmailAlreadyInUseException;
import com.collectorsden.demo.exception.auth.EmailNotConfirmedException;
import com.collectorsden.demo.exception.auth.InvalidCredentialsException;
import com.collectorsden.demo.exception.database.DatabaseOperationException;
import com.collectorsden.demo.model.User;
import com.collectorsden.demo.model.enums.user.UserStatus;
import com.collectorsden.demo.repository.UserRepository;
import com.collectorsden.demo.model.enums.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        logger.info("Authenticating user with email: {}", request.getEmail());

        var user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", request.getEmail());
                    return new BadCredentialsException("");
                });

        if (!user.isEmailConfirmed()) {
            logger.error("User with email: {} has not confirmed their email", user.getEmail());
            throw new EmailNotConfirmedException();
        }

        try {
            // The AuthenticationProvider Bean in AppConfig takes care of comparing passwords, no explicit decode necessary
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            logger.info("User authenticated successfully: {}", user.getEmail());

            return this.getAuthenticationResponse(user);
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for email: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }
    }

    public User register(RegisterRequest request) {
        logger.info("Registering user with email: {}", request.getEmail());

        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("User with email: {} already exists.", request.getEmail());
            throw new EmailAlreadyInUseException(request.getEmail());
        }

        try {
            var user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .encryptedPassword(this.passwordEncoder.encode(request.getPassword()))
                    .country(request.getCountry())
                    .address(request.getAddress())
                    .city(request.getCity())
                    .postalCode(request.getPostalCode())
                    .profilePictureUrl("")
                    .role(UserRole.USER)
                    .status(UserStatus.ACTIVE)
                    .phoneNumber(request.getPhoneNumber())
                    .preferences("{}")
                    .build();

            this.userRepository.save(user);

            logger.info("User registered successfully: {}", user.getEmail());

            return user;
        }
        catch (RuntimeException e) {
            var msg = "Could not register user with email: " + request.getEmail() + " to the database";
            logger.error(msg);
            throw new DatabaseOperationException(msg);
        }
    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        var jwtToken = this.jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
