package com.cabinet.model;

public class Utilisateur extends BaseEntity{
    private String login;
    private String motDePasse;
    private String role;
    private String nom;
    private String email;

    public Utilisateur() {
        super();
    }

    public Utilisateur(String login, String motDePasse, String role, String nom, String email) {
        super();
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.nom = nom;
        this.email = email;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}