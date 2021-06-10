package com.nolonely.mobile.signUpActivities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import org.json.JSONObject;

public class CheckMailValidationActivity extends CheckValidationActivity {

    private TextView buttonResendCheckValidation;
    private PinEntryEditText codeValidation;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail_validation);

        buttonResendCheckValidation = findViewById(R.id.buttonResendCheckValidation);
        codeValidation = findViewById(R.id.codeValidation);

        codeValidation.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                Log.w("Check", "Check Validation Mail");
                checkValidationMail();
            }
        });
        buttonResendCheckValidation.setOnClickListener(v -> checkMail());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            password = extras.getString("password");
            login = extras.getString("login");
            user = (User) extras.getSerializable("user");
            initWidgets();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        TextView infoUser = findViewById(R.id.infoUserCheckValidation);
        checkMail();
        infoUser.setText(login);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkMail() {

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", user.getUid());
        params.putCryptParameter("mail", login);

        JSONController.getJsonObjectFromUrl(Constants.URL_SEND_MAIL, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
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
        params.putCryptParameter("code", codeValidation.getText().toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_CHECK_MAIL_VALIDATION, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {

                if (jsonObject.length() == 3) {
                    user.setMail(login);
                    user.setNumber("+33");
                    createUser();
                } else {
                    Toast.makeText(CheckMailValidationActivity.this, getResources().getString(R.string.code_incorrect), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("MAIL", "NOOO");
            }
        });
    }


}