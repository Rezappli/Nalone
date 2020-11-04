package com.example.nalone.signUpActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.ErrorClass;
import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.example.nalone.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SignUpInformationActivity extends AppCompatActivity {
    public EditText nom;
    public EditText prenom;
    public EditText ville;
    public EditText numero;
    public EditText adresseMail;
    public EditText pass;
    public EditText confirmPass;

    public static String password;

    CardView homme;
    CardView femme;
    ImageView imageH;
    ImageView imageF;
    TextView textH;
    TextView textF;

    boolean bHomme;
    boolean bFemme;

    Button buttonSignUpNext;

    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();

        setContentView(R.layout.activity_sign_up_information);


        user = null;

        buttonSignUpNext = (Button) findViewById(R.id.signUpNext);
        nom =  (EditText) findViewById(R.id.signupNom);
        prenom = (EditText)  findViewById(R.id.signupPrenom);
        ville = (EditText)  findViewById(R.id.signupVille);
        numero = (EditText)  findViewById(R.id.signupNumero);
        adresseMail = (EditText) findViewById(R.id.signupMail);
        pass =  (EditText) findViewById(R.id.signupPass);
        confirmPass = (EditText)  findViewById(R.id.signupConfirmPass);
        homme =  findViewById(R.id.signUpHomme);
        femme = findViewById(R.id.signUpFemme);
        imageF = findViewById(R.id.imageFemme);
        imageH = findViewById(R.id.imageHomme);
        textF = findViewById(R.id.textFemme);
        textH = findViewById(R.id.textHomme);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct != null){
            nom.setText(acct.getFamilyName());
            prenom.setText(acct.getDisplayName());
            adresseMail.setText(acct.getEmail());
        }

        final Drawable customErrorDrawable = getResources().getDrawable(R.drawable.error_icon);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());




        homme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageH.setImageResource(R.drawable.signup_homme_focused);
                bHomme = true;
                imageF.setImageResource(R.drawable.signup_femme);
                textH.setTextColor(Color.GRAY);
                textF.setTextColor(Color.GRAY);
            }
        });

        femme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageF.setImageResource(R.drawable.signup_femme_focused);
                bFemme = true;
                imageH.setImageResource(R.drawable.signup_homme);
                textF.setTextColor(Color.GRAY);
                textH.setTextColor(Color.GRAY);
            }
        });



        buttonSignUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ErrorClass.checkInternetConnection();
                String nomEntre = nom.getText().toString();
                String prenomEntre = prenom.getText().toString();
                String villeEntre = ville.getText().toString();
                String numeroEntre = numero.getText().toString();
                String mailEntre = adresseMail.getText().toString();
                String passEntre = pass.getText().toString();
                String confirmPassEntre = confirmPass.getText().toString();




               if(bHomme == false && bFemme == false){
                   imageH.setImageResource(R.drawable.signup_homme_erreur);
                   imageF.setImageResource(R.drawable.signup_femme_erreur);
                   textF.setTextColor(Color.RED);
                   textH.setTextColor(Color.RED);
                   Toast.makeText(getApplicationContext(), "Selectionnez votre sexe", Toast.LENGTH_LONG).show();
                   return;
               }

                if (nomEntre.matches("")){
                    nom.setError("Entrez votre nom", customErrorDrawable);
                    return;
                }

                if (prenomEntre.matches("")){
                    prenom.setError("Entrez votre prénom",customErrorDrawable);
                    return;
                }

                if (villeEntre.matches("")){
                    ville.setError("Entrez votre ville",customErrorDrawable);
                    return;
                }

                if (numeroEntre.matches("")){
                    numero.setError("Entrez votre numéro de téléphone",customErrorDrawable);
                    return;
                }

                if(mailEntre.matches("")){
                    adresseMail.setError("Entrez votre adresse mail",customErrorDrawable);
                    return;
                }

                if(passEntre.matches("")){
                    pass.setError("Entrez votre mot de passe",customErrorDrawable);
                    return;
                }

                if(passEntre.contains(confirmPassEntre)){
                    password = confirmPassEntre;
                }else{
                    confirmPass.setError("Le mot de passe ne correspond pas",customErrorDrawable);
                    return;
                }
              String sexe = "";

              if(bHomme){
                  sexe = "Homme";
              }

              if(bFemme){
                  sexe = "Femme";
              }

                user = new User(nomEntre, prenomEntre, sexe, villeEntre, numeroEntre, mailEntre, null,
                        null, "");
                Intent signUpStudy = new Intent(getBaseContext(), SignUpStudiesActivity.class);
                startActivityForResult(signUpStudy, 0);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ErrorClass.checkInternetConnection();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }


}