package com.example.nalone;

public class NotificationData {

    public String title;
    public String description;

    public NotificationData(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

}
