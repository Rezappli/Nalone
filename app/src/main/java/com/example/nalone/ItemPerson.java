package com.example.nalone;

import android.widget.ImageButton;

public class ItemPerson {
    private int mImageResource;
    private String mNom;
    private int mImageButton;

    public ItemPerson(int imageResource, String text1){
        mImageResource = imageResource;
        mNom = text1;
        this.mImageButton = mImageResource;

    }

    public  int getImageResource(){
        return mImageResource;
    }

    public String getNom(){
        return mNom;
    }

    public int getButtonResource(){
        return mImageButton;
    }
}
