package com.example.nalone.signUpActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;

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

    @Override
    public void onNextClicked() {
        if (isMail) {
            String mailEntered = inputMail.getText().toString();
            String passEntered = inputPass.getText().toString();
            String confirmPassEntered = inputConfirmPass.getText().toString();

            if (mailEntered.matches("")) {
                inputMail.setError("Entrez votre adresse mail", customErrorDrawable);
                return;
            }

            if (mailEntered.length() <= 3) {
                inputMail.setError("Adresse mail inconnue", customErrorDrawable);
                return;
            }

            String substringl3 = mailEntered.substring(mailEntered.length() - 3);
            String substringl4 = mailEntered.substring(mailEntered.length() - 4);
            if (!mailEntered.contains("@") ||
                    (!substringl3.contains(".fr")
                            && !substringl3.contains(".be")
                            && !substringl4.contains(".com")
                            && !substringl4.contains(".net"))) {
                inputMail.setError("Adresse mail inconnue", customErrorDrawable);
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

            Intent intent = new Intent(getContext(), CheckMailValidationActivity.class);
            intent.putExtra("login", mailEntered);
            intent.putExtra("password", passEntered);
            intent.putExtra("user", user);

            startActivity(intent);

        }

    }
}