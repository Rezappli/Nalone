package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

import static com.example.nalone.util.Constants.USER_REFERENCE;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Message){
            if(((Message) obj).getSender().equals(USER_REFERENCE) && ((Message) obj).getMessage().equals(message)
            && ((Message) obj).getTime().equals(time)){
                return true;
            }
        }
        return false;
    }
}
