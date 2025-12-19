package com.jobtracker.jobtrackerbackend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key cleSignature;
    private final long dureeMs;

    public JwtService(
            @Value("${securite.jwt.secret}") String secret,
            @Value("${securite.jwt.duree-ms:86400000}") long dureeMs
    ) {
        this.cleSignature = Keys.hmacShaKeyFor(secret.getBytes());
        this.dureeMs = dureeMs;
    }

    public String genererToken(String email) {
        Date maintenant = new Date();
        Date expiration = new Date(maintenant.getTime() + dureeMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(maintenant)
                .setExpiration(expiration)
                .signWith(cleSignature, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraireEmail(String token) {
        return parser(token).getBody().getSubject();
    }

    public boolean tokenValide(String token) {
        try {
            parser(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parser(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(cleSignature)
                .build()
                .parseClaimsJws(token);
    }
}
