package com.jobtracker.jobtrackerbackend.dto;

import com.jobtracker.jobtrackerbackend.model.StatutCandidature;

import java.time.LocalDate;

public class CandidatureResponse {
    public Long id;
    public String entreprise;
    public String titrePoste;
    public String typeContrat;
    public String lienAnnonce;
    public StatutCandidature statut;

    public LocalDate dateEnvoi;
    public LocalDate dateLimite;

    // 📌 Champ que le frontend utilisera pour l’affichage “Relance dans X jours”
    public LocalDate dateRelance;

    public String notes;

    public Integer joursAvantRelance;
    public Boolean relanceActive;
}
