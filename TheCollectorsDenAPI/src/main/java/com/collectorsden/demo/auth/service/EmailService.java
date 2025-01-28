package com.collectorsden.demo.auth.service;

import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.config.security.JwtService;
import com.collectorsden.demo.exception.auth.EmailNotRegisteredException;
import com.collectorsden.demo.exception.auth.TokenExpiredException;
import com.collectorsden.demo.exception.auth.TokenInvalidException;
import com.collectorsden.demo.model.ConfirmationToken;
import com.collectorsden.demo.model.User;
import com.collectorsden.demo.repository.ConfirmationTokenRepository;
import com.collectorsden.demo.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final static String EMAIL_CONFIRMATION_SUBJECT = "Confirm your TCD account";

    public AuthenticationResponse confirmEmail(String token) {
        logger.info("Confirming email with token: {}", token);

        ConfirmationToken confirmationToken = this.confirmationTokenRepository.findByToken(token)
                .orElseThrow(TokenInvalidException::new);

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.error("Confirmation token expired");
            throw new TokenExpiredException();
        }

        User user = confirmationToken.getUser();
        user.setEmailConfirmed(true);
        this.confirmationTokenRepository.delete(confirmationToken);

        String jwtToken = this.jwtService.generateToken(user);

        logger.info("User with email: {} has successfully confirmed his email", user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public void resendConfirmationEmail(String email) {
        logger.info("Resending confirmation email for : {}", email);

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    var msg = "User with email " + email + " is not registered.";
                    logger.error(msg);
                    return new EmailNotRegisteredException(email);
                });

        if (!user.isEmailConfirmed()) {
            this.confirmationTokenRepository.deleteAllByUser(user);

            this.sendConfirmationEmail(user);

            logger.info("Confirmation email successfully resent for : {}", email);
        }
    }

    public void sendConfirmationEmail(User user) {
        logger.info("Sending confirmation email for : {}", user.getEmail());

        String token = generateConfirmationToken(user);
        String confirmationUrl = "http://localhost:4200/confirm-email?token=" + token;

        sendEmail(
                user.getEmail(),
                "<p>Please confirm your email by clicking the link below:</p>"
                        + "<a href='" + confirmationUrl + "'>Confirm Email</a>"
        );
        logger.info("Confirmation email successfully sent for : {}", user.getEmail());
    }

    private String generateConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusHours(2));
        confirmationToken.setUser(user);

        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    private void sendEmail(String to, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("no-reply@thecollectorsden.com");
            helper.setTo(to);
            helper.setSubject(EMAIL_CONFIRMATION_SUBJECT);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
