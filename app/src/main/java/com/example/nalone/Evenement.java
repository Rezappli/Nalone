package com.example.nalone;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nalone.util.Constants.user_mail;
import static com.example.nalone.util.Constants.user_id;

public class Evenement {

    private int id;
    private String nom;
    private String description;
    private String adresse;
    private String ville;
    private Visibilite visibilite;
    private String proprietaire;
    private List<String> membres_inscrits;
    private List<String> membres_en_attente;

    public List<String> getMembres_inscrits() {
        return membres_inscrits;
    }

    public List<String> getMembres_en_attente() {
        return membres_en_attente;
    }

    private BitmapDescriptor couleur_icone = null;

    public Evenement() {}


    public Evenement(int id, String nom, String description, String adresse, String ville, Visibilite visibilite,
                     String proprietaire, List<String> membres_inscrits, List<ItemPerson> membres_en_attente){
        this.nom = nom;
        this.description = description;
        this.adresse = adresse;
        this.ville = ville;
        this.visibilite = visibilite;
        this.proprietaire = proprietaire;
        this.membres_inscrits = membres_inscrits;
        this.membres_en_attente = new ArrayList<>();
        for(int i = 0; i < membres_en_attente.size(); i++) {
            this.membres_en_attente.add(membres_en_attente.get(i).getId()+"");
        }
        this.id = id;
        if(visibilite.equals(Visibilite.PRIVE)){
            if(proprietaire.equalsIgnoreCase(user_id)) {
                couleur_icone = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{
                couleur_icone = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
        }else{
            couleur_icone = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        Log.w("Map", "Création d'un evenement : " + couleur_icone.toString());
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
        if(visibilite.equals(Visibilite.PRIVE)){
            if(proprietaire.equalsIgnoreCase(user_id)) {
                couleur_icone = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{
                couleur_icone = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
        }else{
            couleur_icone = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
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

    public void setId(int id){
        this.id = id;
    }

}
