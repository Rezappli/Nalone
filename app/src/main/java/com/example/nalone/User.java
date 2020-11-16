package com.example.nalone;

import android.graphics.Bitmap;

import com.example.nalone.util.Constants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.local.ReferenceSet;
import com.google.firestore.v1.Document;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.mStoreBase;

public class User {
    private String uid;
    private String sex;
    private String last_name;
    private String first_name;
    private String city;
    private String number;
    private String mail;
    private String cursus;
    private String description;
    private String number_events_create;
    private String number_events_attend;
    private String birthday_date;
    private String image_url;

    public User(){}

    public User(String uid, String last_name, String first_name, String sex, String city,
                String number, String mail, String cursus,
                String description, String birthday_date){
        this.uid = uid;
        this.last_name = last_name;
        this.first_name = first_name;
        this.sex = sex;
        this.city = city;
        this.number = number;
        this.mail = mail;
        this.cursus = cursus;

        this.description = description;
        this.birthday_date = birthday_date;

        this.number_events_create = "0";
        this.number_events_attend = "0";

        this.image_url = null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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

    public String getCursus() {
        return cursus;
    }

    public void setCursus(String cursus) {
        this.cursus = cursus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber_events_create() {
        return number_events_create;
    }

    public void setNumber_events_create(String number_events_create) {
        this.number_events_create = number_events_create;
    }

    public String getNumber_events_attend() {
        return number_events_attend;
    }

    public void setNumber_events_attend(String number_events_attend) {
        this.number_events_attend = number_events_attend;
    }

    public String getBirthday_date() {
        return birthday_date;
    }

    public void setBirthday_date(String birthday_date) {
        this.birthday_date = birthday_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
