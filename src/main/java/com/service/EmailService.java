package com.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token) {
        String verifyLink = baseUrl + "/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your IU Bookstore account");
        message.setText(
                "Welcome to IU Bookstore!\n\n" +
                "Please click the link below to verify your email:\n" +
                verifyLink + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you did not create this account, please ignore this email."
        );

        mailSender.send(message);
    }
}