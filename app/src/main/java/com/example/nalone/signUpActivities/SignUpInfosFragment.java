package com.example.nalone.signUpActivities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class SignUpInfosFragment extends SignUpFragment {


    private TextInputEditText inputName;
    private TextInputEditText inputPseudo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_sign_up_infos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputName = (TextInputEditText) view.findViewById(R.id.signupName);

        inputName = view.findViewById(R.id.signupName);
        inputPseudo = view.findViewById(R.id.signupSurname);
        initFields();

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            inputName.setText(acct.getFamilyName());
            inputPseudo.setText(acct.getDisplayName());
        }

        initFields();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNextClicked() {
        String nameEntered = inputName.getText().toString();
        String pseudoEntered = inputPseudo.getText().toString();

        if (nameEntered.length() == 1) {
            inputName.setError(getResources().getString(R.string.error_name_short), customErrorDrawable);
            return;
        }

        if (nameEntered.matches("")) {
            inputName.setError(getResources().getString(R.string.error_name_empty), customErrorDrawable);
            return;
        }

        if (pseudoEntered.length() == 1) {
            inputPseudo.setError(getResources().getString(R.string.error_name_short), customErrorDrawable);
            return;
        }

        if (pseudoEntered.matches("")) {
            inputPseudo.setError(getResources().getString(R.string.error_surname_empty), customErrorDrawable);
            return;
        }

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("pseudo", pseudoEntered);

        Log.w("param", params.toString());
        JSONController.getJsonObjectFromUrl(Constants.URL_EXISTING_PSEUDO, getContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    user.setPseudo(pseudoEntered);
                    user.setName(nameEntered);
                    notifySignUpMainListenerChange();
                } else {
                    errorPseudoAlreadyExist();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                errorPseudoAlreadyExist();
            }
        });


    }

    private void errorPseudoAlreadyExist() {
        inputPseudo.setError(getResources().getString(R.string.error_surname_existing), customErrorDrawable);
        return;
    }

    private void initFields() {
        if (user.getName() != null)
            inputName.setText(user.getName());

        if (user.getPseudo() != null)
            inputPseudo.setText(user.getPseudo());
    }


    private void notifySignUpMainListenerChange() {
        SignUpMainActivity.listenerMain.onFragmentValidate(this);
    }
}