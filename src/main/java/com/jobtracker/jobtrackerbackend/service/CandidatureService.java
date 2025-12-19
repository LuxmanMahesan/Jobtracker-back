package com.jobtracker.jobtrackerbackend.service;

import com.jobtracker.jobtrackerbackend.dto.CandidatureRequest;
import com.jobtracker.jobtrackerbackend.dto.CandidatureResponse;
import com.jobtracker.jobtrackerbackend.model.Candidature;
import com.jobtracker.jobtrackerbackend.model.StatutCandidature;
import com.jobtracker.jobtrackerbackend.model.Utilisateur;
import com.jobtracker.jobtrackerbackend.repo.CandidatureRepository;
import com.jobtracker.jobtrackerbackend.repo.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatureService {

    private final CandidatureRepository candidatureRepo;
    private final UtilisateurRepository utilisateurRepo;

    public CandidatureService(CandidatureRepository candidatureRepo, UtilisateurRepository utilisateurRepo) {
        this.candidatureRepo = candidatureRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    private Utilisateur utilisateurCourant(String email) {
        return utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    private static CandidatureResponse versResponse(Candidature c) {
        CandidatureResponse r = new CandidatureResponse();
        r.id = c.getId();
        r.entreprise = c.getEntreprise();
        r.titrePoste = c.getTitrePoste();
        r.typeContrat = c.getTypeContrat();
        r.lienAnnonce = c.getLienAnnonce();
        r.statut = c.getStatut();
        r.dateEnvoi = c.getDateEnvoi();
        r.dateLimite = c.getDateLimite();
        r.notes = c.getNotes();
        r.joursAvantRelance = c.getJoursAvantRelance();
        r.relanceActive = c.getRelanceActive();
        return r;
    }

    public List<CandidatureResponse> lister(String email) {
        Utilisateur u = utilisateurCourant(email);
        return candidatureRepo.findAllByUtilisateur(u).stream()
                .map(CandidatureService::versResponse)
                .toList();
    }

    public CandidatureResponse creer(String email, CandidatureRequest req) {
        Utilisateur u = utilisateurCourant(email);

        Candidature c = new Candidature();
        c.setUtilisateur(u);

        c.setEntreprise(req.entreprise);
        c.setTitrePoste(req.titrePoste);
        c.setTypeContrat(req.typeContrat);
        c.setLienAnnonce(req.lienAnnonce);

        c.setStatut(req.statut != null ? req.statut : StatutCandidature.A_POSTULER);

        c.setDateEnvoi(req.dateEnvoi);
        c.setDateLimite(req.dateLimite);
        c.setNotes(req.notes);

        c.setJoursAvantRelance(req.joursAvantRelance);
        c.setRelanceActive(req.relanceActive != null ? req.relanceActive : Boolean.FALSE);

        return versResponse(candidatureRepo.save(c));
    }

    public CandidatureResponse modifier(String email, Long id, CandidatureRequest req) {
        Utilisateur u = utilisateurCourant(email);

        Candidature c = candidatureRepo.findByIdAndUtilisateur(id, u)
                .orElseThrow(() -> new RuntimeException("Candidature introuvable ou accès interdit"));

        c.setEntreprise(req.entreprise);
        c.setTitrePoste(req.titrePoste);
        c.setTypeContrat(req.typeContrat);
        c.setLienAnnonce(req.lienAnnonce);
        c.setStatut(req.statut != null ? req.statut : c.getStatut());

        c.setDateEnvoi(req.dateEnvoi);
        c.setDateLimite(req.dateLimite);
        c.setNotes(req.notes);

        c.setJoursAvantRelance(req.joursAvantRelance);
        c.setRelanceActive(req.relanceActive);

        return versResponse(candidatureRepo.save(c));
    }

    public void supprimer(String email, Long id) {
        Utilisateur u = utilisateurCourant(email);

        Candidature c = candidatureRepo.findByIdAndUtilisateur(id, u)
                .orElseThrow(() -> new RuntimeException("Candidature introuvable ou accès interdit"));

        candidatureRepo.delete(c);
    }
}
