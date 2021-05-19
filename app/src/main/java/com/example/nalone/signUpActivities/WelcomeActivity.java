package com.example.nalone.signUpActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.nalone.HomeActivity;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;

public class WelcomeActivity extends AppCompatActivity {


    private ImageView imageViewPhotoProfil;
    private TextInputEditText signUpDescription;
    private LinearLayout linearLayoutBackgroundPP;
    private Button signupNext;
    private boolean hasSelectedImage = false;
    private Uri imageUri = null;
    private TextView textViewIgnore, textViewPhoto;


    static final int RESULT_LOAD_IMG = 1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }

        imageViewPhotoProfil = findViewById(R.id.signupPhotoProfil);
        imageViewPhotoProfil.setOnClickListener(v -> choosePhoto());
        linearLayoutBackgroundPP = findViewById(R.id.signupBgPhotoProfil);
        signupNext = findViewById(R.id.signUpNextEnd);
        signUpDescription = findViewById(R.id.signUpDescription);
        textViewPhoto = findViewById(R.id.textViewPhoto);
        textViewPhoto.setOnClickListener(v -> choosePhoto());

        textViewIgnore = findViewById(R.id.textViewIgnore);
        textViewIgnore.setOnClickListener(v -> {
            startHomeActivity();
        });

        signupNext.setOnClickListener(view -> {
            if (!hasSelectedImage) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setMessage("Vous n'avez pas séléctionné de photo de profil ! Voulez-vous continuer ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(DialogInterface dialog, int id) {
                                checkDescription();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create();
                builder.show();
            } else {
                startHomeActivity();
            }
        });
    }

    private void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkDescription() {
        if (!signUpDescription.getText().toString().matches("")) {
            user.setDescription(signUpDescription.getText().toString());
            JSONObjectCrypt params = new JSONObjectCrypt();

            params.putCryptParameter("name", user.getName());
            params.putCryptParameter("city", user.getCity());
            params.putCryptParameter("description", user.getDescription());
            params.putCryptParameter("latitude", user.getLatitude());
            params.putCryptParameter("longitude", user.getLongitude());

            JSONController.getJsonObjectFromUrl(Constants.URL_UPDATE_ME, getBaseContext(), params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    startHomeActivity();
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.user_error_create), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Erreur: " + volleyError.toString());
                }
            });
        } else {
            startHomeActivity();
        }
    }

    private void startHomeActivity() {
        USER = user;
        startActivity(new Intent(getBaseContext(), HomeActivity.class));
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

    @Override
    public void onBackPressed() {
    }
}
