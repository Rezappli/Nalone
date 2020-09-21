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
    CardView cardViewGB;
    CardView cardViewMMI;
    CardView cardViewTC;
    CardView cardViewLP;

    Button signUpNext4;

    boolean click;

    public static String departement;

    List<CardView> cardsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_study);

        cardViewGB = findViewById(R.id.depGB);
        cardViewINFO = findViewById(R.id.depINFO);
        cardViewLP = findViewById(R.id.depLP);
        cardViewMMI = findViewById(R.id.depMMI);
        cardViewTC = findViewById(R.id.depTC);
        cardsList.add(cardViewMMI);
        cardsList.add(cardViewINFO);
        cardsList.add(cardViewTC);
        cardsList.add(cardViewLP);
        cardsList.add(cardViewGB);
        signUpNext4 = findViewById(R.id.signUpNext4);

        for (final CardView e : cardsList) {
            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(e.getCardElevation() == 0){
                        return;
                    }else{
                        for (final CardView e : cardsList) {
                            e.setCardBackgroundColor(Color.WHITE);
                            e.setCardElevation(10);
                        }
                        e.setCardBackgroundColor(Color.rgb(24, 236, 197));
                        e.setCardElevation(0);

                        if (e == cardViewTC){
                            departement = "TC";
                        }
                        if (e == cardViewGB){
                            departement = "GB";
                        }
                        if (e == cardViewINFO){
                            departement= "INFO";
                        }

                        if(e == cardViewLP){
                            departement = "LP";
                        }

                        if(e== cardViewMMI){
                            departement = "MMI";
                        }

                        Toast.makeText(getApplicationContext(), departement, Toast.LENGTH_SHORT).show();

                    }



                    click = true;


                }
            });
        }

        signUpNext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click == true){
                    Intent signUpTwo = new Intent(getBaseContext(),SignUpActivityTwo.class);
                    startActivityForResult(signUpTwo, 0);
                }else{
                    Toast.makeText(getApplicationContext(), "Selectionnez votre département", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}