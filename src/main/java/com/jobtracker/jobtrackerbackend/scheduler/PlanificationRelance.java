package com.jobtracker.jobtrackerbackend.scheduler;

import com.jobtracker.jobtrackerbackend.model.Candidature;
import com.jobtracker.jobtrackerbackend.repo.CandidatureRepository;
import com.jobtracker.jobtrackerbackend.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PlanificationRelance {

    private final CandidatureRepository repo;
    private final EmailService emailService;

    public PlanificationRelance(CandidatureRepository repo, EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;
    }

    // ⏰ tous les jours à 09:00
    @Scheduled(cron = "0 0 9 * * *")
    public void envoyerRelances() {

        LocalDate aujourdHui = LocalDate.now();

        List<Candidature> liste = repo.findAll();

        liste.stream()
                .filter(c -> Boolean.TRUE.equals(c.getRelanceActive()))
                .filter(c -> c.getDateRelance() != null)
                .filter(c -> c.getDateRelance().isEqual(aujourdHui))
                .forEach(c -> {
                    emailService.envoyerEmail(
                            c.getUtilisateur().getEmail(),
                            "📬 Relance candidature : " + c.getTitrePoste(),
                            "Aujourd’hui est la date prévue de relance pour votre candidature :\n\n" +
                                    "📌 Poste : " + c.getTitrePoste() + "\n" +
                                    "🏢 Entreprise : " + c.getEntreprise() + "\n\n" +
                                    "Vous devriez envoyer un mail de relance à l’entreprise aujourd’hui."
                    );

                    System.out.println("✉️ Relance envoyée à " + c.getUtilisateur().getEmail());
                });

        System.out.println("✔️ Job planifié : relances vérifiées");
    }
}
