package com.jobtracker.jobtrackerbackend.dto;

import com.jobtracker.jobtrackerbackend.model.StatutCandidature;

import java.time.LocalDate;

public class CandidatureRequest {
    public String entreprise;
    public String titrePoste;
    public String typeContrat;
    public String lienAnnonce;
    public StatutCandidature statut;

    public LocalDate dateEnvoi;
    public LocalDate dateLimite;

    // 📌 On NE demande pas la date de relance au frontend, elle est calculée côté backend
    public String notes;

    public Integer joursAvantRelance;
    public Boolean relanceActive;
}
