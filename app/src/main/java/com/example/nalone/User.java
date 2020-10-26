package com.example.nalone;

import android.graphics.Bitmap;

import com.example.nalone.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    public String sexe;
    public String nom;
    public String prenom;
    public String ville;
    public String numero;
    public String mail;
    public List<String> centreInterets;
    public String cursus;
    public String description;
    public String nbCreation;
    public String nbParticipation;
    public List<String> demande_amis_recu;
    public List<String> demande_amis_envoye;
    public List<String> amis;

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public List<String> getCentreInterets() {
        return centreInterets;
    }

    public void setCentreInterets(List<String> centreInterets) {
        this.centreInterets = centreInterets;
    }

    public String getCursus() {
        return cursus;
    }

    public void setCursus(String cursus) {
        this.cursus = cursus;
    }

    public User(){}

    public User(String nom, String prenom, String sexe, String ville,
                String numero, String mail, String cursus, List<String> centreInterets,
                String description){
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.ville = ville;
        this.numero = numero;
        this.mail = mail;
        this.cursus = cursus;
        this.centreInterets = centreInterets;
        this.description = description;
        this.nbCreation = "0";
        this.nbParticipation = "0";
        this.demande_amis_envoye = new ArrayList<>();
        this.demande_amis_recu = new ArrayList<>();
        this.amis = new ArrayList<>();

        amis.add("-1");
        demande_amis_recu.add("-1");
        demande_amis_envoye.add("-1");
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMail() {
        return mail;
    }

    public void setAdresseMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNbCreation() {
        return nbCreation;
    }

    public void setNbCreate(String nbCreate) {
        this.nbCreation = nbCreate;
    }

    public String getNbParticipation() {
        return nbParticipation;
    }

    public void setNbParticipate(String nbParticipate) {
        this.nbParticipation = nbParticipate;
    }

    public void setDemande_amis_recu(List<String> demande_amis_recu) {
        this.demande_amis_recu = demande_amis_recu;
    }

    public void setDemande_amis_envoye(List<String> demande_amis_envoye) {
        this.demande_amis_envoye = demande_amis_envoye;
    }

    public List<String> getDemande_amis_recu(){
        return demande_amis_recu;
    }

    public List<String> getDemande_amis_envoye(){
        return demande_amis_envoye;
    }

    public List<String> getAmis() {
        return amis;
    }

    public void setAmis(List<String> amis) {
        this.amis = amis;
    }
}
