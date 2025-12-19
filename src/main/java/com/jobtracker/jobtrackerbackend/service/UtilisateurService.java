package com.jobtracker.jobtrackerbackend.service;

import com.jobtracker.jobtrackerbackend.dto.LoginRequest;
import com.jobtracker.jobtrackerbackend.dto.RegisterRequest;
import com.jobtracker.jobtrackerbackend.exception.EmailDejaUtiliseException;
import com.jobtracker.jobtrackerbackend.model.Utilisateur;
import com.jobtracker.jobtrackerbackend.repo.UtilisateurRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repo;
    private final BCryptPasswordEncoder encodeur;

    public UtilisateurService(UtilisateurRepository repo, BCryptPasswordEncoder encodeur) {
        this.repo = repo;
        this.encodeur = encodeur;
    }

    public void register(RegisterRequest req) {
        if (repo.findByEmail(req.email).isPresent()) {
            throw new EmailDejaUtiliseException("Email déjà utilisé");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(req.nom);
        u.setPrenom(req.prenom);
        u.setEmail(req.email);
        u.setMotDePasse(encodeur.encode(req.motDePasse));

        repo.save(u);
    }

    public Utilisateur login(LoginRequest req) {
        Utilisateur u = repo.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("Email incorrect"));

        if (!encodeur.matches(req.motDePasse, u.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return u;
    }
}
