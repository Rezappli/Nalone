package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;

import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;

public class CheckPhoneValidationActivity extends AppCompatActivity {

    private String password;
    private String field;
    private static final int RC_SIGN_IN = 101;
    private int randomNumber;

    private PinEntryEditText pinEntryEditText;
    private Button validatButton;

    private User user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_validation);
        pinEntryEditText = findViewById(R.id.txt_pin_entry);
        validatButton = findViewById(R.id.buttonCheckValidation);
        pinEntryEditText.setOnPinEnteredListener(str -> {
            if (str.toString().equals("0000")) {
                user.setNumber(field);
                createUser();
            } else {
                Toast.makeText(CheckPhoneValidationActivity.this, "CODE INCORRECT", Toast.LENGTH_SHORT).show();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            password = extras.getString("password");
            field = extras.getString("field");
            user = (User) extras.getSerializable("user");
            TextView infoUser = findViewById(R.id.infoUserCheckValidation);
            infoUser.setText(field);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUser() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        Log.w("CHECK_PHONE", user.getUid());
        Log.w("CHECK_PHONE", user.getName());
        Log.w("CHECK_PHONE", user.getPseudo());
        Log.w("CHECK_PHONE", user.getCenter_interest());
        Log.w("CHECK_PHONE", user.getCity());
        Log.w("CHECK_PHONE", "none");
        Log.w("CHECK_PHONE", user.getNumber());
        Log.w("CHECK_PHONE", user.getLatitude() + "");
        Log.w("CHECK_PHONE", user.getLongitude() + "");
        Log.w("CHECK_PHONE", password);

        params.putCryptParameter("uid", user.getUid());
        params.putCryptParameter("name", user.getName());
        params.putCryptParameter("pseudo", user.getPseudo());
        params.putCryptParameter("center_interest", user.getCenter_interest());
        params.putCryptParameter("city", user.getCity());
        params.putCryptParameter("description", null);
        params.putCryptParameter("mail", "none");
        params.putCryptParameter("phone", user.getNumber());
        params.putCryptParameter("number_events_attend", 0);
        params.putCryptParameter("number_events_create", 0);
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