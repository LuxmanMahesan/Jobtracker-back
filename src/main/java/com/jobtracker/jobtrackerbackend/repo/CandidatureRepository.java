package com.jobtracker.jobtrackerbackend.repo;

import com.jobtracker.jobtrackerbackend.model.Candidature;
import com.jobtracker.jobtrackerbackend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findAllByUtilisateur(Utilisateur utilisateur);

    Optional<Candidature> findByIdAndUtilisateur(Long id, Utilisateur utilisateur);
}
