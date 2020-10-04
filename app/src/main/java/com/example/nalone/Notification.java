package com.example.nalone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification {

    public static NotificationManager SystemService;
    private String title;
    private String description;
    private int priority;
    private int icon;
    private Context context;
    private NotificationCompat.Builder builder;

    public Notification(Context context, String title, String description, int priority, int icon){

        this.context = context;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.icon = icon;

        generate();

    }

    public void generate(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = SystemService;
            manager.createNotificationChannel(channel);
        }

        Intent snoozeIntent = new Intent(context, MainActivity.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);


        builder = new NotificationCompat.Builder(context, "My Notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority)
                .addAction(R.drawable.ic_launcher_background, "Voir",
                        snoozePendingIntent)
                .setAutoCancel(true);

    }

    public void show(){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1,builder.build());
    }


}
