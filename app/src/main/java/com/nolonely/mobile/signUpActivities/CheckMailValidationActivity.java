package com.nolonely.mobile.signUpActivities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import org.json.JSONObject;

public class CheckMailValidationActivity extends CheckValidationActivity {

    private Button buttonValidate;
    private TextView buttonResendCheckValidation;
    private Handler handler;
    private Runnable runnable;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail_validation);

        buttonValidate = findViewById(R.id.buttonCheckValidation);
        buttonResendCheckValidation = findViewById(R.id.buttonResendCheckValidation);
        buttonResendCheckValidation.setOnClickListener(v -> checkMail(true));
        buttonValidate.setOnClickListener(v -> createUser());
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            password = extras.getString("password");
            login = extras.getString("login");
            user = (User) extras.getSerializable("user");
            initWidgets();
        }

        handler = new Handler();

        runnable = this::checkValidationMail;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        TextView infoUser = findViewById(R.id.infoUserCheckValidation);
        checkMail(false);
        infoUser.setText(login);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkMail(boolean isForceSend) {

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("mail", login);
        params.putCryptParameter("uid", user.getUid());
        params.putCryptParameter("force", isForceSend);


        JSONController.getJsonObjectFromUrl(Constants.URL_SEND_MAIL, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("Response", "JSON Object received, uid : " + user.getUid());
                handler.postDelayed(runnable, 1000);
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur : " + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkValidationMail() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", user.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_CHECK_MAIL_VALIDATION, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                handler.postDelayed(runnable, 1000);

                if (jsonObject.length() == 3) {
                    buttonValidate.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_button_simple));
                    buttonValidate.setTextColor(Color.WHITE);
                    buttonValidate.setClickable(true);
                    user.setMail(login); //picaxe56250@gmail.com
                    user.setNumber("+33");
                    Log.w("MAIL", "VALIDATE");
                    handler.removeCallbacks(runnable);
                }
                Log.w("MAIL", "NO VALIDATE");

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("MAIL", "NOOO");
            }
        });
    }


}