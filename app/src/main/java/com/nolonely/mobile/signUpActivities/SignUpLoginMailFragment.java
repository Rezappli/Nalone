package com.nolonely.mobile.signUpActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.json.JSONController;
import com.nolonely.mobile.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpLoginMailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpLoginMailFragment extends SignUpFragment {
 

    public TextInputEditText inputMail;
    public TextInputEditText inputPass;
    public TextInputEditText inputConfirmPass;

    public static SignUpLoginMailFragment newInstance() {
        return new SignUpLoginMailFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rootView = R.layout.fragment_sign_up_login_mail;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMail = view.findViewById(R.id.signupMail);
        inputPass = view.findViewById(R.id.signupPass);
        inputConfirmPass = view.findViewById(R.id.signupConfirmPass);
        Log.w("SIGNUP", "CREATE MAIL");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNextClicked() {
        if (isMail) {
            String mailEntered = inputMail.getText().toString();
            String passEntered = inputPass.getText().toString();
            String confirmPassEntered = inputConfirmPass.getText().toString();

            if (mailEntered.matches("")) {
                inputMail.setError(getResources().getString(R.string.error_mail_empty), customErrorDrawable);
                return;
            }

            if (mailEntered.length() <= 3) {
                inputMail.setError(getResources().getString(R.string.error_mail_incorrect), customErrorDrawable);
                return;
            }

            String substringl3 = mailEntered.substring(mailEntered.length() - 3);
            String substringl4 = mailEntered.substring(mailEntered.length() - 4);
            if (!mailEntered.contains("@") ||
                    (!substringl3.contains(".fr")
                            && !substringl3.contains(".be")
                            && !substringl4.contains(".com")
                            && !substringl4.contains(".net"))) {
                inputMail.setError(getResources().getString(R.string.error_mail_incorrect), customErrorDrawable);
                return;
            }

            if (passEntered.matches("")) {
                inputPass.setError(getResources().getString(R.string.error_password_empty), customErrorDrawable);
                return;
            }
            if (passEntered.matches("")) {
                inputConfirmPass.setError(getResources().getString(R.string.error_conifrm_password_empty), customErrorDrawable);
                return;
            }
            if (!passEntered.equals(confirmPassEntered)) {
                inputConfirmPass.setError(getResources().getString(R.string.error_conifrm_password_incorrect), customErrorDrawable);
                return;
            }

            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("mail", mailEntered);

            JSONController.getJsonObjectFromUrl(Constants.URL_EXISTING_OBJECT, getContext(), params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    if (jsonObject.length() == 3) {
                        Intent intent = new Intent(getContext(), CheckMailValidationActivity.class);
                        intent.putExtra("login", mailEntered);
                        intent.putExtra("password", passEntered);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else {
                        inputMail.setError(getResources().getString(R.string.error_mail_existing), customErrorDrawable);
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    inputMail.setError(getResources().getString(R.string.error), customErrorDrawable);

                }
            });
        }

    }
}