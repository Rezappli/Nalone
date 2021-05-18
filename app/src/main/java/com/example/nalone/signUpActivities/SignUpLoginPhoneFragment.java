package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;

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

    @Override
    public void onNextClicked() {
        if (!isMail) {
            String telEntered = inputPhone.getText().toString();
            String passEntered = inputPass.getText().toString();
            String confirmPassEntered = inputConfirmPass.getText().toString();

            if (telEntered.matches("")) {
                inputPhone.setError("Entrez votre numéro de téléphone", customErrorDrawable);
                return;
            }

            if (telEntered.length() < 8) {
                inputPhone.setError("Entrez votre numéro de téléphone", customErrorDrawable);
                return;
            }

            if (passEntered.matches("")) {
                inputPass.setError("Entrez votre mot de passe", customErrorDrawable);
                return;
            }

            if (passEntered.matches("")) {
                inputConfirmPass.setError("Confirmez votre mot de passe", customErrorDrawable);
                return;
            }

            if (!passEntered.equals(confirmPassEntered)) {
                inputConfirmPass.setError("Le mot de passe ne correspond pas", customErrorDrawable);
                return;
            }

            Intent intent = new Intent(getContext(), CheckPhoneValidationActivity.class);
            intent.putExtra("field", telEntered);
            intent.putExtra("password", passEntered);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    }

    public void displayError(String error) {
        inputConfirmPass.setError(error, customErrorDrawable);

    }
}