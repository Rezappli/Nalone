package com.example.nalone.objects;

import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.util.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

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
    private Timestamp startDate;
    private Timestamp endDate;
    private String owner;
    private int nbMembers;
    private StatusEvent statusEvent;
    private double distance;

    public Evenement() {
        this.uid = null;
        this.name = null;
        this.description = null;
        this.address = null;
        this.city = null;
        this.visibility = null;
        this.ownerDoc = null;
        this.startDate = null;
        this.endDate = null;
        this.owner = null;
        this.latitude = null;
        this.longitude = null;
        this.nbMembers = 0;
        this.statusEvent = null;
        this.distance = 0;
    }


    public Evenement(String uid, StatusEvent statusEvent,String owner, int image, String name, String description, String address, String city,
                     Visibility visibility, DocumentReference ownerDoc, Timestamp startDate, Timestamp endDate, GeoPoint location, double distance, int nbMembers){
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.visibility = visibility;
        this.ownerDoc = ownerDoc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
        if(location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
        this.nbMembers = nbMembers;
        this.statusEvent = statusEvent;
        this.distance = distance;
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

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
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
        String date_text = Constants.formatD.format(this.startDate);
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

    public void setNbMembers(int nbMembers) {
        this.nbMembers = nbMembers;
    }

    public StatusEvent getStatusEvent() {
        return statusEvent;
    }

    public void setStatusEvent(StatusEvent statusEvent) {
        this.statusEvent = statusEvent;
    }
}
