package com.jobtracker.jobtrackerbackend.controller;


import com.jobtracker.jobtrackerbackend.dto.AuthResponse;
import com.jobtracker.jobtrackerbackend.dto.LoginRequest;
import com.jobtracker.jobtrackerbackend.dto.RegisterRequest;
import com.jobtracker.jobtrackerbackend.model.Utilisateur;
import com.jobtracker.jobtrackerbackend.service.JwtService;
import com.jobtracker.jobtrackerbackend.service.UtilisateurService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurService service;
    private final JwtService jwt;

    public AuthController(UtilisateurService service, JwtService jwt) {
        this.service = service;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        service.register(req);
        return "Inscription réussie !";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        Utilisateur u = service.login(req);
        String token = jwt.genererToken(u.getEmail());
        return new AuthResponse(token);
    }
}
