package com.jobtracker.jobtrackerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "candidature")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private String entreprise;

    @Column(nullable = false)
    private String titrePoste;

    private String typeContrat;
    private String lienAnnonce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCandidature statut;

    private LocalDate dateEnvoi;
    private LocalDate dateLimite;

    // 📌 Calculée par le backend : dateEnvoi + joursAvantRelance
    private LocalDate dateRelance;

    @Column(columnDefinition = "text")
    private String notes;

    private Integer joursAvantRelance;
    private Boolean relanceActive;
}
