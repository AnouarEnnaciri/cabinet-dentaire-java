package com.cabinet.model;

public class Prescription {
    private int id;
    private long  consultationId;
    private String medicamentNom;
    private String posologie;

    public Prescription() {}

    public Prescription(String medicamentNom, String posologie) {
        this.medicamentNom = medicamentNom;
        this.posologie = posologie;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public long getConsultationId() { return consultationId; }
    public void setConsultationId(long consultationId) { this.consultationId = consultationId; }
    public String getMedicamentNom() { return medicamentNom; }
    public void setMedicamentNom(String medicamentNom) { this.medicamentNom = medicamentNom; }
    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }
}