package com.collectorsden.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    private static final int GMAIL_SMTP_PORT = 587;

    private final String host;
    private final String user;
    private final String password;

    public EmailConfig(
            @Value("${spring.mail.host:}") String hostProp,
            @Value("${spring.mail.username:}") String userProp,
            @Value("${spring.mail.password:}") String passwordProp
    ) {
        // Fallback to environment variables if properties are empty :)
        this.host = !hostProp.isEmpty() ? hostProp : System.getenv("MAIL_HOST");
        this.user = !userProp.isEmpty() ? userProp : System.getenv("MAIL_USERNAME");
        this.password = !passwordProp.isEmpty() ? passwordProp : System.getenv("MAIL_PASSWORD");

        if (this.host == null || this.host.isEmpty()) {
            throw new IllegalStateException("Email host is not set. Check environment variables or application properties.");
        }
        if (this.user == null || this.user.isEmpty()) {
            throw new IllegalStateException("Email username is not set. Check environment variables or application properties.");
        }
        if (this.password == null || this.password.isEmpty()) {
            throw new IllegalStateException("Email password is not set. Check environment variables or application properties.");
        }
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(GMAIL_SMTP_PORT);
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
