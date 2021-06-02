package com.nolonely.mobile.items;

public class ItemImagePerson {
    private int mImageResource;
    private String uid;

    public ItemImagePerson(String uid, int image){
        this.uid = uid;
        this.mImageResource = image;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getUid(){
        return uid;
    }
}
