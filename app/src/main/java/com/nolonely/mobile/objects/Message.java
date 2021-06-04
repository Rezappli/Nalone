package com.nolonely.mobile.objects;

public class Message {

    private String uidChannel;
    private String sender;
    private String date;
    private Boolean view;

    public Message(String uidChannel, String sender, String date, Boolean view) {
        this.uidChannel = uidChannel;
        this.sender = sender;
        this.date = date;
        this.view = view;
    }

    public String getUidChannel() {
        return uidChannel;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public Boolean getView() {
        return view;
    }
}
