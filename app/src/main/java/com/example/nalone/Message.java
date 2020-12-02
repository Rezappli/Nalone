package com.example.nalone;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Message {

    private DocumentReference sender;
    private String message;
    private Timestamp time;


    public Message(){
    }

    public Message(DocumentReference sender, String message){
        this.sender = sender;
        this.message = message;
        this.time = new Timestamp(new Date(System.currentTimeMillis()));
    }

    public DocumentReference getSender() {
        return sender;
    }

    public void setSender(DocumentReference sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return "Message : {sender:" + sender.toString() + ",message:"+message+",time:"+time+"}";
    }
}
