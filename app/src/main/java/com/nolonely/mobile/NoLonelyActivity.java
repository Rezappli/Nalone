package com.nolonely.mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

public class NoLonelyActivity extends AppCompatActivity {

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
}
