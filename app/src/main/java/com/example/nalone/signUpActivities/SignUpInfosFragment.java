package com.example.nalone.signUpActivities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nalone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpInfosFragment extends Fragment {

    public TextInputEditText nom;
    public TextInputEditText prenom;
    CardView homme;
    CardView femme;
    ImageView imageH;
    ImageView imageF;
    TextView textH;
    TextView textF;

    public static SignUpLoginPhoneFragment newInstance() {
        return new SignUpLoginPhoneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_infos, container, false);

        homme =  view.findViewById(R.id.signUpHomme);
        femme = view.findViewById(R.id.signUpFemme);
        imageF = view.findViewById(R.id.imageFemme);
        imageH = view.findViewById(R.id.imageHomme);
        textF = view.findViewById(R.id.textFemme);
        textH = view.findViewById(R.id.textHomme);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if(acct != null){
            nom.setText(acct.getFamilyName());
            prenom.setText(acct.getDisplayName());
        }

        final Drawable customErrorDrawable = getResources().getDrawable(R.drawable.error_icon);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());

        homme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageH.setImageResource(R.drawable.signup_homme_focused);
                SignUpMainActivity.currentUser.setSex("Homme");
                imageF.setImageResource(R.drawable.signup_femme);
                textH.setTextColor(Color.GRAY);
                textF.setTextColor(Color.GRAY);
            }
        });

        femme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageF.setImageResource(R.drawable.signup_femme_focused);
                SignUpMainActivity.currentUser.setSex("Femme");
                imageH.setImageResource(R.drawable.signup_homme);
                textF.setTextColor(Color.GRAY);
                textH.setTextColor(Color.GRAY);
            }
        });

        return view;
    }
}