package com.nolonely.mobile.objects;


import java.io.Serializable;

public class Chat implements Serializable {
    private String uid;
    private String name;
    private String message;
    private String date;
    private String image;

    public Chat(String uidChannel, String date, String name, String message) {
        this.uid = uidChannel;
        this.date = date;
        this.name = name;
        this.message = message;
    }

    public String getUidChannel() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }
}
