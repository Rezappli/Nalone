package com.example.nalone.util;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static int COUNTER = 0;

    public static Bundle NALONE_BUNDLE = new Bundle();
    public static String user_mail;
    public static String user_id;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

}
