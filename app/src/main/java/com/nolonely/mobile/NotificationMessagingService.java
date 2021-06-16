package com.nolonely.mobile;

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
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.Notification;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import static com.nolonely.mobile.util.Constants.USER;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationMessagingService extends JobService {
    private static final String TAG = "MyOwnService";
    private boolean jobCancelled = false;
    private static int nbNotifications = -1;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(() -> {
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

            Log.d(TAG, "Job finished");
            jobFinished(params, true);
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readNotifications() {

        if (USER != null) {
            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());

            JSONController.getJsonArrayFromUrl(Constants.URL_NEW_NOTIFICATIONS, getApplicationContext(), params, new JSONArrayListener() {
                @Override
                public void onJSONReceived(JSONArray jsonArray) {
                    try {
                        if (jsonArray.length() > 0) {
                            if (nbNotifications != jsonArray.length() || nbNotifications == -1) {
                                if (jsonArray.length() == 1) {
                                    Notification notif = (Notification) JSONController.convertJSONToObject(jsonArray.getJSONObject(0), Notification.class);
                                    sendNotification(getApplicationContext().getResources().getString(R.string.title_notification), notif.getMessage());
                                } else {
                                    sendNotification(getApplicationContext().getResources().getString(R.string.title_notification), getApplicationContext().getResources().getString(R.string.description_more_one_event));
                                }
                            }
                        }

                        nbNotifications = jsonArray.length();
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
