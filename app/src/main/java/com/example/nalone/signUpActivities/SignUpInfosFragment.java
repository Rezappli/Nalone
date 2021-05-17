package com.example.nalone.signUpActivities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpInfosFragment extends SignUpFragment {


    private TextInputEditText inputName;
    private TextInputEditText inputSurname;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_sign_up_infos;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParticularAccount();

    }

    private void initParticularAccount() {

        inputName = (TextInputEditText) view.findViewById(R.id.signupName);

        initFields();
        inputName = (TextInputEditText) view.findViewById(R.id.signupName);
        inputSurname = (TextInputEditText) view.findViewById(R.id.signupSurname);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            inputName.setText(acct.getFamilyName());
            inputSurname.setText(acct.getDisplayName());
        }

        initFields();
    }

    @Override
    public void onNextClicked() {
        String nameEntered = inputName.getText().toString();
        String surnameEntered = inputSurname.getText().toString();

        if (nameEntered.length() == 1) {
            inputName.setError(getResources().getString(R.string.error_name_short), customErrorDrawable);
            return;
        }

        if (nameEntered.matches("")) {
            inputName.setError(getResources().getString(R.string.error_name_empty), customErrorDrawable);
            return;
        }

        if (surnameEntered.length() == 1) {
            inputSurname.setError(getResources().getString(R.string.error_name_short), customErrorDrawable);
            return;
        }

        if (surnameEntered.matches("")) {
            inputSurname.setError(getResources().getString(R.string.error_surname_empty), customErrorDrawable);
            return;
        }

        user.setLast_name(nameEntered);
        user.setFirst_name(surnameEntered);
        notifySignUpMainListenerChange();
    }

    private void initFields() {
        if (user.getLast_name() != null)
            inputName.setText(user.getLast_name());

        if (user.getFirst_name() != null)
            inputSurname.setText(user.getFirst_name());
    }


    private void notifySignUpMainListenerChange() {
        SignUpMainActivity.listenerMain.onFragmentValidate(this);
    }
}