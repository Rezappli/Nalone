package com.nolonely.mobile.objects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nolonely.mobile.ui.message.ChatActivityFriend;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String name;
    private String pseudo;
    private String city;
    private String number;
    private String mail;
    private String description;
    private int number_events_create;
    private int number_events_attend;
    private String image_url;
    private double latitude;
    private double longitude;

    public User() {
    }

    public User(String uid) {
        this.uid = uid;
    }


    public User(String uid, String name, String pseudo, String city, String number, String mail, String description, int number_events_create, int number_events_attend, String image_url, double latitude, double longitude) {
        this.uid = uid;
        this.name = name;
        this.pseudo = pseudo;
        this.city = city;
        this.number = number;
        this.mail = mail;
        this.description = description;
        this.number_events_create = number_events_create;
        this.number_events_attend = number_events_attend;
        this.image_url = image_url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(String uid, String name, String pseudo, String city,
                String number, String mail,
                String description) {
        this.uid = uid;
        this.name = name;
        this.pseudo = pseudo;
        this.city = city;
        this.number = number;
        this.mail = mail;
        this.image_url = null;

        this.description = description;

        this.number_events_create = 0;
        this.number_events_attend = 0;
        this.image_url = null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber_events_create() {
        return number_events_create;
    }

    public void setNumber_events_create(int number_events_create) {
        this.number_events_create = number_events_create;
    }

    public int getNumber_events_attend() {
        return number_events_attend;
    }

    public void setNumber_events_attend(int number_events_attend) {
        this.number_events_attend = number_events_attend;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_date) {
        this.image_url = image_date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    
    public void launchChat(Context context, Activity activity, Boolean newChat) {
        Intent intent = new Intent(context, ChatActivityFriend.class);
        intent.putExtra("user", this);
        intent.putExtra("newChat", newChat);
        activity.startActivity(intent);
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", city='" + city + '\'' +
                ", number='" + number + '\'' +
                ", mail='" + mail + '\'' +
                ", description='" + description + '\'' +
                ", number_events_create='" + number_events_create + '\'' +
                ", number_events_attend='" + number_events_attend + '\'' +
                ", image_url='" + image_url + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
