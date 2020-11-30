package com.example.nalone;

import com.google.firebase.firestore.DocumentReference;

public class Chat {

    private DocumentReference chatRef;
    private String uid;

    public Chat(){}

    public Chat(DocumentReference chatRef, String uid){
        this.chatRef = chatRef;
        this.uid = uid;
    }

    public DocumentReference getChatRef() {
        return chatRef;
    }

    public void setChatRef(DocumentReference chatRef) {
        this.chatRef = chatRef;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
