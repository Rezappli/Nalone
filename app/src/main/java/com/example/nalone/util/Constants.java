package com.example.nalone.util;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nalone.Evenement;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public static List<MarkerOptions> markers = new ArrayList<>();
    public static List<Evenement> events = new ArrayList<>();
    public static int nb_evenements = 0;
    public static File settingsFile;
    public static final DateFormat formatD= DateFormat.getDateInstance(DateFormat.FULL,
            new Locale("fr","FR"));

    public static String getBytesFromBitmap(Bitmap image){
        return "";
    }
}
