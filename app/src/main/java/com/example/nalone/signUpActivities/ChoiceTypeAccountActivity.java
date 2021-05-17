package com.example.nalone.signUpActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nalone.R;

public class ChoiceTypeAccountActivity extends Activity {

    public static String ACTION_GET_TYPE_ACCOUNT = "ACTION_GET_TYPE_ACCOUNT";
    public static String EXTRA_TYPE_ACCOUNT = "EXTRA_TYPE_ACCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_type_account);
    }

    public void signUpBusiness(View view) {
        notifyTypeAccount(true);
    }

    public void signUpParticular(View view) {
        notifyTypeAccount(false);
    }

    public void notifyTypeAccount(boolean isBusiness) {
        Intent intent = new Intent(getBaseContext(), SignUpMainActivity.class);
        intent.putExtra("isBusinessAccount", isBusiness);
        startActivity(intent);
    }

}