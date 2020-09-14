package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpActivityThree extends AppCompatActivity {

    ImageView imageViewPhotoProfil;
    CardView cardViewBackgroundPP;
    Spinner spinnerCentreInteret1;
    Spinner spinnerCentreInteret2;
    Spinner spinnerCentreInteret3;

    String[] centresInteret = { "Musique", "Sport", "Jeux Vidéo", "Livres", "Mangas", "Fête", "Cuisine"};

    static final int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_three);

        imageViewPhotoProfil = findViewById(R.id.signupPhotoProfil);
        cardViewBackgroundPP = findViewById(R.id.signupBgPhotoProfil);
        spinnerCentreInteret1 = (Spinner) findViewById(R.id.signupSpinner1);
        spinnerCentreInteret2 = (Spinner) findViewById(R.id.signupSpinner2);
        spinnerCentreInteret3 = (Spinner) findViewById(R.id.signupSpinner3);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, centresInteret);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentreInteret1.setAdapter(adapter);
        spinnerCentreInteret2.setAdapter(adapter);
        spinnerCentreInteret3.setAdapter(adapter);
      //  spinnerCentreInteret1.setOnItemSelectedListener(this);


        if(SignUpActivityTwo.departement == "MMI"){
            cardViewBackgroundPP.setCardBackgroundColor(Color.parseColor("#b83dba"));
        }

        if(SignUpActivityTwo.departement == "TC"){
            cardViewBackgroundPP.setCardBackgroundColor(Color.parseColor("#00a8f3"));
        }

        if(SignUpActivityTwo.departement == "GB"){
            cardViewBackgroundPP.setCardBackgroundColor(Color.parseColor("#0ed145"));
        }

        if(SignUpActivityTwo.departement == "INFO"){
            cardViewBackgroundPP.setCardBackgroundColor(Color.parseColor("#ec1c24"));
        }

        imageViewPhotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewPhotoProfil.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Une erreur s'est produite",Toast.LENGTH_LONG).show();

            }

        }else {
            Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();

        }
    }
}