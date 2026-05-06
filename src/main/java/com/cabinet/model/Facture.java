package com.cabinet.model;

import java.time.LocalDateTime;

public class Facture extends BaseEntity {
    private Long patientId;
    private Long consultationId;
    private LocalDateTime dateFacture;
    private Double montantTotal;
    private Double montantPaye;
    private String statut; // EN_ATTENTE, PAYEE, ANNULEE

    public Facture() {}

    public Facture(Long patientId, Long consultationId, LocalDateTime dateFacture, Double montantTotal, Double montantPaye, String statut) {
        this.patientId = patientId;
        this.consultationId = consultationId;
        this.dateFacture = dateFacture;
        this.montantTotal = montantTotal;
        this.montantPaye = montantPaye;
        this.statut = statut;
    }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }

    public LocalDateTime getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDateTime dateFacture) { this.dateFacture = dateFacture; }

    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }

    public Double getMontantPaye() { return montantPaye; }
    public void setMontantPaye(Double montantPaye) { this.montantPaye = montantPaye; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}