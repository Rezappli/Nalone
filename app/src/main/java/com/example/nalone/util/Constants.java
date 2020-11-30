package com.example.nalone.util;

import android.app.Application;
import com.example.nalone.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koalap.geofirestore.GeoFire;

import java.text.DateFormat;
import java.util.Locale;

public class Constants {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore mStoreBase = FirebaseFirestore.getInstance();
    public static GeoFire geoFirestore = new GeoFire(mStoreBase.collection("events"));


    public static FirebaseStorage mStore = FirebaseStorage.getInstance();
    public static StorageReference USER_STORAGE_REF;

    public static FirebaseMessaging mMessaging = FirebaseMessaging.getInstance();

    public static FirebaseUser currentUser;

    public static String USER_ID;
    public static DocumentReference USER_REFERENCE;
    public static User USER;

    public static int widthScreen;
    public static int heightScreen;

    public static boolean load = false;

    public static GeoHash USER_GEOHASH;

    public static final DateFormat formatD = DateFormat.getDateInstance(DateFormat.FULL,
            new Locale("fr", "FR"));

    public static LatLng targetZoom;

    public static int range = 5;

    public static boolean maPosition;

    public static Application application;
}
