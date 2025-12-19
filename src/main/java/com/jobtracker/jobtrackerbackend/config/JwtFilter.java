package com.jobtracker.jobtrackerbackend.config;

import com.jobtracker.jobtrackerbackend.model.Utilisateur;
import com.jobtracker.jobtrackerbackend.repo.UtilisateurRepository;
import com.jobtracker.jobtrackerbackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;

    public JwtFilter(JwtService jwtService, UtilisateurRepository utilisateurRepository) {
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest requete,
            HttpServletResponse reponse,
            FilterChain chaine
    ) throws ServletException, IOException {

        String header = requete.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chaine.doFilter(requete, reponse);
            return;
        }

        String token = header.substring(7);

        if (!jwtService.tokenValide(token)) {
            chaine.doFilter(requete, reponse);
            return;
        }

        String email = jwtService.extraireEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElse(null);

            if (utilisateur != null) {
                var auth = new UsernamePasswordAuthenticationToken(
                        utilisateur.getEmail(),
                        null,
                        List.of() // tu pourras mettre des rôles plus tard
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(requete));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chaine.doFilter(requete, reponse);
    }
}
