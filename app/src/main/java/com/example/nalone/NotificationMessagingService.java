package com.example.nalone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.VolleyError;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.Notification;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.nalone.util.Constants.USER;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationMessagingService extends JobService {
    private static final String TAG = "MyOwnService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                for (int i = 0; i < 2; i++) {
                    Log.d(TAG, "run: " + i);
                    if (jobCancelled) {
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                readNotifications();
                Log.d(TAG, "Job finished");
                jobFinished(params, true);
            }

        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readNotifications() {

        //JOUER AVEC LES DATES
        if (USER != null) {
            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());

            JSONController.getJsonArrayFromUrl(Constants.URL_NEW_NOTIFICATIONS, getApplicationContext(), params, new JSONArrayListener() {
                @Override
                public void onJSONReceived(JSONArray jsonArray) {
                    try {
                        if (jsonArray.length() > 0) {
                            if (jsonArray.length() == 1) {
                                Notification notif = (Notification) JSONController.convertJSONToObject(jsonArray.getJSONObject(0), Notification.class);
                                sendNotification("NoLonely - Notification", notif.getMessage());
                            } else {
                                sendNotification("NoLonely - Notification", "Vous avez plusieurs notifications en attente");
                            }
                        }
                    } catch (JSONException e) {
                        Log.w("Response", "Erreur:" + e.getMessage());
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Log.w("Response", "Erreur:" + volleyError.toString());
                }
            });
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setupChannels(notificationManager);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "NoLonely")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);
        //.setContentIntent(pendingIntent)

        notificationManager.notify(29, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("NoLonely", "NoLonely - Services", NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription("NoLonely - Services");
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
