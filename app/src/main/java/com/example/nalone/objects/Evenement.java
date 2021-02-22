package com.example.nalone.objects;

import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.util.Constants;

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
    private String startDate;
    private String endDate;
    private String owner_uid;
    private int nbMembers;
    private StatusEvent statusEvent;
    private String ownerFirstName;
    private String ownerLastName;

    public Evenement() {
        this.uid = null;
        this.name = null;
        this.description = null;
        this.address = null;
        this.city = null;
        this.visibility = null;
        this.startDate = null;
        this.endDate = null;
        this.owner_uid = null;
        this.latitude = null;
        this.longitude = null;
        this.nbMembers = 0;
        this.statusEvent = null;
    }


    public Evenement(String uid, StatusEvent statusEvent,String owner, String name, String description,
                     String address, String city, Visibility visibility, String startDate, String endDate,
                     double latitude, double longitude, int nbMembers){
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.visibility = visibility;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner_uid = owner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbMembers = nbMembers;
        this.statusEvent = statusEvent;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public String getOwner_uid() {
        return owner_uid;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public String getOwner_first_name() {
        return ownerFirstName;
    }

    public String getOwner_last_name() {
        return ownerLastName;
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
}
