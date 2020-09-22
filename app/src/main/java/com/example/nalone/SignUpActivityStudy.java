package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivityStudy extends AppCompatActivity {

    CardView cardViewINFO;
    CardView cardViewMMI;
    CardView cardViewTC;
    CardView cardViewGB;
    CardView cardViewLP;
    Button signUpNext;

    boolean click;

    List<CardView> cards = new ArrayList<>();

    static String departement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_study);

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
               if(click){
                   Intent signUpTwo = new Intent(getBaseContext(),SignUpActivityTwo.class);
                   startActivityForResult(signUpTwo,0);
               }else{
                   Toast.makeText(getApplicationContext(), "Selectionnez votre département", Toast.LENGTH_SHORT).show();
               }

           }
       });




    }
}