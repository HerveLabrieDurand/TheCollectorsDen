package com.collectorsden.demo.auth.service;

import ch.qos.logback.classic.Level;
import com.collectorsden.demo.exception.auth.EmailAlreadyConfirmedException;
import com.collectorsden.demo.exception.auth.EmailNotRegisteredException;
import com.collectorsden.demo.exception.auth.TokenExpiredException;
import com.collectorsden.demo.exception.auth.TokenInvalidException;
import com.collectorsden.demo.model.ConfirmationToken;
import com.collectorsden.demo.model.User;
import com.collectorsden.demo.repository.ConfirmationTokenRepository;
import com.collectorsden.demo.repository.UserRepository;
import com.collectorsden.demo.util.LoggerTestUtil;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSenderMock;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private MimeMessage mimeMessageMock;

    @InjectMocks
    private EmailService sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSenderMock.createMimeMessage()).thenReturn(mimeMessageMock);
    }

    @Test
    void ConfirmEmail_Should_Confirm_Email_For_User_If_Token_Valid_And_User_Email_Not_Confirmed() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        String requestToken = "requestToken";
        String email = "email@collectorsden.com";
        User user = User.builder()
                .emailConfirmed(false)
                .email(email)
                .build();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(requestToken)
                .expiresAt(LocalDateTime.now().plusHours(2))
                .user(user).build();

        when(this.confirmationTokenRepositoryMock.findByToken(requestToken)).thenReturn(Optional.of(confirmationToken));

        // Act
        this.sut.confirmEmail(requestToken);

        // Assert
        verify(this.confirmationTokenRepositoryMock).delete(confirmationToken);
        assertTrue(user.isEmailConfirmed());
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Confirming email with token: " + requestToken
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.INFO,
                "User with email: " + email + " has successfully confirmed his email"
        );
    }

    @Test
    void ConfirmEmail_Should_Throw_TokenInvalidException_When_Token_Is_Not_Found_In_Database() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        String requestToken = "requestToken";

        when(this.confirmationTokenRepositoryMock.findByToken(requestToken))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenInvalidException.class, () -> this.sut.confirmEmail(requestToken));
        verify(this.confirmationTokenRepositoryMock, never()).delete(any());
        assertEquals(1, appender.getLogs().size(), "Expected 1 log event.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Confirming email with token: " + requestToken
        );
    }

    @Test
    void ConfirmEmail_Should_Throw_TokenExpiredException_When_Token_Is_Expired() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        String requestToken = "requestToken";
        ConfirmationToken token = ConfirmationToken.builder()
                .expiresAt(LocalDateTime.now().plusMinutes(-1)).build();

        when(this.confirmationTokenRepositoryMock.findByToken(requestToken))
                .thenReturn(Optional.of(token));

        // Act & Assert
        assertThrows(TokenExpiredException.class, () -> this.sut.confirmEmail(requestToken));
        verify(this.confirmationTokenRepositoryMock, never()).delete(any());
        assertEquals(2, appender.getLogs().size(), "Expected 1 log event.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Confirming email with token: " + requestToken
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "Confirmation token expired"
        );
    }

    @Test
    void ConfirmEmail_Should_Throw_EmailAlreadyConfirmedException_When_User_Email_Already_Confirmed() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        String requestToken = "requestToken";
        String email = "email@collectorsden.com";
        User user = User.builder()
                .emailConfirmed(true)
                .email(email)
                .build();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(requestToken)
                .expiresAt(LocalDateTime.now().plusHours(2))
                .user(user).build();

        when(this.confirmationTokenRepositoryMock.findByToken(requestToken)).thenReturn(Optional.of(confirmationToken));

        // Act & Assert
        assertThrows(EmailAlreadyConfirmedException.class, () -> this.sut.confirmEmail(requestToken));
        verify(this.confirmationTokenRepositoryMock, never()).delete(any());
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Confirming email with token: " + requestToken
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "User with email: " + email + " has already confirmed their email"
        );
    }

    @Test
    void ResendConfirmationEmail_Should_Resend_If_User_Exists_And_Not_Confirmed() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        String email = "email@collectorsden.com";
        User user = User.builder()
                .email(email)
                .emailConfirmed(false)
                .build();

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        this.sut.resendConfirmationEmail(email);

        // Assert
        verify(this.confirmationTokenRepositoryMock).deleteAllByUser(user);
        assertEquals(4, appender.getLogs().size(), "Expected 4 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Resending confirmation email for : " + email
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.INFO,
                "Sending confirmation email for : " + email
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                2,
                Level.INFO,
                "Confirmation email successfully sent for : " + email
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                3,
                Level.INFO,
                "Confirmation email successfully resent for : " + email
        );
    }

    @Test
    void ResendConfirmationEmail_Should_Throw_EmailNotRegisteredException_If_User_Does_Not_Exist() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        String email = "email@collectorsden.com";

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmailNotRegisteredException.class, () -> this.sut.resendConfirmationEmail(email));
        assertEquals(2, appender.getLogs().size(), "Expected 2 log event.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Resending confirmation email for : " + email
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.ERROR,
                "User with email " + email + " is not registered."
        );
    }

    @Test
    void ResendConfirmationEmail_Should_Throw_EmailAlreadyConfirmedException_If_Email_Is_Already_Confirmed() {
        // Arrange
        String email = "email@collectorsden.com";
        User user = User.builder()
                .email(email)
                .emailConfirmed(true)
                .build();

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailAlreadyConfirmedException.class, () -> this.sut.resendConfirmationEmail(email));
        verify(this.confirmationTokenRepositoryMock, never()).deleteAllByUser(user);
    }

    @Test
    void ResendConfirmationEmail_Should_Delete_Previous_Tokens_Before_Resending_Email() {
        // Arrange
        String email = "email@collectorsden.com";
        User user = User.builder()
                .email(email)
                .emailConfirmed(false)
                .build();

        when(this.userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        this.sut.resendConfirmationEmail(email);

        // Assert
        verify(this.confirmationTokenRepositoryMock).deleteAllByUser(user);
    }

    @Test
    void SendConfirmationEmail_Should_Log_Messages_When_Email_Is_Sent() {
        // Arrange
        LoggerTestUtil.TestLogAppender appender = LoggerTestUtil.captureLogs(EmailService.class);
        User user = User.builder()
                .email("email@collectorsden.com")
                .build();

        // Act
        this.sut.sendConfirmationEmail(user);

        // Assert
        assertEquals(2, appender.getLogs().size(), "Expected 2 log events.");
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                0,
                Level.INFO,
                "Sending confirmation email for : " + user.getEmail()
        );
        LoggerTestUtil.assertLog(
                appender.getLogs(),
                1,
                Level.INFO,
                "Confirmation email successfully sent for : " + user.getEmail()
        );
    }
}
