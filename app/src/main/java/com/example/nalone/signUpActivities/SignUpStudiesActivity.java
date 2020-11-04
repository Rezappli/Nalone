package com.example.nalone.signUpActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nalone.ErrorClass;
import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.mAuth;

public class SignUpStudiesActivity extends AppCompatActivity {

    CardView cardViewINFO;
    CardView cardViewMMI;
    CardView cardViewTC;
    CardView cardViewGB;
    CardView cardViewLP;
    Button signUpNext;

    boolean click;

    private AppCompatActivity activity;

    List<CardView> cards = new ArrayList<>();

    static String departement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();

        setContentView(R.layout.activity_sign_up_studies);

        cardViewTC = findViewById(R.id.depTC);
        cardViewMMI = findViewById(R.id.depMMI);
        cardViewINFO = findViewById(R.id.depINFO);
        cardViewGB = findViewById(R.id.depGB);
        cardViewLP= findViewById(R.id.depLP);
        signUpNext = findViewById(R.id.signUpNext4);

        cards.add(cardViewTC);
        cards.add(cardViewMMI);
        cards.add(cardViewINFO);
        cards.add(cardViewLP);
        cards.add(cardViewGB);

       for (final CardView e : cards){
            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (final CardView e : cards){
                        if(e.getCardElevation() == 0){
                            e.setCardBackgroundColor(Color.WHITE);
                            e.setCardElevation(15);
                        }
                    }
                   e.setCardBackgroundColor(Color.rgb(24,236,197));
                    e.setCardElevation(0);

                    if(e == cardViewTC){
                        departement = "TC";
                    }
                    if(e == cardViewINFO){
                        departement = "INFO";
                    }
                    if(e == cardViewGB){
                        departement = "GB";
                    }
                    if(e == cardViewLP){
                        departement = "LP";
                    }
                    if(e == cardViewMMI){
                        departement = "MMI";
                    }

                    click = true;
                }
            });
        }

       signUpNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               ErrorClass.checkInternetConnection();

               if(click == true){
                   SignUpInformationActivity.user.setCursus(departement);
                   Intent signUpTwo = new Intent(getBaseContext(), SignUpHobbiesActivity.class);
                   startActivityForResult(signUpTwo,0);
               }else{
                   Toast.makeText(getApplicationContext(), "Selectionnez votre département", Toast.LENGTH_SHORT).show();
                   return;
               }

           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ErrorClass.checkInternetConnection();
    }
}