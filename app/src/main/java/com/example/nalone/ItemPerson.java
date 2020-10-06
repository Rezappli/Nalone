package com.example.nalone;

import android.widget.ImageButton;

import com.example.nalone.util.Constants;

import java.util.concurrent.CopyOnWriteArrayList;

public class ItemPerson {
    private int id;
    private int mImageResource;
    private String mNom;
    private String mNomToLowerCase;
    private String mDescription;
    private int mImageResource2;
    private String mNbCreate = 0+"";
    private String mNbParticipate = 0+"";

    public ItemPerson(int id, int imageResource, String nom, int imageResource2 , String description, String nbCreate, String nbParticipate){
        mImageResource = imageResource;
        mNom = nom;
        mNomToLowerCase = mNom.toLowerCase();
        mImageResource2 = imageResource2;
        mDescription = description;
        mNbCreate = nbCreate;
        mNbParticipate = nbParticipate;
        this.id = id;
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

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getmNom() {
        return mNom;
    }

    public void setmNom(String mNom) {
        this.mNom = mNom;
    }

    public void setmNomToLowerCase(String mNomToLowerCase) {
        this.mNomToLowerCase = mNomToLowerCase;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmImageResource2(int mImageResource2) {
        this.mImageResource2 = mImageResource2;
    }

    public String getmNbCreate() {
        return mNbCreate;
    }

    public void setmNbCreate(String mNbCreate) {
        this.mNbCreate = mNbCreate;
    }

    public String getmNbParticipate() {
        return mNbParticipate;
    }

    public void setmNbParticipate(String mNbParticipate) {
        this.mNbParticipate = mNbParticipate;
    }

    public int getId(){
        return id;
    }
}
