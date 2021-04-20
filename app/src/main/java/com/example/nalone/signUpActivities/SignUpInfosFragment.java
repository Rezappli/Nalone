package com.example.nalone.signUpActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.nalone.signUpActivities.SignUpMainActivity.userName;
import static com.example.nalone.signUpActivities.SignUpMainActivity.userSurname;

public class SignUpInfosFragment extends Fragment {

    private static Context context;

    public static ImageView imageH;
    public static ImageView imageF;
    public static TextView textH;
    public static TextView textF;

    public static TextInputEditText inputName;
    public static TextInputEditText inputSurname;
    public static boolean isMan;
    public static boolean isWoman;

    public static SignUpInfosFragment newInstance() {
        return new SignUpInfosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up_infos, container, false);

        context = getContext();
        CardView manCardView = view.findViewById(R.id.signUpHomme);
        CardView womanCardView = view.findViewById(R.id.signUpFemme);
        imageF = view.findViewById(R.id.imageFemme);
        imageH = view.findViewById(R.id.imageHomme);
        textF = view.findViewById(R.id.textFemme);
        textH = view.findViewById(R.id.textHomme);
        inputName = (TextInputEditText) view.findViewById(R.id.signupNom);
        inputSurname = (TextInputEditText) view.findViewById(R.id.signupPrenom);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            inputName.setText(acct.getFamilyName());
            inputSurname.setText(acct.getDisplayName());
        }

        manCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMan();
            }
        });

        womanCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickWoman();
            }
        });

        initFields();

        return view;
    }

    public enum sex {
        FEMME, HOMME;
    }

    private void initFields() {
        if (isWoman)
            clickWoman();

        if (isMan)
            clickMan();

        if (userName != null)
            inputName.setText(userName);

        if (userSurname != null)
            inputSurname.setText(userSurname);
    }

    private void clickWoman() {
        imageF.setImageResource(R.drawable.signup_femme_focused);
        imageH.setImageResource(R.drawable.signup_homme);
        textF.setTextColor(Color.GRAY);
        textH.setTextColor(Color.GRAY);
        isWoman = true;
        isMan = false;
    }

    private void clickMan() {
        imageH.setImageResource(R.drawable.signup_homme_focused);
        imageF.setImageResource(R.drawable.signup_femme);
        textF.setTextColor(Color.GRAY);
        textH.setTextColor(Color.GRAY);
        isWoman = false;
        isMan = true;
    }

    public static boolean checkValidity() {
        @SuppressLint("UseCompatLoadingForDrawables") final Drawable customErrorDrawable = context.getResources().getDrawable(R.drawable.error_icon);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        String nameEntered = inputName.getText().toString();
        String surnameEntered = inputSurname.getText().toString();

        if (!isMan && !isWoman) {
            imageH.setImageResource(R.drawable.signup_homme_erreur);
            imageF.setImageResource(R.drawable.signup_femme_erreur);
            textF.setTextColor(Color.RED);
            textH.setTextColor(Color.RED);
            Toast.makeText(context, "Selectionnez votre sexe", Toast.LENGTH_LONG).show();
            return false;
        }

        if (nameEntered.matches("")) {
            inputName.setError("Entrez votre nom", customErrorDrawable);
            return false;
        }

        if (surnameEntered.matches("")) {
            inputSurname.setError("Entrez votre prénom", customErrorDrawable);
            return false;
        }

        userName = inputName.getText().toString();
        SignUpMainActivity.userSurname = inputSurname.getText().toString();
        if (isWoman)
            SignUpMainActivity.userSex = sex.FEMME.toString();
        else
            SignUpMainActivity.userSex = sex.HOMME.toString();

        return true;
    }
}