package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView textViewSinscrire;
    private TextView textViewConnexion;
    private EditText editTextPass;
    private EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewSinscrire = (TextView) findViewById(R.id.textView3);
        textViewConnexion = (TextView) findViewById(R.id.buttonConnexion);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPass = (EditText) findViewById(R.id.editTextPassword);


        textViewSinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(getBaseContext(), SignUpActivity.class);
                startActivityForResult(signUp, 0);
            }
        });

        textViewConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String textAddress = editTextAddress.getText().toString().replace(".", ",");
                final String textPass = editTextPass.getText().toString();

                if(textAddress.matches("")){
                    editTextAddress.setError("Entrez votre adresse");
                    return;
                }

                if(textPass.matches("")){
                    editTextPass.setError("Entrez votre mot de passe");
                    return;
                }

                //Enregistrement d'un nouvel utilisateur !

                /*FirebaseDatabase database = FirebaseDatabase.getInstance("authentification/");
                DatabaseReference myRef = database.getReference(textAddress);
                myRef.setValue(textPass);*/

                //


                //Lecture d'une connexion mail/mdp via bdd
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("authentification/");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(textAddress)){
                            DatabaseReference mailRef = FirebaseDatabase.getInstance().getReference("authentification/" + textAddress);
                            mailRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String MailPasswordBind = dataSnapshot.getValue(String.class);
                                    if(textPass.equals(MailPasswordBind)){
                                        Intent signUp = new Intent(getBaseContext(), HomeActivity.class);
                                        startActivityForResult(signUp, 0);
                                    }else{
                                        editTextPass.setText("");
                                        Toast.makeText(getApplicationContext(), "Le mot de passe est incorrect !", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "L'adresse mail n'existe pas !", Toast.LENGTH_LONG).show();
                        }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
                   });
            }
        });
    }

}