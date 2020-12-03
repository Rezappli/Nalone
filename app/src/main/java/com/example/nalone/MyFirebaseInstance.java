package com.example.nalone;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import splash.SplashActivity;

import static com.example.nalone.util.Constants.ID_NOTIFICATION_GLOBAL;
import static com.example.nalone.util.Constants.ID_NOTIFICATION_INVITATIONS;
import static com.example.nalone.util.Constants.ID_NOTIFICATION_MESSAGES;
import static com.example.nalone.util.Constants.ON_FRIENDS_ACTIVITY;
import static com.example.nalone.util.Constants.ON_MESSAGE_ACTIVITY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mMessaging;
import static com.example.nalone.util.Constants.mStoreBase;

public class MyFirebaseInstance extends FirebaseMessagingService {

    public static String user_id;
    private final String ADMIN_CHANNEL_ID = "admin_channel";
    private final String NOTIFICATION_TAG_LOGCAT = "FIREBASEOC";
    private String NOTIFICATION_TAG = "TAG_EMPTY";
    private static List<String> channels = new ArrayList<>();

    @Override
    public void onNewToken(String s) {
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(NOTIFICATION_TAG_LOGCAT, "ID : " + user_id);
        Log.w(NOTIFICATION_TAG_LOGCAT, " to : " + user_id);
        mMessaging.subscribeToTopic(user_id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "subscribe !";
                if (!task.isSuccessful()) {
                    msg = "unsubscribe";
                }
                Log.w(NOTIFICATION_TAG_LOGCAT, msg + " to : " + user_id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(NOTIFICATION_TAG_LOGCAT, "Error " + e.getMessage());
            }
        });
        Log.w(NOTIFICATION_TAG_LOGCAT, "Service start");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final Intent intent = new Intent(this, SplashActivity.class);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = 0;

      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;

        if (remoteMessage.getNotification() != null) {
            notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);
            notificationID = ID_NOTIFICATION_GLOBAL;
        } else {
            if(remoteMessage.getData().get("type") != null) {
                if (remoteMessage.getData().get("type").equalsIgnoreCase("message")) {
                    notificationID = ID_NOTIFICATION_MESSAGES;
                    NOTIFICATION_TAG = remoteMessage.getData().get("sender");
                } else if (remoteMessage.getData().get("type").equalsIgnoreCase("invitation")) {
                    notificationID = ID_NOTIFICATION_INVITATIONS;
                    NOTIFICATION_TAG = remoteMessage.getData().get("sender");
                }
            }else{
                notificationID = new Random().nextInt(3000);
            }
            notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);
        }

        if(remoteMessage.getData().get("target")!=null) {
            intent.putExtra("target", remoteMessage.getData().get("target"));
        }

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if(NOTIFICATION_TAG.equalsIgnoreCase("TAG_EMPTY")) {
            notificationManager.notify(notificationID, notificationBuilder.build());
        }else{
            Log.w(NOTIFICATION_TAG_LOGCAT, "Envoye notification");
            Log.w(NOTIFICATION_TAG_LOGCAT, "Notification TAG : " + NOTIFICATION_TAG);
            Log.w(NOTIFICATION_TAG_LOGCAT, "Notification ID : " + notificationID);
            if(notificationID == ID_NOTIFICATION_INVITATIONS){
                if(ON_FRIENDS_ACTIVITY){
                    return;
                }
            }

            if(notificationID == ID_NOTIFICATION_MESSAGES){
                if(ON_MESSAGE_ACTIVITY){
                    return;
                }
            }
            notificationManager.notify(NOTIFICATION_TAG, notificationID, notificationBuilder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(NOTIFICATION_TAG, "Service stop");
    }
}
