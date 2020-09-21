package com.example.nalone;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    EditText nom;
    EditText prenom;
    EditText ville;
    EditText adresse;
    EditText date;
    EditText numero;
    EditText adresseMail;
    EditText pass;
    EditText confirmPass;
    CardView homme;
    CardView femme;
    ImageView imageH;
    ImageView imageF;
    TextView textH;
    TextView textF;

    boolean bHomme;
    boolean bFemme;

    Button buttonSignUpNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSignUpNext = (Button) findViewById(R.id.signUpNext);
        nom =  (EditText) findViewById(R.id.signupNom);
        prenom = (EditText)  findViewById(R.id.signupPrenom);
        ville = (EditText)  findViewById(R.id.signupVille);
        adresse = (EditText) findViewById(R.id.signupAdress);
        date = (EditText)  findViewById(R.id.signupDate);
        numero = (EditText)  findViewById(R.id.signupNumero);
        adresseMail = (EditText) findViewById(R.id.signupAdresseMail);
        pass =  (EditText) findViewById(R.id.signupPass);
        confirmPass = (EditText)  findViewById(R.id.signupConfirmPass);
        homme =  findViewById(R.id.signUpHomme);
        femme = findViewById(R.id.signUpFemme);
        imageF = findViewById(R.id.imageFemme);
        imageH = findViewById(R.id.imageHomme);
        textF = findViewById(R.id.textFemme);
        textH = findViewById(R.id.textHomme);

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

                String nomEntre = nom.getText().toString();
                String prenomEntre = prenom.getText().toString();
                String villeEntre = ville.getText().toString();
                String adresseEntre = adresse.getText().toString();
                String numeroEntre = numero.getText().toString();
                String dateEntre = date.getText().toString();
                String loginEntre = adresseMail.getText().toString();
                String passEntre = pass.getText().toString();
                String confirmPassEntre = confirmPass.getText().toString();


              /* if(bHomme == false && bFemme == false){
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

                if (dateEntre.matches("")){
                    date.setError("Entrez votre date de naissance",customErrorDrawable);
                    return;
                }

                if (villeEntre.matches("")){
                    ville.setError("Entrez votre ville",customErrorDrawable);
                    return;
                }

                if (adresseEntre.matches("")){
                    adresse.setError("Entrez votre adresse",customErrorDrawable);
                    return;
                }

                if (numeroEntre.matches("")){
                    numero.setError("Entrez votre numéro de téléphone",customErrorDrawable);
                    return;
                }

                if(loginEntre.matches("")){
                    adresseMail.setError("Entrez votre adresse mail",customErrorDrawable);
                    return;
                }

                if(passEntre.matches("")){
                    pass.setError("Entrez votre mot de passe",customErrorDrawable);
                    return;
                }

                if(passEntre.contains(confirmPassEntre)){

                }else{
                    confirmPass.setError("Le mot de passe ne correspond pas",customErrorDrawable);
                    return;
                }*/

                Intent signUpStudy = new Intent(getBaseContext(), SignUpActivityStudy.class);
                startActivityForResult(signUpStudy, 0);

            }
        });
    }


}