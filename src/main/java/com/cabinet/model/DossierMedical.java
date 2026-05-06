package com.cabinet.model;

import java.time.LocalDateTime;

public class DossierMedical extends BaseEntity {
    private Long patientId;
    private LocalDateTime dateCreation;

    public DossierMedical() {}

    public DossierMedical(Long patientId, LocalDateTime dateCreation) {
        this.patientId = patientId;
        this.dateCreation = dateCreation;
    }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}