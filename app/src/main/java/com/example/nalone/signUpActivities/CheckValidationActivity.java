package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;

import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;

public class CheckValidationActivity extends AppCompatActivity {
    protected User user;
    protected String password;
    protected String login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void createUser() {
        JSONObjectCrypt params = new JSONObjectCrypt();

        Log.w("CREATION_USER", user.toString());
        params.putCryptParameter("uid", user.getUid());
        params.putCryptParameter("name", user.getName());
        params.putCryptParameter("pseudo", user.getPseudo());
        params.putCryptParameter("center_interest", user.getCenter_interest());
        params.putCryptParameter("city", user.getCity());
        params.putCryptParameter("description", null);
        params.putCryptParameter("mail", user.getMail());
        params.putCryptParameter("phone", user.getNumber());
        params.putCryptParameter("number_events_attend", "0");
        params.putCryptParameter("number_events_create", "0");
        params.putCryptParameter("image_url", null);
        params.putCryptParameter("latitude", user.getLatitude());
        params.putCryptParameter("longitude", user.getLongitude());
        params.putCryptParameter("password", password);
        params.putCryptParameter("is_valid", 1);


        JSONController.getJsonObjectFromUrl(Constants.URL_REGISTER, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                USER = user;
                startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.user_error_create), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur: " + volleyError.toString());
            }
        });
    }
}
