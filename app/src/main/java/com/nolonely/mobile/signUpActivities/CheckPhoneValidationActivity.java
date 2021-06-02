package com.nolonely.mobile.signUpActivities;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.User;

public class CheckPhoneValidationActivity extends CheckValidationActivity {


    private int randomNumber;

    private PinEntryEditText pinEntryEditText;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_validation);
        pinEntryEditText = findViewById(R.id.txt_pin_entry);
        pinEntryEditText.setOnPinEnteredListener(str -> {
            if (str.toString().equals("0000")) {
                user.setNumber(login);
                user.setMail(login);
                createUser();
            } else {
                Toast.makeText(CheckPhoneValidationActivity.this, "CODE INCORRECT", Toast.LENGTH_SHORT).show();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            password = extras.getString("password");
            login = extras.getString("login");
            user = (User) extras.getSerializable("user");
            TextView infoUser = findViewById(R.id.infoUserCheckValidation);
            infoUser.setText(login);
        }
    }


}