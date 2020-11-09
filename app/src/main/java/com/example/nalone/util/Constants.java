package com.example.nalone.util;

import com.example.nalone.CoreListener;
import com.example.nalone.Evenement;
import com.example.nalone.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Constants {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static FirebaseDatabase mFirebase = FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser currentUser;
    public static String USER_ID;

    public static HashMap<String, Evenement> EVENTS_LIST = new HashMap();
    public static HashMap<String, User> USERS_LIST = new HashMap();
    public static HashMap<String, MarkerOptions> MARKERS_EVENT = new HashMap<>();
    public static boolean MARKER_COLOR_SET = false;
    public static DatabaseReference USERS_DB_REF = mFirebase.getReference("users");
    public static DatabaseReference EVENTS_DB_REF = mFirebase.getReference("evenements");

    public static int widthScreen;
    public static int heightScreen;

    public static boolean load = false;

    public static List<CoreListener> listeners = new ArrayList<>();

    public static LatLng USER_LATLNG;


    public static final DateFormat formatD = DateFormat.getDateInstance(DateFormat.FULL,
            new Locale("fr", "FR"));

    public static LatLng targetZoom;

    public static int range = 50000;
}
