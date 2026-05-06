package com.cabinet.model;

public class Acte extends BaseEntity {
    private String code;
    private String libelle;
    private String categorie;
    private Double prixBase;

    public Acte() {}

    public Acte(String code, String libelle, String categorie, Double prixBase) {
        this.code = code;
        this.libelle = libelle;
        this.categorie = categorie;
        this.prixBase = prixBase;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public Double getPrixBase() { return prixBase; }
    public void setPrixBase(Double prixBase) { this.prixBase = prixBase; }
}