package com.nolonely.mobile.bdd.json;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public abstract class JSONActivity extends AppCompatActivity {

    protected Handler handler;

    public static String ACTION_RECEIVE_NO_CONNECTION = "ACTION_RECEIVE_NO_CONNECTION";
    public static String ACTION_RECEIVE_CONNECTION = "ACTION_RECEIVE_CONNECTION";


    protected final BroadcastReceiver receiverNoConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                displayNoConnection();
            }
        }
    };

    protected final BroadcastReceiver receiverConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                hiddeNoConnection();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE_NO_CONNECTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverNoConnection, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ACTION_RECEIVE_CONNECTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverConnection, intentFilter1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverNoConnection);
    }


    protected boolean haveInternetConnection() {
        // Fonction haveInternetConnection : return true si connecté, return false dans le cas contraire
        NetworkInfo network = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (network == null || !network.isConnected()) {
            // Le périphérique n'est pas connecté à Internet
            return false;
        }
        // Le périphérique est connecté à Internet
        return true;
    }

    protected void launchJSONCall() {
        handler = new Handler();
        handler.postDelayed(runnable, 0);
    }

    private final Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            if (!haveInternetConnection()) {
                handler.postDelayed(this, 0);
            } else {
                doInHaveInternetConnection();
                handler.removeCallbacks(this);
            }
        }
    };

    protected abstract void doInHaveInternetConnection();

    protected abstract void displayNoConnection();

    protected abstract void hiddeNoConnection();

}
