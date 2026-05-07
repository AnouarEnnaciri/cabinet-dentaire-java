package com.cabinet.model;

public class Prescription {
    private Long id;           // Changed to Long object
    private Long consultationId;  // Changed to Long object
    private String medicamentNom;
    private String posologie;

    public Prescription() {}

    public Prescription(String medicamentNom, String posologie) {
        this.medicamentNom = medicamentNom;
        this.posologie = posologie;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }

    public String getMedicamentNom() { return medicamentNom; }
    public void setMedicamentNom(String medicamentNom) { this.medicamentNom = medicamentNom; }

    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }
}