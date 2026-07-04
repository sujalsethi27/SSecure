package com.project2.mailjwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

// this whole package is use to send the emails

@Service
@RequiredArgsConstructor
public class emailservice {

    private final JavaMailSender mailsender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;


    // method to send the welcome email
    public void sendwelcomeEmail(String name , String toEmail) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail);
    message.setTo(toEmail);
    message.setSubject("Welcome to out platform");
    message.setText("Hello "+name+",\n\nThanks for registering with us!\n\nRegards,\n SSecure team");
    System.out.println("FROM = " + fromEmail);
    System.out.println("TO = " + toEmail);
    mailsender.send(message);
    }


// method to send the reset otp email
public void sendResetOtpemail(String toEmail , String otp) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail);
    message.setTo(toEmail);
    message.setSubject("Password Reset OTP");
    message.setText("Your otp for resetting your password is "+otp+". Use this OTP to procced with the reset password");
    mailsender.send(message);
}

// method to send the otp to verify the account(email)
public void sendotpEmail(String toEmail , String otp) {
    SimpleMailMessage message = new SimpleMailMessage(); 
    message.setFrom(fromEmail);
    message.setTo(toEmail);
    message.setSubject("Account verification OTP");
    message.setText("Your OTP is "+otp+ ". Verfity your account using the OTP.");
    mailsender.send(message);       
}
}
