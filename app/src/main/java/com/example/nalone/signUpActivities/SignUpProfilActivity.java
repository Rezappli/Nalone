package com.example.nalone.signUpActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nalone.ErrorClass;
import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.mAuth;

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

        final DatabaseReference id_user = Constants.mFirebase.getReference("id_users/");
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
        SignUpInformationActivity.user.setDescription(signUpDescriptionEnter);

        ErrorClass.checkInternetConnection();

        signInUser(SignUpInformationActivity.user.getMail(), SignUpInformationActivity.password);

        USERS_LIST.put(USERS_LIST.size()+"", SignUpInformationActivity.user);

        USERS_DB_REF.setValue(USERS_LIST);

        Toast.makeText(this, "Bienvenue dans NoLonely !", Toast.LENGTH_SHORT).show();

        Intent welcomeIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivityForResult(welcomeIntent,0);
    }

    private void signInUser(String mail, String pass){
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendVerificationEmail();
                        } else {
                            Toast.makeText(SignUpProfilActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpProfilActivity.this, "Veuillez vérifiez votre adresse mail !",
                                    Toast.LENGTH_SHORT).show();
                        }

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