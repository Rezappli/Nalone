package com.nolonely.mobile.ui.profil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.ResetPasswordActivity;
import com.nolonely.mobile.json.JSONController;
import com.nolonely.mobile.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.util.Constants;

import org.json.JSONObject;

import static com.nolonely.mobile.util.Constants.USER;


public class EditActivity extends AppCompatActivity {


    TextView profilEditPassword, textProfilEditMail, profilEditMail;
    EditText profilEditNumero, profilEditPrenom, profilEditNom, profilEditVille, profilEditDate;
    Button profilEditValider;
    ImageView buttonBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        profilEditPassword = findViewById(R.id.profilEditPassword);
        profilEditDate = findViewById(R.id.profilEditNaissance);
        profilEditMail = findViewById(R.id.profilEditMail);
        textProfilEditMail = findViewById(R.id.textProfilEditMail);
        profilEditNom = findViewById(R.id.profilEditNom);
        profilEditPrenom = findViewById(R.id.profilEditPrenom);
        profilEditNumero = findViewById(R.id.profilEditNumero);
        profilEditVille = findViewById(R.id.profilEditVille);
        profilEditValider = findViewById(R.id.profilEditValider);

        profilEditNom.setText(USER.getName());
        textProfilEditMail.setText(USER.getMail());
        profilEditNumero.setText(USER.getNumber());
        profilEditVille.setText(USER.getCity());
        textProfilEditMail.setText(USER.getMail());
        Log.w("Data", "Mail:" + USER.getMail());

        profilEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ResetPasswordActivity.class));
            }
        });

        profilEditValider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String nom = profilEditNom.getText().toString();
                String prenom = profilEditPrenom.getText().toString();
                String ville = profilEditVille.getText().toString();
                String date = profilEditDate.getText().toString();
                String num = profilEditNumero.getText().toString();
                boolean error = false;
                if (nom.equalsIgnoreCase("")) {
                    profilEditNom.setError("Entrez votre nom");
                    error = true;
                }

                if (prenom.equalsIgnoreCase("")) {
                    profilEditPrenom.setError("Entrez votre prénom");
                    error = true;
                }

                if (date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
                    profilEditDate.setError("Entrez votre date de naissance");
                    error = true;
                }

                if (num.length() != 13) {
                    profilEditNumero.setError("Entrez votre numéro");
                    error = true;
                }

                if (ville.equalsIgnoreCase("")) {
                    profilEditVille.setError("Entrez votre ville");
                    error = true;
                }

                if (!error) {
                    if (!USER.getName().equalsIgnoreCase(nom) ||
                            !USER.getCity().equalsIgnoreCase(ville) || !USER.getNumber().equalsIgnoreCase(num)
                    ) {

                        if (!USER.getName().equalsIgnoreCase(nom)) {
                            USER.setName(nom);
                        }


                        if (!USER.getCity().equalsIgnoreCase(ville)) {
                            USER.setCity(ville);
                        }

                        if (!USER.getNumber().equalsIgnoreCase(num)) {
                            USER.setNumber(num);
                        }


                        updateDescription();
                    } else {
                        startActivity(new Intent(getBaseContext(), ProfilActivity.class));
                    }
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDescription() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("name", USER.getName());
        params.putCryptParameter("city", USER.getCity());
        params.putCryptParameter("description", USER.getDescription());
        params.putCryptParameter("latitude", USER.getLatitude());
        params.putCryptParameter("longitude", USER.getLongitude());

        JSONController.getJsonObjectFromUrl(Constants.URL_UPDATE_ME, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.update_description), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), ProfilActivity.class));
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
            }
        });
    }
}