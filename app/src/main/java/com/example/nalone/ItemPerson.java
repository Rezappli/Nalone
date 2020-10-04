package com.example.nalone;

import android.widget.ImageButton;

import com.example.nalone.util.Constants;

import java.util.concurrent.CopyOnWriteArrayList;

public class ItemPerson {
    private int mImageResource;
    private String mNom;
    private String mNomToLowerCase;
    private int mImageResource2;

    public ItemPerson(int imageResource, String nom, int imageResource2){
        mImageResource = imageResource;
        mNom = nom;
        mNomToLowerCase = mNom.toLowerCase();
        mImageResource2 = imageResource2;
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

    public String getmNomToLowerCase(){
        return mNomToLowerCase;
    }


}
