package com.example.nalone;

import android.graphics.Bitmap;

import com.example.nalone.util.Constants;

import java.util.Arrays;

public class UserData {
    public String sexe;
    public String nom;
    public String prenom;
    public String ville;
    public String adresse;
    public String date;
    public String numero;
    public String mail;
    public String pass;
    public String[] centreInterets;
    public String cursus;
    public String description;
    public String nbCreate;
    public String nbParticipate;
    public String demande_amis_recu;
    public String demande_amis_envoye;
    public String amis;

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

    public UserData(){}

    public UserData(String sexe, String nom, String prenom, String ville, String adresse, String date, String numero,String adresseMail, String pass, String cursus, String[] centreInterets, String description){
        this.sexe = sexe;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.adresse = adresse;
        this.date = date;
        this.numero = numero;
        this.mail = adresseMail.replace(".", ",");
        this.pass = pass;
        this.cursus = cursus;
        this.centreInterets = centreInterets;
        this.description = description;
        this.nbCreate = "0";
        this.nbParticipate = "0";
        this.demande_amis_envoye = "";
        this.demande_amis_recu = "";
        this.amis = "";
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
        return mail;
    }

    public void setAdresseMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNbCreate() {
        return nbCreate;
    }

    public void setNbCreate(String nbCreate) {
        this.nbCreate = nbCreate;
    }

    public String getNbParticipate() {
        return nbParticipate;
    }

    public void setNbParticipate(String nbParticipate) {
        this.nbParticipate = nbParticipate;
    }

    public void setDemande_amis_recu(String demande_amis_recu) {
        this.demande_amis_recu = demande_amis_recu;
    }

    public void setDemande_amis_envoye(String demande_amis_envoye) {
        this.demande_amis_envoye = demande_amis_envoye;
    }

    public String getDemande_amis_recu(){
        return demande_amis_recu;
    }

    public String getDemande_amis_envoye(){
        return demande_amis_envoye;
    }

}
