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
import java.util.List;

public class User {
    private String uid;
    private String sex;
    private String last_name;
    private String first_name;
    private String city;
    private String number;
    private String mail;
    private List<String> centers_interests;
    private String cursus;
    private String description;
    private String number_events_create;
    private String number_events_attend;
    private List<DocumentReference> friends_requests_send;
    private List<DocumentReference> friends_requests_received;
    private List<DocumentReference> friends;
    private List<DocumentReference> register_events;
    private List<DocumentReference> waiting_events;
    private String birthday_date;
    private String image_url;

    public User(){}

    public User(String uid, String last_name, String first_name, String sex, String city,
                String number, String mail, String cursus, List<String> centers_interests,
                String description, String birthday_date){
        this.uid = uid;
        this.last_name = last_name;
        this.first_name = first_name;
        this.sex = sex;
        this.city = city;
        this.number = number;
        this.mail = mail;
        this.cursus = cursus;
        this.centers_interests = centers_interests;
        this.description = description;
        this.birthday_date = birthday_date;

        this.number_events_create = "0";
        this.number_events_attend = "0";

        this.friends_requests_send = new ArrayList<>();
        this.friends_requests_received = new ArrayList<>();
        this.friends = new ArrayList<>();

        this.image_url = null;
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

    public List<String> getCenters_interests() {
        return centers_interests;
    }

    public void setCenters_interests(List<String> centers_interests) {
        this.centers_interests = centers_interests;
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

    public String get_number_events_create() {
        return number_events_create;
    }

    public void set_number_events_create(String number_events_create) {
        this.number_events_create = number_events_create;
    }

    public String get_number_events_attend() {
        return number_events_attend;
    }

    public void set_number_events_attend(String number_events_attend) {
        this.number_events_attend = number_events_attend;
    }

    public List<DocumentReference> get_friends_requests_send() {
        return friends_requests_send;
    }

    public void set_friends_requests_send(List<DocumentReference> friends_requests_send) {
        this.friends_requests_send = friends_requests_send;
    }

    public List<DocumentReference> get_friends_requests_received() {
        return friends_requests_received;
    }

    public void set_friends_requests_received(List<DocumentReference> friends_requests_received) {
        this.friends_requests_received = friends_requests_received;
    }

    public List<DocumentReference> get_friends() {
        return friends;
    }

    public void set_friends(List<DocumentReference> friends) {
        this.friends = friends;
    }

    public List<DocumentReference> get_register_events() {
        return register_events;
    }

    public void set_register_events(List<DocumentReference> register_events) {
        this.register_events = register_events;
    }

    public List<DocumentReference> getWaiting_events() {
        return waiting_events;
    }

    public void set_waiting_events(List<DocumentReference> waiting_events) {
        this.waiting_events = waiting_events;
    }

    public String get_birthday_date() {
        return birthday_date;
    }

    public void set_birthday_date(String birthday_date) {
        this.birthday_date = birthday_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
