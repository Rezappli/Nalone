package com.example.nalone;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Evenement {

    private int id;
    private String nom;
    private String description;
    private String adresse;
    private String ville;
    private Visibilite visibilite;
    private List<String> membre_inscrits;
    private List<String> membre_en_attente;


    public Evenement(int id, String nom, String description, String adresse, String ville, Visibilite visibilite,
                     List<String> membre_inscrits, List<String> membre_en_attente){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.adresse = adresse;
        this.ville = ville;
        this.visibilite = visibilite;
        this.membre_inscrits = membre_inscrits;
        this.membre_en_attente = membre_en_attente;
        Log.w("Evenement", "Nom:"+ nom);
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setVisibilite(Visibilite visibilite) {
        this.visibilite = visibilite;
    }

    public void setMembre_inscrits(List<String> membre_inscrits) {
        this.membre_inscrits = membre_inscrits;
    }

    public void setMembre_en_attente(List<String> membre_en_attente) {
        this.membre_en_attente = membre_en_attente;
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

    public List<String> getMembre_inscrits() {
        return membre_inscrits;
    }

    public List<String> getMembre_en_attente() {
        return membre_en_attente;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
