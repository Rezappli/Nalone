package com.example.nalone;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;

public class Evenement {

    private int id;
    private String nom;
    private String description;
    private String adresse;
    private String ville;
    private Visibilite visibilite;
    private String proprietaire;
    private BitmapDescriptor couleur_icone;


    public Evenement(int id, String nom, String description, String adresse, String ville, Visibilite visibilite,
                     String proprietaire){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.adresse = adresse;
        this.ville = ville;
        this.visibilite = visibilite;
        this.proprietaire = proprietaire;
        Log.w("Evenement", "Nom:"+ nom);
    }

    public int getId(){
        return id;
    }


    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public Visibilite getVisibilite() {
        return visibilite;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public BitmapDescriptor getCouleur_icone() {
        return couleur_icone;
    }

    public void setCouleur_icone(BitmapDescriptor couleur_icone) {
        this.couleur_icone = couleur_icone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
