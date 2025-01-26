package com.collectorsden.demo.auth.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    private final static String EMAIL_CONFIRMATION_SUBJECT = "Confirm your TCD account";

    public void sendEmail(String to, String body) {
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
