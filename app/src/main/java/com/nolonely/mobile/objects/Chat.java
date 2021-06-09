package com.nolonely.mobile.objects;


public class Chat {
    private String uidChannel;
    private String name;
    private String lastMessage;
    private String date;

    public Chat(String uidChannel, String date, String username, String lastMessage) {
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
