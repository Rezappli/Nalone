package com.example.nalone.signUpActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nalone.HomeActivity;
import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;

public class WelcomeActivity extends AppCompatActivity {


    private ImageView imageViewPhotoProfil;
    private TextInputEditText signUpDescription;
    private LinearLayout linearLayoutBackgroundPP;
    private Button signupNext;
    private boolean hasSelectedImage = false;
    private Uri imageUri = null;


    static final int RESULT_LOAD_IMG = 1;

    private String id_users;
    private String signUpDescriptionEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        imageViewPhotoProfil = findViewById(R.id.signupPhotoProfil);
        linearLayoutBackgroundPP = findViewById(R.id.signupBgPhotoProfil);
        signupNext = findViewById(R.id.signUpNextEnd);
        signUpDescription = findViewById(R.id.signUpDescription);

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
                if (!hasSelectedImage) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                    builder.setMessage("Vous n'avez pas séléctionné de photo de profil ! Voulez-vous continuer ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create();
                    builder.show();
                } else {
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            imageUri = data.getData();
            assert imageUri != null;
            Glide.with(this).load(imageUri).fitCenter().centerCrop().into(imageViewPhotoProfil);
            hasSelectedImage = true;
        } else {
            Toast.makeText(getApplicationContext(), "Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
