package com.example.nalone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivityTwo extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);
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
                    Toast.makeText(getApplicationContext(), "Selectionnez au moins 1 centre d'intérêt", Toast.LENGTH_LONG).show();
                    return;
                }

                String[] interets = new String[cardsList.size()];
                int index = 0;
                for(final CardView c : cardsList){
                    if(c == ciFilms){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Films";
                        }
                    }

                    if(c == ciSport){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Sport";
                        }
                    }

                    if(c == ciMusique){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Musique";
                        }
                    }

                    if(c == ciJeuxVideo){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Jeux";
                        }
                    }

                    if(c == ciLivres){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Livres";
                        }
                    }

                    if(c == ciPeinture){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Peinture";
                        }
                    }

                    if(c == ciPhoto){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Photo";
                        }
                    }

                    if(c == ciProg){
                        if(c.getCardElevation() == 0){
                            interets[index] = "Programmation";
                        }
                    }

                    index++;
                }

                SignUpActivity.userData.setCentreInterets(interets);
                Intent signUpThree = new Intent(getBaseContext(), SignUpActivityThree.class);
                startActivityForResult(signUpThree, 0);
            }
        });



    }


}
