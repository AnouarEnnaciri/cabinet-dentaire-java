package com.cabinet.model;

import java.time.LocalDateTime;

public class Antecedent extends BaseEntity {
    private Long patientId;
    private String type;     // "allergie", "maladie", "traitement"
    private String description;
    private LocalDateTime dateCreation;

    public Antecedent() {}

    public Antecedent(Long patientId, String type, String description, LocalDateTime dateCreation) {
        this.patientId = patientId;
        this.type = type;
        this.description = description;
        this.dateCreation = dateCreation;
    }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}