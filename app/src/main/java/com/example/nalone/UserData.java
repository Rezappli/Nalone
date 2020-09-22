package com.example.nalone;

import java.util.Arrays;

public class UserData {
    private String sexe;
    private String nom;
    private String prenom;
    private String ville;
    private String adresse;
    private String date;
    private String numero;
    private String adresseMail;
    private String pass;
    private String[] centreInterets;
    private String cursus;

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCentreInterets() {
        return Arrays.toString(centreInterets);
    }

    public void setCentreInterets(String[] centreInterets) {
        this.centreInterets = centreInterets;
    }

    public String getCursus() {
        return cursus;
    }

    public void setCursus(String cursus) {
        this.cursus = cursus;
    }

    public UserData(){
        this(null, null, null, null, null, null, null, null, null, null, null);
    }

    public UserData(String sexe, String nom, String prenom, String ville, String adresse, String date, String numero,String adresseMail, String pass, String cursus, String[] centreInterets){
        this.sexe = sexe;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.adresse = adresse;
        this.date = date;
        this.numero = numero;
        this.adresseMail = adresseMail.replace(".", ",");
        this.pass = pass;
        this.cursus = cursus;
        this.centreInterets = centreInterets;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
