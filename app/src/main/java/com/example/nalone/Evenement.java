package com.example.nalone;

import com.example.nalone.items.ItemPerson;
import com.example.nalone.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evenement {

    public int image;
    public int id;
    public String nom;
    public String description;
    public String adresse;
    public String ville;
    public Visibilite visibilite;
    public String proprietaire;
    public List<String> membres_inscrits;
    public List<String> membres_en_attente;
    public Date date;
    public String time;

    public Evenement() {}


    public Evenement(int image, int id, String nom, String description, String adresse, String ville,
                     Visibilite visibilite, String proprietaire, List<String> membres_inscrits,
                     List<ItemPerson> membres_en_attente, Date date, String time){
        this.image = image;
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
        this.date = date;
        this.time = time;
    }


    public int getId(){
        return id;
    }

    public List<String> getMembres_inscrits() {
        return membres_inscrits;
    }

    public List<String> getMembres_en_attente() {
        return membres_en_attente;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id){
        this.id = id;
    }

    public Date getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString(){
        String date_text = Constants.formatD.format(this.date);
        String final_date_text = "";
        for(int i = 0; i < date_text.length(); i++){
            char character = date_text.charAt(i);
            if(i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }

        return final_date_text;
    }

}
