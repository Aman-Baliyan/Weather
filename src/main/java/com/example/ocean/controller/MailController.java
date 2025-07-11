package com.example.ocean.controller;

import com.example.ocean.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private EmailService emailService;


    // api to send mail to the required use
    @PostMapping("/send")
    public String sendMail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body
    ) {

        emailService.sendSimpleEmail(to, subject, body);
        return "Email sent to " + to;
    }
}
