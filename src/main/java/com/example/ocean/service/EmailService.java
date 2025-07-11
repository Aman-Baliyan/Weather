package com.example.ocean.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired//its bean is created by spring internally
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {

        //creating a mail message object to send
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println("trying to Mail Sent Successfully...");
        message.setFrom("teamhelix.nitjsr@gmail.com"); // same as configured in spring.mail.username
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Mail Sent Successfully...");
    }
}
