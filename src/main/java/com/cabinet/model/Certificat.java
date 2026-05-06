package com.cabinet.model;

import java.time.LocalDateTime;

public class Certificat {
    private int id;
    private int patientId;
    private String patientNom;
    private Integer consultationId;
    private String type;
    private LocalDateTime dateEmission;
    private String contenu;

    // Constructors
    public Certificat() {}

    public Certificat(int patientId, String patientNom, Integer consultationId, String type, String contenu) {
        this.patientId = patientId;
        this.patientNom = patientNom;
        this.consultationId = consultationId;
        this.type = type;
        this.contenu = contenu;
        this.dateEmission = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getPatientNom() { return patientNom; }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }

    public Integer getConsultationId() { return consultationId; }
    public void setConsultationId(Integer consultationId) { this.consultationId = consultationId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getDateEmission() { return dateEmission; }
    public void setDateEmission(LocalDateTime dateEmission) { this.dateEmission = dateEmission; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
}