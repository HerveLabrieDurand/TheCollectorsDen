package com.collectorsden.demo.auth.service;

import ch.qos.logback.classic.Level;
import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.config.security.JwtService;
import com.collectorsden.demo.exception.auth.EmailAlreadyInUseException;
import com.collectorsden.demo.exception.auth.InvalidCredentialsException;
import com.collectorsden.demo.exception.database.DatabaseOperationException;
import com.collectorsden.demo.model.User;
import com.collectorsden.demo.model.enums.user.UserRole;
import com.collectorsden.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.collectorsden.demo.util.LoggerTestUtil;
import com.collectorsden.demo.util.LoggerTestUtil.TestLogAppender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Authenticate_Should_Return_AuthResponse_When_Credentials_Are_Valid() {
        // Arrange
        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        AuthenticateRequest request = new AuthenticateRequest("user@example.com", "password123");
        User user = User.builder()
                .email("user@example.com")
                .encryptedPassword("encryptedPassword")
                .role(UserRole.USER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // Simulating successful authentication
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("mockJwtToken", response.getAccessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("user@example.com");
        verify(jwtService).generateToken(user);

        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Authenticating user with email: " + user.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.INFO,
                "User authenticated successfully: " + user.getEmail()
        );
    }

    @Test
    void Authenticate_Should_Throw_InvalidCredentialsException_When_Credentials_Are_Invalid() {
        // Arrange
        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        AuthenticateRequest request = new AuthenticateRequest("user@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(request));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userRepository, jwtService);
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Authenticating user with email: " + request.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "Authentication failed for email: " + request.getEmail()
        );
    }

    @Test
    void Authenticate_Should_Throw_InvalidCredentialsException_When_Database_Fetching_Throws() {
        // Arrange
        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        AuthenticateRequest request = new AuthenticateRequest("user@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("user@example.com")).thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(request));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtService);
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Authenticating user with email: " + request.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "Authentication failed for email: " + request.getEmail()
        );
    }

    @Test
    void Register_Should_Save_User_When_Credentials_Are_Valid() {
        // Arrange
        var email = "user@example.com";
        var password = "password123";

        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mockJwtToken");

        // Act
        User user = authenticationService.register(request);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("encryptedPassword", user.getPassword());
        verify(userRepository).save(any(User.class));
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Registering user with email: " + request.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.INFO,
                "User registered successfully: " + request.getEmail()
        );
    }

    @Test
    void Register_Throw_EmailAlreadyInUseException_When_Email_Already_In_Use() {
        // Arrange
        var email = "user@example.com";
        var password = "password123";

        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> authenticationService.register(request));
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(jwtService);
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Registering user with email: " + request.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "User with email: " + request.getEmail() + " already exists."
        );
    }

    @Test
    void Register_Throw_DatabaseOperationException_When_Error_Saving_User() {
        // Arrange
        var email = "user@example.com";
        var password = "password123";

        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");
        doThrow(RuntimeException.class).when(userRepository).save(any(User.class));

        // Act & Assert
        assertThrows(DatabaseOperationException.class, () -> authenticationService.register(request));
        verify(userRepository).save(any(User.class));
        verifyNoInteractions(jwtService);
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Registering user with email: " + request.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "Could not register user with email: " + request.getEmail() + " to the database"
        );
    }
}
