package com.cabinet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RendezVous extends BaseEntity {
    private Long patientId;
    private Long medecinId;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private String statut;
    private String patientNom;
    private String patientTelephone;
    private LocalDateTime dateDemande;

    public RendezVous() {}

    public RendezVous(Long patientId, Long medecinId, LocalDate date, LocalTime heure, String motif, String statut) {
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.statut = statut;
    }

    // Getters and Setters
    public String getPatientNom() { return patientNom; }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }

    public String getPatientTelephone() { return patientTelephone; }
    public void setPatientTelephone(String patientTelephone) { this.patientTelephone = patientTelephone; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}