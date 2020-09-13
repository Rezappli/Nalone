package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SignUpActivityTwo extends AppCompatActivity {

    ImageView imageViewTC;
    ImageView imageViewGB;
    ImageView imageViewMMI;
    ImageView imageViewINFO;

    private String departement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);

        imageViewTC = (ImageView) findViewById(R.id.imageViewTC);
        imageViewGB = (ImageView) findViewById(R.id.imageViewGB);
        imageViewMMI = (ImageView) findViewById(R.id.imageViewMMI);
        imageViewINFO = (ImageView) findViewById(R.id.imageViewINFO);

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


    }
    }
