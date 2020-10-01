package com.example.nalone;

import android.widget.ImageButton;

public class ItemPerson {
    private int mImageResource;
    private String mNom;
    private int mImageButton;

    public ItemPerson(int imageResource, String nom){
        mImageResource = imageResource;
        mNom = nom;

    }

    public  int getImageResource(){
        return mImageResource;
    }

    public String getNom(){
        return mNom;
    }

}
