package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.nalone.R;

import java.util.ArrayList;
import java.util.List;

public class SignUpHobbiesActivity extends AppCompatActivity {


    CardView ciSport;
    CardView ciMusique;
    CardView ciPhoto;
    CardView ciJeuxVideo;
    CardView ciProg;
    CardView ciFilms;
    CardView ciPeinture;
    CardView ciLivres;

    Integer nbCi=0;

    Button signupNext;

    List<CardView> cardsList = new ArrayList<>();

    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up_hobbies);

        ciFilms = findViewById(R.id.ciFilms);
        ciSport = findViewById(R.id.ciSport);
        ciMusique = findViewById(R.id.ciMusique);
        ciProg = findViewById(R.id.signUpHomme);
        ciPeinture = findViewById(R.id.ciPeinture);
        ciPhoto = findViewById(R.id.ciPhoto);
        ciLivres = findViewById(R.id.ciLivres);
        ciJeuxVideo = findViewById(R.id.signUpFemme);
        signupNext = findViewById(R.id.signUpNext2);

        cardsList.add(ciFilms);
        cardsList.add(ciSport);
        cardsList.add(ciMusique);
        cardsList.add(ciProg);
        cardsList.add(ciPeinture);
        cardsList.add(ciPhoto);
        cardsList.add(ciLivres);
        cardsList.add(ciJeuxVideo);



        for(final CardView e : cardsList){
            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(nbCi == 5){
                        if(e.getCardElevation() == 0){
                            e.setCardBackgroundColor(Color.WHITE);
                            e.setCardElevation(15);
                            nbCi--;
                        }else{
                            Toast.makeText(getApplicationContext(),"Limite atteinte",Toast.LENGTH_LONG).show();

                        }

                    }else{
                        if(e.getCardElevation() > 0){
                            e.setCardBackgroundColor(Color.rgb(24,236,197));
                            e.setCardElevation(0);
                            nbCi++;

                        }else{
                            e.setCardBackgroundColor(Color.WHITE);
                            e.setCardElevation(15);
                            nbCi--;
                        }
                    }
                }
            });
        }

        signupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nbCi == 0){
                    Toast.makeText(getApplicationContext(), "Séléctionnez au moins 1 centre d'intérêt", Toast.LENGTH_LONG).show();
                    return;
                }

                List<String> interets = new ArrayList<>();
                for(final CardView c : cardsList){
                    if(c == ciFilms){
                        if(c.getCardElevation() == 0){
                            interets.add("Films");
                        }
                    }

                    if(c == ciSport){
                        if(c.getCardElevation() == 0){
                            interets.add("Sport");
                        }
                    }

                    if(c == ciMusique){
                        if(c.getCardElevation() == 0){
                            interets.add("Musique");
                        }
                    }

                    if(c == ciJeuxVideo){
                        if(c.getCardElevation() == 0){
                            interets.add("Jeux");
                        }
                    }

                    if(c == ciLivres){
                        if(c.getCardElevation() == 0){
                            interets.add("Livres");
                        }
                    }

                    if(c == ciPeinture){
                        if(c.getCardElevation() == 0){
                            interets.add("Peinture");
                        }
                    }

                    if(c == ciPhoto){
                        if(c.getCardElevation() == 0){
                            interets.add("Photo");
                        }
                    }

                    if(c == ciProg){
                        if(c.getCardElevation() == 0){
                            interets.add("Programmation");
                        }
                    }
                }

                SignUpInformationActivity.user.setCentreInterets(interets);
                Intent signUpThree = new Intent(getBaseContext(), SignUpProfilActivity.class);
                startActivityForResult(signUpThree, 0);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
