package com.nolonely.mobile.objects;


public class Chat {
    private String uidChannel;
    private String date;

    public Chat(String uidChannel, String date) {
        this.uidChannel = uidChannel;
        this.date = date;
    }

    public String getUidChannel() {
        return uidChannel;
    }

    public String getDate() {
        return date;
    }
}
