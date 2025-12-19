package com.jobtracker.jobtrackerbackend.controller;

import com.jobtracker.jobtrackerbackend.service.EmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestMailController {

    private final EmailService emailService;

    public TestMailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/mail")
    public String envoyerMailTest(@RequestParam String dest) {

        emailService.envoyerEmail(
                dest,
                "TEST SMTP OK",
                "Ceci est un email de test envoyé via SMTP Gmail 🎉"
        );

        return "Email envoyé à " + dest;
    }
}
