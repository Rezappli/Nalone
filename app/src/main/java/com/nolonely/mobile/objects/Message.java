package com.nolonely.mobile.objects;

public class Message {

    private String sender;
    private String message;
    private String date;
    private Boolean view;

    public Message(String sender, String message, String date, Boolean view) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.view = view;
    }

    public String getMessage() {
        return message;
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
