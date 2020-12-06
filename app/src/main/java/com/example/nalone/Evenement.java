package com.example.nalone;

import com.example.nalone.util.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class Evenement implements Serializable {

    private String uid;
    private String name;
    private String description;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Visibility visibility;
    private DocumentReference ownerDoc;
    private Timestamp date;
    private String owner;
    private int nbMembers;

    public Evenement() {}


    public Evenement(String uid, String owner, int image, String name, String description, String address, String city,
                     Visibility visibility, DocumentReference ownerDoc, Timestamp date, GeoPoint location, int nbMembers){
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.visibility = visibility;
        this.ownerDoc = ownerDoc;
        this.date = date;
        this.owner = owner;
        if(location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
        this.nbMembers = nbMembers;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public DocumentReference getOwnerDoc() {
        return ownerDoc;
    }

    public void setOwnerDoc(DocumentReference ownerDoc) {
        this.ownerDoc = ownerDoc;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public int getNbMembers() {
        return nbMembers;
    }
}
