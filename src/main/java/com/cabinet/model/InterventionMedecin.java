package com.cabinet.model;

public class InterventionMedecin extends BaseEntity {
    private Long consultationId;
    private Long acteId;
    private Integer numDent;
    private Integer quantite;
    private Double prixUnitaire;

    public InterventionMedecin() {}

    public InterventionMedecin(Long consultationId, Long acteId, Integer numDent, Integer quantite, Double prixUnitaire) {
        this.consultationId = consultationId;
        this.acteId = acteId;
        this.numDent = numDent;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }

    public Long getActeId() { return acteId; }
    public void setActeId(Long acteId) { this.acteId = acteId; }

    public Integer getNumDent() { return numDent; }
    public void setNumDent(Integer numDent) { this.numDent = numDent; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}