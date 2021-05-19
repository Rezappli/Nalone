package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpLoginPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpLoginPhoneFragment extends SignUpFragment {

    private TextInputEditText inputPhone;
    private TextInputEditText inputPass;
    private TextInputEditText inputConfirmPass;

    public static SignUpLoginPhoneFragment newInstance() {
        return new SignUpLoginPhoneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_sign_up_login_phone;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputPhone = view.findViewById(R.id.signupNumero);
        inputPass = view.findViewById(R.id.signupPass);
        inputConfirmPass = view.findViewById(R.id.signupConfirmPass);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNextClicked() {
        if (!isMail) {
            String telEntered = inputPhone.getText().toString();
            String passEntered = inputPass.getText().toString();
            String confirmPassEntered = inputConfirmPass.getText().toString();

            if (telEntered.matches("")) {
                inputPhone.setError(getString(R.string.error_phone_empty), customErrorDrawable);
                return;
            }

            if (passEntered.matches("")) {
                inputPass.setError(getString(R.string.error_password_empty), customErrorDrawable);
                return;
            }

            if (passEntered.matches("")) {
                inputConfirmPass.setError(getString(R.string.error_conifrm_password_empty), customErrorDrawable);
                return;
            }

            if (!passEntered.equals(confirmPassEntered)) {
                inputConfirmPass.setError(getString(R.string.error_conifrm_password_incorrect), customErrorDrawable);
                return;
            }
            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("phone", telEntered);

            JSONController.getJsonObjectFromUrl(Constants.URL_EXISTING_OBJECT, getContext(), params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    if (jsonObject.length() == 3) {
                        Intent intent = new Intent(getContext(), CheckPhoneValidationActivity.class);
                        intent.putExtra("login", telEntered);
                        intent.putExtra("password", passEntered);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else {
                        inputPhone.setError(getResources().getString(R.string.error_mail_existing), customErrorDrawable);
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    inputPhone.setError(getResources().getString(R.string.error), customErrorDrawable);
                }
            });

        }
    }
}