package com.nolonely.mobile.objects;

import com.nolonely.mobile.enumeration.Visibility;

public class Group {
    private String uid;
    private String name;
    private String owner;
    private String description;
    private String image_url;
    private Visibility visibility;

    public Group(){}

    public Group(String uid,String owner,String name,
                 String description, Visibility visibility){
        this.uid = uid;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.image_url = null;
        this.visibility = visibility;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
