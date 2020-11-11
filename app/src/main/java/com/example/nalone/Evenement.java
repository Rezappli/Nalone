package com.example.nalone;

import com.example.nalone.items.ItemPerson;
import com.example.nalone.util.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evenement implements Serializable {

    private String uid;
    private int image;
    private String name;
    private String description;
    private String address;
    private String city;
    private Visibility visibility;
    private DocumentReference ownerDoc;
    private Timestamp date;
    private GeoPoint location;
    private String owner;
    private List<DocumentReference> register_users;
    private List<DocumentReference> wainting_users;

    public Evenement() {}


    public Evenement(String uid, String owner, int image, String name, String description, String address, String city,
                     Visibility visibility, DocumentReference ownerDoc, Timestamp date, GeoPoint location){
        this.uid = uid;
        this.image = image;
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.visibility = visibility;
        this.ownerDoc = ownerDoc;
        this.date = date;
        this.location = location;
        this.owner = owner;
    }

    public List<DocumentReference> getRegister_users() {
        return register_users;
    }

    public void addRegister_users(DocumentReference user) {
        this.register_users.add(user);
    }

    public List<DocumentReference> getWainting_users() {
        return wainting_users;
    }

    public void addWainting_users(DocumentReference user) {
        this.wainting_users.add(user);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner(){
        return owner;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
