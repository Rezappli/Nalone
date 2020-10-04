package com.example.nalone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorClass {

    public static AppCompatActivity activity;
    private static Dialog dialogAddPerson;

    public static void checkInternetConnection(){
        if(!isInternetConnected(activity)){
            Intent intent = new Intent(activity.getBaseContext(), ErrorConnexionActivity.class);
            activity.startActivityForResult(intent, 0);
            /*dialogAddPerson = new Dialog(activity.getBaseContext());
            dialogAddPerson.setContentView(R.layout.activity_error_connexion);
            dialogAddPerson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogAddPerson.show();*/
        }
    }

    public static boolean isInternetConnected(Activity activity){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;;
        }

        return connected;
    }

    public void printError(String tag, String message){
        Log.w(tag, message);
    }
}
