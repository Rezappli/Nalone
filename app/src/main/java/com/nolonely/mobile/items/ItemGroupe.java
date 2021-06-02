package com.nolonely.mobile.items;

import java.io.Serializable;

public class ItemGroupe implements Serializable {
    private String uid;
    private int mImageResource, mImageResource2;
    private String mNom;
    private String mNomToLowerCase;
    private String mDescription;


    public ItemGroupe(String uid, int imageResource, String nom, int imageResource2 , String description){
        this.mImageResource = imageResource;
        this.mNom = nom;
        this.mNomToLowerCase = mNom.toLowerCase();
        this.mImageResource2 = imageResource2;
        this.mDescription = description;
        this.uid = uid;
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


    public String getUid(){
        return uid;
    }

}
