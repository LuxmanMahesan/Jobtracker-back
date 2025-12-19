package com.jobtracker.jobtrackerbackend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerEmail(String destinataire, String sujet, String texte) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(texte);
        message.setFrom("evalux.casino@gmail.com");

        mailSender.send(message);

        System.out.println("📩 Mail envoyé à " + destinataire);
    }
}
