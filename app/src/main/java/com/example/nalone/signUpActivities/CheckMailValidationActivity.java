package com.example.nalone.signUpActivities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;
import com.example.nalone.util.CryptoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;

public class CheckMailValidationActivity extends CheckValidationActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail_validation);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            password = extras.getString("password");
            login = extras.getString("field");
            user = (User) extras.getSerializable("user");
            initWidgets();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        TextView infoUser = findViewById(R.id.infoUserCheckValidation);
        checkMail(login, password);
        infoUser.setText(login);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkMail(final String mail, final String pass) {
        final SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPreferences.edit();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("mail", mail);
        params.putCryptParameter("password", pass);


        JSONController.getJsonObjectFromUrl(Constants.URL_SIGN_IN, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    try {
                        editor.putString("mail", CryptoUtils.encrypt(mail));
                        editor.putString("password", CryptoUtils.encrypt(pass));
                        editor.apply();
                        loadUserData(jsonObject);
                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        Log.w("Response", "Erreur:" + e.getMessage());
                    }
                } else if (jsonObject.length() == 4) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.mail_not_verified), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Mail not verified");
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.mail_or_password_incorrect), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur : " + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserData(JSONObject json) throws JSONException {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", json.getString("uid"));

        JSONController.getJsonObjectFromUrl(Constants.URL_ME, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                USER = (User) JSONController.convertJSONToObject(jsonObject, User.class);
                Toast.makeText(getBaseContext(), "CA FONCTIONNNNNE", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
            }
        });
    }


}