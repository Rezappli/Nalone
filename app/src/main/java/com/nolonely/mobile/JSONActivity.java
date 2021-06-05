package com.nolonely.mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public abstract class JSONActivity extends AppCompatActivity {

    protected Handler handler;

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
}
