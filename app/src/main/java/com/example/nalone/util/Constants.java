package com.example.nalone.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.fcm.MySingleton;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.Group;
import com.example.nalone.objects.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import com.koalap.geofirestore.GeoFire;

public class Constants {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser currentUser;

    public static String USER_ID;
    public static User USER;

    public static boolean load = false;

    public static final DateFormat formatD = DateFormat.getDateInstance(DateFormat.FULL,
            new Locale("fr", "FR"));

    public static final DateFormat formatDayHoursMinutesSeconds = new SimpleDateFormat("dd-mm-yyyy hh:MM:ss");

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

    public static String BASE_URL = "http://nolonely.fr";
    public static String BASE_API_URL = "http://api.nolonely.fr";

    public static String URL_ME = BASE_API_URL + "/me.php";
    public static String URL_SIGN_IN = BASE_API_URL + "/sign_in.php";
    public static String URL_REGISTER = BASE_API_URL + "/register.php";
    public static String URL_SEND_MAIL = BASE_API_URL + "/send_mail.php";
    public static String URL_CHECK_MAIL_VALIDATION = BASE_API_URL + "/check_mail_validation.php";
    public static String URL_EXISTING_OBJECT = BASE_API_URL + "/existing_object.php";
    public static String URL_NEARBY_EVENTS = BASE_API_URL + "/get_event_nearby.php";
    public static String URL_FRIENDS = BASE_API_URL + "/get_friends.php";
    public static String URL_FRIENDS_INVITATIONS = BASE_API_URL + "/get_friends_request.php";
    public static String URL_GET_USER_INVITATIONS = BASE_API_URL + "/get_friends_invitations.php";
    public static String URL_EVENT_INVITATIONS = BASE_API_URL + "/get_event_invitations.php";

    public static String URL_NOTIFICATIONS = BASE_API_URL + "/get_my_notifications.php";
    public static String URL_NEW_NOTIFICATIONS = BASE_API_URL + "/get_my_new_notifications.php";

    public static String URL_USER_WHITHOUT_ME = BASE_API_URL + "/get_users.php";
    public static String URL_MY_FRIENDS = BASE_API_URL + "/get_friends.php";
    public static String URL_EVENT_DATE = BASE_API_URL + "/get_event_date.php";
    public static String URL_EVENT_ISREGISTERED = BASE_API_URL + "/is_registered.php";
    public static String URL_EVENT_NEXT = BASE_API_URL + "/get_next_event.php";
    public static String URL_EVENT_POPULAR = BASE_API_URL + "/get_event_popular.php";
    public static String URL_EVENT_SUGGEST = BASE_API_URL + "/get_event_suggest.php";
    public static String URL_EVENT_LIST_MAP = BASE_API_URL + "/get_event_list_map.php";
    public static String URL_EVENT_FILTRE = BASE_API_URL + "/get_event_filtre.php";
    public static String URL_ADD_EVENT = BASE_API_URL + "/add_event.php";
    public static String URL_UPLOAD_IMAGE = BASE_API_URL + "/upload_image.php";

    public static String URL_GET_GROUPS = BASE_API_URL + "/get_groups.php";

    public static String URL_DELETE_USER_TO_EVENT = BASE_API_URL + "/delete_user_event.php";
    public static String URL_DELETE_FRIEND = BASE_API_URL + "/delete_friend.php";
    public static String URL_EVENT_DELETE = BASE_API_URL + "/delete_event.php";


    public static String URL_ADD_USER_TO_EVENT = BASE_API_URL + "/add_user_event.php";
    public static String URL_SEND_FRIEND_REQUEST = BASE_API_URL + "/add_friend_request.php";
    public static String URL_ADD_FRIEND = BASE_API_URL + "/add_friend.php";
    public static String URL_ADD_GROUP = BASE_API_URL + "/add_group.php";

    public static String URL_UPDATE_ME = BASE_API_URL + "/update_me.php";
    public static String URL_TEST = BASE_API_URL + "/test.php";

    public static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AdY_jy3oUvBy5lsZW8jva7OQpCplEfY86EVtUsNImMEDFjiy5wlK2AbRTOcFijOaCcqQDUR6LHTxMEAl");

    public static final float margin_percentage = 3.5142f;


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


    public static void setUserImage(final User u, final ImageView imageView) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (u.getImage_url() != null && !u.getImage_url().equals("")) {
                    Glide.with(application).load(u.getImage_url()).fitCenter().centerCrop().into(imageView);
                }
            }
        });

    }

    public static void setGroupImage(final Group g, final ImageView imageView) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (g.getImage_url() != null && !g.getImage_url().equals("")) {
                    Glide.with(application).load(g.getImage_url()).fitCenter().centerCrop().into(imageView);
                }
            }
        });
    }

    public static void setEventImage(final Evenement e, final ImageView imageView) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (e.getImage_url() != null && !e.getImage_url().equals("")) {
                    Glide.with(application).load(e.getImage_url()).fitCenter().centerCrop().into(imageView);
                }
            }
        });
    }

    public static String getFullDate(Date d) {
        String s = Constants.formatD.format(d);
        String temp = "";
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                temp = Character.toUpperCase(s.charAt(i)) + "";
            } else {
                temp += s.charAt(i);
            }
        }
        return temp;
    }

    public static String getDateDayHoursMinutesSeconds(Date d) {
        return formatDayHoursMinutesSeconds.format(d);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void uploadImageOnServer(ImageType type, String name, String data,
                                           final Context context) {
        JSONObjectCrypt params = new JSONObjectCrypt();
        try {
            params.put("uid", USER.getUid());
            params.put("name", name);
            params.put("image", data);
            params.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w("ResponseImage", params.toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_UPLOAD_IMAGE, context, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("ResponseImage", "Value:" + jsonObject.toString());
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("ResponseImage", volleyError.toString());
            }
        });
    }

    public static String key = "kXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z"; // 128 bit key
    public static String iv = "7w!z!C&F)J@NcRfU"; // 16 bytes IV
    public static String g_key = "E0:48:BF:89:66:92:6F:30:7E:B1:3A:EC:6D:01:E7:F2:29:7F:B6:FF";
}
