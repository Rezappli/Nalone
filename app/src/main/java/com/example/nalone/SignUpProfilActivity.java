package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpProfilActivity extends AppCompatActivity {

    ImageView imageViewPhotoProfil;
    LinearLayout linearLayoutBackgroundPP;
    Button signupNext;


    static final int RESULT_LOAD_IMG = 1;

    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = this;
        ErrorClass.checkInternetConnection();


        setContentView(R.layout.activity_sign_up_profil);

        imageViewPhotoProfil = findViewById(R.id.signupPhotoProfil);
        linearLayoutBackgroundPP = findViewById(R.id.signupBgPhotoProfil);
        signupNext = findViewById(R.id.signUpNext3);

       if(SignUpStudiesActivity.departement.equals("MMI")){
            linearLayoutBackgroundPP.setBackgroundResource(R.drawable.custom_mmi);
        }

        if(SignUpStudiesActivity.departement.equals("TC")){
            linearLayoutBackgroundPP.setBackgroundResource(R.drawable.custom_tc);
        }
        if(SignUpStudiesActivity.departement.equals("INFO")){
            linearLayoutBackgroundPP.setBackgroundResource(R.drawable.custom_info);
        }
        if(SignUpStudiesActivity.departement.equals("LP")){
            linearLayoutBackgroundPP.setBackgroundResource(R.drawable.custom_lp);
        }
        if(SignUpStudiesActivity.departement.equals("GB")){
            linearLayoutBackgroundPP.setBackgroundResource(R.drawable.custom_gb);
        }


        imageViewPhotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        signupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ErrorClass.checkInternetConnection();

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference mail = database.getReference("authentification/" + SignUpInformationActivity.userData.getAdresseMail());
                mail.setValue(SignUpInformationActivity.userData.getPass());

                mail = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail());

                DatabaseReference nom = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/nom");
                nom.setValue(SignUpInformationActivity.userData.getNom());

                DatabaseReference prenom = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/prenom");
                prenom.setValue(SignUpInformationActivity.userData.getPrenom());

                DatabaseReference sexe = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/sexe");
                sexe.setValue(SignUpInformationActivity.userData.getSexe());

                DatabaseReference ville = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/ville");
                ville.setValue(SignUpInformationActivity.userData.getVille());

                DatabaseReference adresse = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/adresse");
                adresse.setValue(SignUpInformationActivity.userData.getAdresse());

                DatabaseReference date = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/date");
                date.setValue(SignUpInformationActivity.userData.getDate());

                DatabaseReference numero = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/numero");
                numero.setValue(SignUpInformationActivity.userData.getNumero());

                DatabaseReference cursus = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/cursus");
                cursus.setValue(SignUpInformationActivity.userData.getCursus());

                DatabaseReference interets = database.getReference("users/" + SignUpInformationActivity.userData.getAdresseMail() + "/interets");
                interets.setValue(SignUpInformationActivity.userData.getCentreInterets());




                Intent welcomeIntent = new Intent(getBaseContext(), HomeActivity.class);
                startActivityForResult(welcomeIntent,0);
                Toast.makeText(getApplicationContext(), "Bienvenue dans NoLonely !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                assert imageUri != null;
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

    @Override
    protected void onResume() {
        super.onResume();
        ErrorClass.checkInternetConnection();
    }



}