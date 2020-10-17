package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nalone.util.Constants;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpProfilActivity extends AppCompatActivity {

    private ImageView imageViewPhotoProfil;
    private TextInputEditText signUpDescription;
    private LinearLayout linearLayoutBackgroundPP;
    private Button signupNext;
    private boolean hasSelectedImage = false;
    private Bitmap selectedImage;


    static final int RESULT_LOAD_IMG = 1;

    private String id_users;
    private String signUpDescriptionEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ErrorClass.checkInternetConnection();

        final DatabaseReference id_user = Constants.firebaseDatabase.getReference("id_users/");
        id_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                id_users = dataSnapshot.getValue(String.class);
                Log.d("ID_USERS", "Value is: " + id_users);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ID_USERS", "Failed to read value.", error.toException());
            }
        });

        Log.d("ID_USERS", "Value is: " + id_users);

        setContentView(R.layout.activity_sign_up_profil);


        imageViewPhotoProfil = findViewById(R.id.signupPhotoProfil);
        linearLayoutBackgroundPP = findViewById(R.id.signupBgPhotoProfil);
        signupNext = findViewById(R.id.signUpNext3);
        signUpDescription = findViewById(R.id.signUpDescription);


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
                if (!hasSelectedImage) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SignUpProfilActivity.this);
                    builder.setMessage("Vous n'avez pas séléctionné de photo de profil ! Voulez-vous continuer ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveData();
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create();
                    builder.show();
                }else{
                    saveData();
                }
            }
        });
    }

    private void saveData(){

        signUpDescriptionEnter = signUpDescription.getText().toString();
        SignUpInformationActivity.userData.setDescription(signUpDescriptionEnter);

        ErrorClass.checkInternetConnection();

        DatabaseReference id_user = Constants.firebaseDatabase.getReference("authentification/"+id_users);

        DatabaseReference mailAuth = Constants.firebaseDatabase.getReference("authentification/"+id_users + "/mail");
        mailAuth.setValue(SignUpInformationActivity.userData.getAdresseMail());

        DatabaseReference passwordAuth = Constants.firebaseDatabase.getReference("authentification/"+id_users + "/password");
        passwordAuth.setValue(SignUpInformationActivity.userData.getPass());

        DatabaseReference mail = Constants.firebaseDatabase.getReference("users/" + id_users + "/mail");
        mail.setValue(SignUpInformationActivity.userData.getAdresseMail());

        DatabaseReference nom = Constants.firebaseDatabase.getReference("users/" +id_users + "/nom");
        nom.setValue(SignUpInformationActivity.userData.getNom());

        DatabaseReference prenom = Constants.firebaseDatabase.getReference("users/"+ id_users + "/prenom");
        prenom.setValue(SignUpInformationActivity.userData.getPrenom());

        DatabaseReference sexe = Constants.firebaseDatabase.getReference("users/"+id_users + "/sexe");
        sexe.setValue(SignUpInformationActivity.userData.getSexe());

        DatabaseReference ville = Constants.firebaseDatabase.getReference("users/"+id_users + "/ville");
        ville.setValue(SignUpInformationActivity.userData.getVille());

        DatabaseReference adresse = Constants.firebaseDatabase.getReference("users/"+id_users + "/adresse");
        adresse.setValue(SignUpInformationActivity.userData.getAdresse());

        DatabaseReference date = Constants.firebaseDatabase.getReference("users/"+id_users + "/date");
        date.setValue(SignUpInformationActivity.userData.getDate());

        DatabaseReference numero = Constants.firebaseDatabase.getReference("users/"+id_users + "/numero");
        numero.setValue(SignUpInformationActivity.userData.getNumero());

        DatabaseReference cursus = Constants.firebaseDatabase.getReference("users/"+id_users +  "/cursus");
        cursus.setValue(SignUpInformationActivity.userData.getCursus());

        DatabaseReference interets = Constants.firebaseDatabase.getReference("users/"+id_users + "/interets");
        interets.setValue(SignUpInformationActivity.userData.getCentreInterets());

        DatabaseReference description = Constants.firebaseDatabase.getReference("users/"+id_users + "/description");
        description.setValue(SignUpInformationActivity.userData.getDescription());

        DatabaseReference nbCreate = Constants.firebaseDatabase.getReference("users/"+id_users + "/nombre_creation");
        nbCreate.setValue(SignUpInformationActivity.userData.getNbCreate());

        DatabaseReference nbParticipate = Constants.firebaseDatabase.getReference("users/"+id_users + "/nombre_participation");
        nbParticipate.setValue(SignUpInformationActivity.userData.getNbParticipate());

        DatabaseReference photo_profil = Constants.firebaseDatabase.getReference("users/"+id_users + "/photo_profil");
        photo_profil.setValue(""+Constants.getBytesFromBitmap(selectedImage));

        DatabaseReference id_users_ref = Constants.firebaseDatabase.getReference("id_users");
        int id_users_int = Integer.parseInt(id_users);
        id_users_int++;
        String s = id_users_int + "";
        id_users_ref.setValue(s);

        Intent welcomeIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivityForResult(welcomeIntent,0);
        Toast.makeText(getApplicationContext(), "Bienvenue dans NoLonely !", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                assert imageUri != null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewPhotoProfil.setImageBitmap(selectedImage);
                hasSelectedImage = true;
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