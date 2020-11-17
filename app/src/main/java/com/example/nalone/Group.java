package com.example.nalone;

public class Group {
    private String uid;
    private String name;
    private String description;
    private String image_url;

    public Group(){}

    public Group(String uid,String name,
                 String description){
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.image_url = null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
