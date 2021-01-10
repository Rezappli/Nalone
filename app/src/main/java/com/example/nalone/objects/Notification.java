package com.example.nalone.objects;

import com.example.nalone.enumeration.TypeNotification;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.UUID;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.mStoreBase;

public class Notification {

    private String mOwner;
    private String mDesc;
    private TypeNotification mType;
    private DocumentReference mOwnerRef;
    private Timestamp mDate;


    public Notification(){

    }

    public Notification(TypeNotification type,String owner,DocumentReference ownerRef,String desc){
        this.mType = type;
        this.mDesc = desc;
        this.mOwner = owner;
        this.mOwnerRef = ownerRef;
        this.mDate = new Timestamp(new Date(System.currentTimeMillis()));
    }



    public static String  joinGroupe() {
        return "a rejoins un de vos groupe";
    }

    public static String  demandeAmi() {
        return "vous a envoyé une demande d'ami";
    }

    public static String  demandeEvent() {
        return "vous a envoyé une invitation pour participer à son évènement";
    }

    public static String  demandeGroupe() {
        return "vous a envoyé une invitation pour rejoindre son groupe";
    }

    public static String  joinEvent() {
        return "participe à un de vos évènement";
    }

    public static String joinAmi(){
        return "a accepté votre demande d'ami";
    }

    public String getmOwner() {
        return mOwner;
    }

    public void setmOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public TypeNotification getmType() {
        return mType;
    }

    public void setmType(TypeNotification mType) {
        this.mType = mType;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public DocumentReference getmOwnerRef() {
        return mOwnerRef;
    }

    public void setmOwnerRef(DocumentReference mOwnerRef) {
        this.mOwnerRef = mOwnerRef;
    }

    public Timestamp getmDate() {
        return mDate;
    }

    public void setmDate(Timestamp mDate) {
        this.mDate = mDate;
    }

    public static void createNotif(User user, String desc){
        Notification n = new Notification(TypeNotification.DEMANDE_AMI,USER.getFirst_name() + " " + USER.getLast_name(),USER_REFERENCE, desc);
        mStoreBase.collection("users").document(user.getUid()).collection("notifications").
                document(UUID.randomUUID().toString()).set(n);
    }
}
