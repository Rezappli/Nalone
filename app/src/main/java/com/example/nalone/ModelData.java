package com.example.nalone;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class ModelData {

    private Timestamp add_time;
    private String status;
    private DocumentReference user;

    public ModelData(){}

    public ModelData(String status, DocumentReference user){
        this(status, user, new Timestamp(new Date()));
    }

    public ModelData(String status, DocumentReference user, Timestamp add_time){
        this.status = status;
        this.user = user;
        this.add_time = add_time;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public String getStatus() {
        return status;
    }

    public DocumentReference getUser() {
        return user;
    }

}
