package com.example.nalone.objects;

import com.google.firebase.firestore.DocumentReference;

public class ChatModel {

    private String uid;
    private DocumentReference chatRef;

    public ChatModel(String uid, DocumentReference chatRef){
        this.uid = uid;
        this.chatRef = chatRef;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public DocumentReference getChatRef() {
        return chatRef;
    }

    public void setChatRef(DocumentReference chatRef) {
        this.chatRef = chatRef;
    }
}
