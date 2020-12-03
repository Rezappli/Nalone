package com.example.nalone.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nalone.User;
import com.example.nalone.fcm.MySingleton;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koalap.geofirestore.GeoFire;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    public static final DateFormat formatD = DateFormat.getDateInstance(DateFormat.FULL,
            new Locale("fr", "FR"));

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat allTimeFormat = new SimpleDateFormat("dd-MM-yy HH:mm");

    public static LatLng targetZoom;

    public static int range;

    public static boolean maPosition;

    public static Application application;

    public static int ID_NOTIFICATION_GLOBAL = 100;
    public static int ID_NOTIFICATION_INVITATIONS = 101;
    public static int ID_NOTIFICATION_MESSAGES = 102;

    public static boolean ON_MESSAGE_ACTIVITY = false;
    public static boolean ON_FRIENDS_ACTIVITY = false;

    private static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static String serverKey = "key=AAAA4ZRDAW0:APA91bErNdSatj13ahbk5w8kqEtjnJ4B4BI70KPYBvJNBnLjKjXn0y-FfB73j9p-A6Iw2sVDN93UfrjkhXxqqU3H_rVm1RuB5IwPfrcB85CgAZH2ZN-SopzO-Pp2r5p_V7R5Er_X7wl7";
    private static String contentType = "application/json";

    public static void sendNotification(JSONObject notification, final Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Notification", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("Notification", "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
