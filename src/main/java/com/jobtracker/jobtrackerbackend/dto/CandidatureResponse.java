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

    public String notes;

    public Integer joursAvantRelance;
    public Boolean relanceActive;
}
