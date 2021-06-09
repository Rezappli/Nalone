package com.nolonely.mobile.objects;


public class Chat {
    private String uid;
    private String name;
    private String message;
    private String date;
    private String image;

    public Chat(String uidChannel, String date, String username, String lastMessage) {
        this.uid = uidChannel;
        this.date = date;
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
