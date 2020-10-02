package com.example.nalone;

import android.widget.ImageButton;

public class ItemPerson {
    private int mImageResource;
    private String mNom;
    private int mImageResource2;

    public ItemPerson(int imageResource, String nom, int imageResource2){
        mImageResource = imageResource;
        mNom = nom;
        mImageResource2 = imageResource2;

    }

    public  int getImageResource(){
        return mImageResource;
    }

    public String getNom(){
        return mNom;
    }

    public  int getImageResource2(){
        return mImageResource2;
    }

}
