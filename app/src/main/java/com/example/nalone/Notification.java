package com.example.nalone;

import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.mStoreBase;

public class Notification {
    String mOwner;
    String mDesc;
    TypeNotification mType;
    DocumentReference mOwnerRef;

    public Notification(TypeNotification type,String owner,DocumentReference ownerRef,String desc){
        this.mType = type;
        this.mDesc = desc;
        this.mOwner = owner;
        this.mOwnerRef = ownerRef;
    }



    private static String  joinGroupe() {
        return "à rejoins un de vos groupe";
    }

    private static String  demandeAmi() {
        return "vous à envoyé une demande d'ami";
    }

    private static String  joinEvent() {
        return "à rejoins un de vos évènement";
    }

    private static String joinAmi(){
        return "à accepté votre demande d'ami";
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

    public static void createDemandeAmi(User user){
        Notification n = new Notification(TypeNotification.DEMANDE_AMI,USER.getFirst_name() + " " + USER.getLast_name(),USER_REFERENCE, demandeAmi());
        mStoreBase.collection("users").document(user.getUid()).collection("notifications").
                document(UUID.randomUUID().toString()).set(n);
    }

    public static void createJoinAmi(User user){
        Notification n = new Notification(TypeNotification.JOIN_AMI,USER.getFirst_name() + " " + USER.getLast_name(),USER_REFERENCE, joinAmi());
        mStoreBase.collection("users").document(user.getUid()).collection("notifications").
                document(UUID.randomUUID().toString()).set(n);
    }

    public static void createJoinGroupe(User user){
        Notification n = new Notification(TypeNotification.JOIN_GROUPE,USER.getFirst_name() + " " + USER.getLast_name(),USER_REFERENCE, joinAmi());
        mStoreBase.collection("users").document(user.getUid()).collection("notifications").
                document(UUID.randomUUID().toString()).set(n);
    }

    public static void createJoinEvent(User user){
        Notification n = new Notification(TypeNotification.DEMANDE_AMI,USER.getFirst_name() + " " + USER.getLast_name(),USER_REFERENCE, joinEvent());
        mStoreBase.collection("users").document(user.getUid()).collection("notifications").
                document(UUID.randomUUID().toString()).set(n);
    }
}
