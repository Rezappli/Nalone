package com.example.nalone;

import android.widget.ImageButton;

import com.example.nalone.util.Constants;

import java.util.concurrent.CopyOnWriteArrayList;

public class ItemPerson {
    private int mImageResource;
    private String mNom;
    private int mImageResource2;
    private int mId;

    public ItemPerson(int imageResource, String nom, int imageResource2){
        mImageResource = imageResource;
        mNom = nom;
        mImageResource2 = imageResource2;
        mId = Constants.getId();
    }

    public void changerPlus(int imageRessourceChange){
        mImageResource2 = imageRessourceChange;
    }

    public void changeName(String name){
        mNom = name;
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

    public int getId(){
        return mId;
    }

}
