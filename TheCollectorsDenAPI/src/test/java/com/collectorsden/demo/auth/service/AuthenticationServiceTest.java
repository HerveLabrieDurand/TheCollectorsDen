package com.collectorsden.demo.auth.service;

import ch.qos.logback.classic.Level;
import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.config.security.JwtService;
import com.collectorsden.demo.exception.auth.EmailAlreadyInUseException;
import com.collectorsden.demo.exception.auth.EmailNotConfirmedException;
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
    private AuthenticationManager authenticationManagerMock;
    @Mock
    private JwtService jwtServiceMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private AuthenticationService sut;

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
                .emailConfirmed(true)
                .role(UserRole.USER)
                .build();

        when(this.authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // Simulating successful authentication
        when(this.userRepositoryMock.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(this.jwtServiceMock.generateToken(user)).thenReturn("mockJwtToken");

        // Act
        AuthenticationResponse response = this.sut.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("mockJwtToken", response.getAccessToken());
        verify(this.authenticationManagerMock).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(this.userRepositoryMock).findByEmail("user@example.com");
        verify(this.jwtServiceMock).generateToken(user);

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
    void Authenticate_Should_Throw_InvalidCredentialsException_When_Database_Fetching_Throws() {
        // Arrange
        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        AuthenticateRequest request = new AuthenticateRequest("user@example.com", "wrongPassword");

        when(this.userRepositoryMock.findByEmail("user@example.com")).thenThrow(InvalidCredentialsException.class);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> this.sut.authenticate(request));
        assertEquals(1, appender.getLogs().size(), "Expected 1 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Authenticating user with email: " + request.getEmail()
        );
    }

    @Test
    void Authenticate_Should_Throw_EmailNotConfirmedException_When_User_Email_Not_Confirmed() {
        // Arrange
        String email = "user@example.com";
        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        AuthenticateRequest request = new AuthenticateRequest(email, "password");
        User user = User.builder()
                .email(email)
                .encryptedPassword("encryptedPassword")
                .emailConfirmed(false).build();

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailNotConfirmedException.class, () -> this.sut.authenticate(request));
        verifyNoInteractions(this.authenticationManagerMock, this.jwtServiceMock);
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
                "User with email: " + email + " has not confirmed their email"
        );
    }

    @Test
    void Authenticate_Should_Throw_InvalidCredentialsException_When_Credentials_Are_Invalid() {
        // Arrange
        String email = "user@example.com";
        TestLogAppender appender = LoggerTestUtil.captureLogs(AuthenticationService.class);
        AuthenticateRequest request = new AuthenticateRequest(email, "wrongPassword");
        User user = User.builder()
                .email(email)
                .encryptedPassword("encryptedPassword")
                .emailConfirmed(true).build();

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));
        when(this.authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> this.sut.authenticate(request));
        verify(this.authenticationManagerMock).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(this.jwtServiceMock);
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

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());
        when(this.passwordEncoderMock.encode(password)).thenReturn("encryptedPassword");
        when(this.jwtServiceMock.generateToken(any(User.class))).thenReturn("mockJwtToken");

        // Act
        User user = sut.register(request);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("encryptedPassword", user.getPassword());
        verify(this.userRepositoryMock).save(any(User.class));
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

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> this.sut.register(request));
        verify(this.userRepositoryMock, never()).save(any(User.class));
        verifyNoInteractions(this.jwtServiceMock);
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

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());
        when(this.passwordEncoderMock.encode(password)).thenReturn("encryptedPassword");
        doThrow(RuntimeException.class).when(this.userRepositoryMock).save(any(User.class));

        // Act & Assert
        assertThrows(DatabaseOperationException.class, () -> this.sut.register(request));
        verify(this.userRepositoryMock).save(any(User.class));
        verifyNoInteractions(this.jwtServiceMock);
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
