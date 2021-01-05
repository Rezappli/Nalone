package com.example.nalone.objects;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class ModelDataEvent {

    private Timestamp add_time;
    private boolean create;
    private String status;
    private DocumentReference user;

    public ModelDataEvent(){}

    public ModelDataEvent(boolean create, String status, DocumentReference user){
        this(create, status, user, new Timestamp(Calendar.getInstance().getTime()));
    }

    public ModelDataEvent(boolean create,String status, DocumentReference user, Timestamp add_time){
        this.create = create;
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

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }
}
