package com.nolonely.mobile.items;

import java.io.Serializable;
import java.util.List;

public class ItemPerson implements Serializable {
    private String uid;
    private int mImageResource;
    private String mNom;
    private String mNomToLowerCase;
    private String mDescription;
    private String mVille;
    private String cursus;
    private List<String> centresInterets;
    private int mImageResource2;
    private String mNbCreate;
    private String mNbParticipate;


    public ItemPerson(String uid, int imageResource, String nom, int imageResource2 , String description, String ville, String cursus, String nbCreate, String nbParticipate, List<String> centresInterets){
        this.mImageResource = imageResource;
        this.mNom = nom;
        this.mNomToLowerCase = mNom.toLowerCase();
        this.mImageResource2 = imageResource2;
        this.mDescription = description;
        this.cursus = cursus;
        this.mVille = ville;
        this.mNbCreate = nbCreate;
        this.mNbParticipate = nbParticipate;
        this.uid = uid;
        this.centresInterets = centresInterets;
    }

    public ItemPerson(){}

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

    public String getVille() {
        return mVille;
    }

    public void setVille(String mVille) {
        this.mVille = mVille;
    }

    public String getCursus() {
        return cursus;
    }

    public void setCursus(String cursus) {
        this.cursus = cursus;
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

    public String getUid(){
        return uid;
    }

    public List<String> getCentresInterets() {
        return centresInterets;
    }

}
