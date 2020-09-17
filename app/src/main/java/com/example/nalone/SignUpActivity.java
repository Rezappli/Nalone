package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

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
    RadioButton homme;
    RadioButton femme;

    String sexe;

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
        homme =  findViewById(R.id.signupHomme);
        femme = findViewById(R.id.signupFemme);



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


             /*   if(homme.isChecked() == false && femme.isChecked() == false){
                    femme.setError("Entrez votre sexe");
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

                if(loginEntre.matches("")){
                    adresseMail.setError("Entrez votre adresse mail");
                    return;
                }

                if(passEntre.matches("")){
                    pass.setError("Entrez votre mot de passe");
                    return;
                }

                if(passEntre.contains(confirmPassEntre)){

                }else{
                    confirmPass.setError("Le mot de passe ne correspond pas");
                    return;
                }

*/
                Intent signUpTwo = new Intent(getBaseContext(), SignUpActivityTwo.class);
                startActivityForResult(signUpTwo, 0);

            }
        });
    }

}