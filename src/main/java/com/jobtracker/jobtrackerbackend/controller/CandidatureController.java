package com.jobtracker.jobtrackerbackend.controller;

import com.jobtracker.jobtrackerbackend.dto.CandidatureRequest;
import com.jobtracker.jobtrackerbackend.dto.CandidatureResponse;
import com.jobtracker.jobtrackerbackend.service.CandidatureService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
@CrossOrigin(origins = "*")
public class CandidatureController {

    private final CandidatureService service;

    public CandidatureController(CandidatureService service) {
        this.service = service;
    }

    private String email(Authentication auth) {
        return auth.getName(); // si ton JwtFilter met l'email en principal
    }

    @GetMapping
    public List<CandidatureResponse> lister(Authentication auth) {
        return service.lister(email(auth));
    }

    @PostMapping
    public CandidatureResponse creer(Authentication auth, @RequestBody CandidatureRequest req) {
        return service.creer(email(auth), req);
    }

    @PutMapping("/{id}")
    public CandidatureResponse modifier(Authentication auth, @PathVariable Long id, @RequestBody CandidatureRequest req) {
        return service.modifier(email(auth), id, req);
    }

    @DeleteMapping("/{id}")
    public void supprimer(Authentication auth, @PathVariable Long id) {
        service.supprimer(email(auth), id);
    }
}
