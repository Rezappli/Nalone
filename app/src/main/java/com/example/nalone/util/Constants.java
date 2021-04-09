package com.example.nalone.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.nalone.R;
import com.example.nalone.enumeration.ImageProtocol;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.GetImageListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Group;
import com.example.nalone.objects.User;
import com.example.nalone.fcm.MySingleton;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.koalap.geofirestore.GeoFire;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constants {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore mStoreBase;

    public static FirebaseStorage mStore = FirebaseStorage.getInstance();
    public static StorageReference USER_STORAGE_REF;

    public static FirebaseMessaging mMessaging = FirebaseMessaging.getInstance();

    public static FirebaseUser currentUser;

    public static String USER_ID;
    public static DocumentReference USER_REFERENCE;
    public static User USER;

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

    public static String FCM_API = null;
    public static String serverKey = null;
    public static String contentType = null;

    public static String URL_ME = "http://api.nolonely.fr:53000/me.php";
    public static String URL_SIGN_IN = "http://api.nolonely.fr:53000/sign_in.php";
    public static String URL_NEARBY_EVENTS = "http://api.nolonely.fr:53000/nearby_events.php";
    public static String URL_FRIENDS = "http://api.nolonely.fr:53000/get_friends.php";
    public static String URL_FRIENDS_INVITATIONS = "http://api.nolonely.fr:53000/get_friends_invitations.php";
    public static String URL_EVENT_INVITATIONS = "http://api.nolonely.fr:53000/get_event_invitations.php";
    public static String URL_NOTIFICATIONS = "http://api.nolonely.fr:53000/get_my_notifications.php";
    public static String URL_USER_WHITHOUT_ME = "http://api.nolonely.fr:53000/get_users.php";
    public static String URL_MY_FRIENDS = "http://api.nolonely.fr:53000/get_friends.php";
    public static String URL_EVENT_DATE = "http://api.nolonely.fr:53000/get_event_date.php";
    public static String URL_EVENT_ISREGISTERED = "http://api.nolonely.fr:53000/isRegistered.php";
    public static String URL_EVENT_NEXT = "http://api.nolonely.fr:53000/get_next_event.php";
    public static String URL_EVENT_POPULAR = "http://api.nolonely.fr:53000/get_event_popular.php";
    public static String URL_EVENT_FILTRE = "http://api.nolonely.fr:53000/get_event_filtre.php";
    public static String URL_ADD_EVENT = "http://api.nolonely.fr:53000/add_event.php";
    public static String URL_IMAGE = "http://api.nolonely.fr:53000/test_image.php";

    public static String URL_GET_GROUPS = "http://api.nolonely.fr:53000/get_groups.php";

    public static String URL_DELETE_USER_TO_EVENT = "http://api.nolonely.fr:53000/delete_user_event.php";
    public static String URL_DELETE_FRIEND = "http://api.nolonely.fr:53000/delete_friend.php";
    public static String URL_EVENT_DELETE = "http://api.nolonely.fr:53000/delete_event.php";


    public static String URL_ADD_USER_TO_EVENT = "http://api.nolonely.fr:53000/add_user_event.php";
    public static String URL_ADD_FRIEND = "http://api.nolonely.fr:53000/add_friend.php";
    public static String URL_ADD_GROUP = "http://api.nolonely.fr:53000/add_group.php";

    public static String URL_UPDATE_ME= "http://api.nolonely.fr:53000/update_me.php";
    public static String URL_TEST = "http://api.nolonely.fr:53000/test.php";


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


    public static void setUserImage(final User u, final Context context, final ImageView imageView){
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if(u.getImage_url() != null) {
                    if(!Cache.fileExists(u.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(u.getUid(), img);
                                            u.setImage_url(Cache.getImageDate(u.getUid()));
                                            mStoreBase.collection("users").document(u.getUid()).set(u);
                                            Glide.with(context).load(img).fitCenter().centerCrop().into(imageView);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(u.getUid());
                        if(Cache.getImageDate(u.getUid()).equalsIgnoreCase(u.getImage_url())) {
                            Glide.with(context).load(imgCache).fitCenter().centerCrop().into(imageView);
                        }else{
                            StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(u.getUid(), img);
                                                u.setImage_url(Cache.getImageDate(u.getUid()));
                                                mStoreBase.collection("users").document(u.getUid()).set(u);
                                                Glide.with(context).load(img).fitCenter().centerCrop().into(imageView);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

    }

    public static void setGroupImage(final Group g, final Context context, final ImageView imageView){
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if(g.getImage_url() != null) {
                    if(!Cache.fileExists(g.getUid())) {
                        StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(g.getUid(), img);
                                            g.setImage_url(Cache.getImageDate(g.getUid()));
                                            mStoreBase.collection("groups").document(g.getUid()).set(g);
                                            Glide.with(context).load(img).fitCenter().centerCrop().into(imageView);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(g.getUid());
                        if(Cache.getImageDate(g.getUid()).equalsIgnoreCase(g.getImage_url())) {
                            Glide.with(context).load(imgCache).fitCenter().centerCrop().into(imageView);
                        }else{
                            StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(g.getUid(), img);
                                                g.setImage_url(Cache.getImageDate(g.getUid()));
                                                mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                Glide.with(context).load(img).fitCenter().centerCrop().into(imageView);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    public static String getFullDate(Date d){
        String s = Constants.formatD.format(d);
        String temp = "";
        for(int i=0; i < s.length(); i++){
            if(i==0){
                temp=Character.toUpperCase(s.charAt(i))+"";
            }else{
                temp += s.charAt(i);
            }
        }
        return temp;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void uploadImageOnServer(ImageType type, String name, String data,
                                           final Context context){
        JSONObjectCrypt params = new JSONObjectCrypt();
        try {
            params.put("image", data);
            params.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONController.getJsonObjectFromUrl("http://api.nolonely.fr:53000/test_image.php", context, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("Response", "Value:"+jsonObject.toString());
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError);
            }
        });
        /*JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("protocol", ImageProtocol.SAVE);
        params.addParameter("type", type);
        params.addParameter("name", name);
        params.addParameter("image", data);

        //Base64.encodeToString(data.getBytes(), 0)

        try {
            Log.w("Image", "Params : "+params.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONController.getJsonObjectFromUrl(Constants.URL_IMAGE, context, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("Response", "Response :"+jsonObject.toString());
                Toast.makeText(context, context.getResources().getString(R.string.image_save), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur: "+volleyError.toString());
                Toast.makeText(context, context.getResources().getString(R.string.image_save_error), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public static void uploadFile(final String selectedFilePath, final Context context) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadImageOnServer(final ImageType type, String name, final Context context, final GetImageListener imageListener){

    }

    public static String key = "kXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z"; // 128 bit key
    public static String iv = "7w!z!C&F)J@NcRfU"; // 16 bytes IV
}
