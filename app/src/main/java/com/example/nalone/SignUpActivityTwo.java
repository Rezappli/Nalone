package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class SignUpActivityTwo extends AppCompatActivity {

    public static String departement;
    ImageView imageViewTC;
    ImageView imageViewGB;
    ImageView imageViewMMI;
    ImageView imageViewINFO;
    Button buttonNext;
    RadioButton annee1;
    RadioButton annee2;
    TextView textViewDepartement;

    Integer annee;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);

        imageViewTC = (ImageView) findViewById(R.id.imageViewTC);
        imageViewGB = (ImageView) findViewById(R.id.imageViewGB);
        imageViewMMI = (ImageView) findViewById(R.id.imageViewMMI);
        imageViewINFO = (ImageView) findViewById(R.id.imageViewINFO);
        buttonNext = (Button) findViewById(R.id.signUpNext);
        annee1 = (RadioButton) findViewById(R.id.signupAnnee1);
        annee2 = (RadioButton) findViewById(R.id.signupAnnee2);
        textViewDepartement = findViewById(R.id.textViewDepartement);

        imageViewTC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                imageViewTC.setImageResource(R.drawable.logo_tc_selected);
                imageViewGB.setImageResource(R.drawable.logo_gb);
                imageViewMMI.setImageResource(R.drawable.logo_mmi);
                imageViewINFO.setImageResource(R.drawable.logo_info);
                departement = "TC";
            }
        });

        imageViewGB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                imageViewGB.setImageResource(R.drawable.logo_gb_selected);
                imageViewTC.setImageResource(R.drawable.logo_tc);
                imageViewMMI.setImageResource(R.drawable.logo_mmi);
                imageViewINFO.setImageResource(R.drawable.logo_info);
                departement = "GB";
            }
        });

        imageViewMMI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                imageViewMMI.setImageResource(R.drawable.logo_mmi_selected);
                imageViewGB.setImageResource(R.drawable.logo_gb);
                imageViewTC.setImageResource(R.drawable.logo_tc);
                imageViewINFO.setImageResource(R.drawable.logo_info);
                departement = "MMI";
            }
        });

        imageViewINFO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                imageViewINFO.setImageResource(R.drawable.logo_info_selected);
                imageViewGB.setImageResource(R.drawable.logo_gb);
                imageViewMMI.setImageResource(R.drawable.logo_mmi);
                imageViewTC.setImageResource(R.drawable.logo_tc);
                departement = "INFO";
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(annee1.isChecked() == false && annee2.isChecked() == false) {
                    annee2.setError("Entrez votre année d'étude");
                    return;
                }else{
                    if(annee1.isChecked()){
                        annee= 1;
                    }else{
                        annee = 2;
                    }
                }

                if(departement == null){
                    textViewDepartement.setError("Choisissez votre département");
                    return;
                }
                Intent signUpThree = new Intent(getBaseContext(), SignUpActivityThree.class);
                startActivityForResult(signUpThree,0);
            }
        });

    }
    }
