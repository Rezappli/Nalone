package com.nolonely.mobile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ConnectionService extends Service {
    private Handler mHandler;
    private Runnable runn;
    public static boolean onPage;


    @Override
    public void onCreate() {
        mHandler = new Handler();
        runn = new Runnable() {
            @Override
            public void run() {
                if(!isInternetConnected(ConnectionService.this) && !onPage){
                    onPage = true;
                    Intent dialogIntent = new Intent(ConnectionService.this, ErrorConnexionActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }

                update();
            }
        };
    }

    private void update(){
        mHandler.postDelayed(runn, 3000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(runn, 3000);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public boolean isInternetConnected(Context context){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;;
        }

        return connected;
    }
}
