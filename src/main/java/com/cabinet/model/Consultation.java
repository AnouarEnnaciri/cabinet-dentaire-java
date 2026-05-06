package com.cabinet.model;

import java.time.LocalDate;

public class Consultation extends BaseEntity {
    private Long dossierId;
    private Long medecinId;
    private LocalDate date;
    private String diagnostic;
    private String traitement;
    private String notes;

    public Consultation() {}

    public Consultation(Long dossierId, LocalDate date, String diagnostic, String traitement, String notes) {
        this.dossierId = dossierId;
        this.date = date;
        this.diagnostic = diagnostic;
        this.traitement = traitement;
        this.notes = notes;
    }

    public Long getDossierId() { return dossierId; }
    public void setDossierId(Long dossierId) { this.dossierId = dossierId; }

    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}