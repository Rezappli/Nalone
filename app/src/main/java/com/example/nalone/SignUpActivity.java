package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText nom;
    TextInputEditText prenom;
    EditText ville;
    EditText adresse;
    EditText date;
    EditText numero;
    RadioButton homme;
    RadioButton femme;

    String sexe;

    Button buttonSignUpNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSignUpNext = (Button) findViewById(R.id.signUpNext);
        nom = (TextInputEditText) findViewById(R.id.signupNom);
        prenom = (TextInputEditText) findViewById(R.id.signupPrenom);
        ville = (EditText) findViewById(R.id.singupVille);
        adresse = (EditText) findViewById(R.id.signupAdresse);
        date = (EditText) findViewById(R.id.signupDate);
        numero = (EditText) findViewById(R.id.signupNumero);
        homme = (RadioButton) findViewById(R.id.signupHomme);
        femme = (RadioButton) findViewById(R.id.signupHomme);



        buttonSignUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomEntre = nom.getText().toString();
                String prenomEntre = prenom.getText().toString();
                String villeEntre = ville.getText().toString();
                String adresseEntre = adresse.getText().toString();
                String numeroEntre = numero.getText().toString();
                String dateEntre = date.getText().toString();


                if(homme.isChecked() == false && femme.isChecked() == false){
                    homme.setError("Entrez votre sexe");
                    return;
                }else{
                    if(homme.isChecked()){
                        sexe="homme";
                    }else{
                        sexe="femme";
                    }
                }

                if (nomEntre.matches("")){
                    nom.setError("Entrez votre nom");
                    return;
                }

                if (prenomEntre.matches("")){
                    prenom.setError("Entrez votre prénom");
                    return;
                }

                if (dateEntre.matches("")){
                    date.setError("Entrez votre date de naissance");
                    return;
                }

                if (villeEntre.matches("")){
                    ville.setError("Entrez votre ville");
                    return;
                }

                if (adresseEntre.matches("")){
                    adresse.setError("Entrez votre adresse");
                    return;
                }

                if (numeroEntre.matches("")){
                    numero.setError("Entrez votre numéro de téléphone");
                    return;
                }

                Intent signUpTwo = new Intent(getBaseContext(), SignUpActivityTwo.class);
                startActivityForResult(signUpTwo, 0);

            }
        });
    }

}