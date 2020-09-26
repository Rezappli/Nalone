package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ErrorConnexionActivity extends AppCompatActivity {

    private Button retry_button;
    private Button quit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_connexion);
        retry_button = findViewById(R.id.buttonRetry);
        quit_button = findViewById(R.id.buttonQuit);

        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void retry(){
        if(isInternetConnected()){
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivityForResult(intent, 0);
        }else{
            Toast.makeText(getApplicationContext(), "Impossible de se connecter à Internet", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isInternetConnected(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;;
        }

        return connected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isInternetConnected()){
            Intent intent = new Intent(getBaseContext(), ErrorClass.activity.getClass());
            startActivityForResult(intent, 0);
        }
    }
}