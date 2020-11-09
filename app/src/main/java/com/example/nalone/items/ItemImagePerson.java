package com.example.nalone.items;

public class ItemImagePerson {
    private int mImageResource;
    private int id;

    public ItemImagePerson(int id, int image){
        this.id = id;
        this.mImageResource = image;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public int getId(){
        return id;
    }
}
