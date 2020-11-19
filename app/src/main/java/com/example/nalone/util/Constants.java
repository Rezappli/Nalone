package com.example.nalone.util;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nalone.Evenement;
import com.example.nalone.User;
import com.example.nalone.listeners.FireStoreEventsListeners;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Constants {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore mStoreBase = FirebaseFirestore.getInstance();


    public static FirebaseStorage mStore = FirebaseStorage.getInstance();
    public static StorageReference USER_STORAGE_REF;

    public static FirebaseUser currentUser;

    public static String USER_ID;
    public static DocumentReference USER_REFERENCE;
    public static User USER;

    public static int widthScreen;
    public static int heightScreen;

    public static boolean load = false;

    public static List<CoreListener> listeners = new ArrayList<>();

    public static LatLng USER_LATLNG;

    public static final DateFormat formatD = DateFormat.getDateInstance(DateFormat.FULL,
            new Locale("fr", "FR"));

    public static LatLng targetZoom;

    public static int range = 50000;

    public static boolean maPosition;

    public static String USER_UID_LISTEN = null;

    public static Bundle nolonelyBundle = new Bundle();

    public static Application application;

    public static void updateUserData(User u){
        mStoreBase.collection("users").document(u.getUid())
                .set(u)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error writing document", e);
                    }
                });
    }

    public static void getUserData(String uid, final FireStoreUsersListeners fireStoreUsersListeners){
        mStoreBase.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.w("CONSTANTS", "Data read : " + document.getData().toString());
                        User u = document.toObject(User.class);
                        fireStoreUsersListeners.onDataUpdate(u);
                    } else {
                        Log.d("FIREBASE", "No such document");
                    }
                } else {
                    Log.d("FIREBASE", "get failed with ", task.getException());
                }
            }
        });
    }

    public static void updateEventData(Evenement e){
        mStoreBase.collection("users").document(e.getUid())
                .set(e)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error writing document", e);
                    }
                });
    }

    public static void getEventData(String uid, final FireStoreEventsListeners fireStoreEventsListeners){
        mStoreBase.collection("events").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.w("CONSTANTS", "Data read : " + document.getData().toString());
                        Evenement e = document.toObject(Evenement.class);
                        fireStoreEventsListeners.onDataUpdate(e);
                    } else {
                        Log.d("FIREBASE", "No such document");
                    }
                } else {
                    Log.d("FIREBASE", "get failed with ", task.getException());
                }
            }
        });
    }
}
