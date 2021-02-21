package com.example.nalone.signUpActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class SignUpInformationActivity extends AppCompatActivity {
    public TextInputEditText nom;
    public TextInputEditText prenom;
    public TextInputEditText ville;
    public TextInputEditText numero;
    public TextInputEditText adresseMail;
    public TextInputEditText pass;
    public TextInputEditText confirmPass;
    public TextInputEditText dateNaissance;

    private Dialog dialogCalendrier;
    private CalendarView calendarDate;
    private Button buttonCalendrier;
    private Calendar calendar;

    private int newDay;
    private int newMonth;
    private int newYears;

    public static String password;

    CardView homme;
    CardView femme;
    ImageView imageH;
    ImageView imageF;
    TextView textH;
    TextView textF;

    boolean bHomme;
    boolean bFemme;

    Button buttonSignUpNext;

    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);

        dialogCalendrier = new Dialog(this);

        user = null;

        buttonSignUpNext = (Button) findViewById(R.id.signUpNext);
        nom =  (TextInputEditText) findViewById(R.id.signupNom);
        prenom = (TextInputEditText)  findViewById(R.id.signupPrenom);
        ville = (TextInputEditText)  findViewById(R.id.signupVille);
        numero = (TextInputEditText)  findViewById(R.id.signupNumero);
        adresseMail = (TextInputEditText) findViewById(R.id.signupMail);
        dateNaissance = (TextInputEditText) findViewById(R.id.signupDate);


        pass =  (TextInputEditText) findViewById(R.id.signupPass);
        confirmPass = (TextInputEditText)  findViewById(R.id.signupConfirmPass);
        homme =  findViewById(R.id.signUpHomme);
        femme = findViewById(R.id.signUpFemme);
        imageF = findViewById(R.id.imageFemme);
        imageH = findViewById(R.id.imageHomme);
        textF = findViewById(R.id.textFemme);
        textH = findViewById(R.id.textHomme);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct != null){
            nom.setText(acct.getFamilyName());
            prenom.setText(acct.getDisplayName());
            adresseMail.setText(acct.getEmail());
        }

        final Drawable customErrorDrawable = getResources().getDrawable(R.drawable.error_icon);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());

        dateNaissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendrier(v);
            }
        });

        homme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageH.setImageResource(R.drawable.signup_homme_focused);
                bHomme = true;
                imageF.setImageResource(R.drawable.signup_femme);
                textH.setTextColor(Color.GRAY);
                textF.setTextColor(Color.GRAY);
            }
        });

        femme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageF.setImageResource(R.drawable.signup_femme_focused);
                bFemme = true;
                imageH.setImageResource(R.drawable.signup_homme);
                textF.setTextColor(Color.GRAY);
                textH.setTextColor(Color.GRAY);
            }
        });


        buttonSignUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomEntre = nom.getText().toString();
                String prenomEntre = prenom.getText().toString();
                String villeEntre = ville.getText().toString();
                String numeroEntre = numero.getText().toString();
                String mailEntre = adresseMail.getText().toString();
                mailEntre = mailEntre.replaceAll(" ", "");
                String passEntre = pass.getText().toString();
                String confirmPassEntre = confirmPass.getText().toString();




               if(bHomme == false && bFemme == false){
                   imageH.setImageResource(R.drawable.signup_homme_erreur);
                   imageF.setImageResource(R.drawable.signup_femme_erreur);
                   textF.setTextColor(Color.RED);
                   textH.setTextColor(Color.RED);
                   Toast.makeText(getApplicationContext(), "Selectionnez votre sexe", Toast.LENGTH_LONG).show();
                   return;
               }

                if (nomEntre.matches("")){
                    nom.setError("Entrez votre nom", customErrorDrawable);
                    return;
                }

                if (prenomEntre.matches("")){
                    prenom.setError("Entrez votre prénom",customErrorDrawable);
                    return;
                }

                if (villeEntre.matches("")){
                    ville.setError("Entrez votre ville",customErrorDrawable);
                    return;
                }

                if (numeroEntre.matches("")){
                    numero.setError("Entrez votre numéro de téléphone",customErrorDrawable);
                    return;
                }

                if(mailEntre.matches("")){
                    adresseMail.setError("Entrez votre adresse mail",customErrorDrawable);
                    return;
                }

                if(passEntre.matches("")){
                    pass.setError("Entrez votre mot de passe",customErrorDrawable);
                    return;
                }

                if(passEntre.contains(confirmPassEntre)){
                    password = confirmPassEntre;
                }else{
                    confirmPass.setError("Le mot de passe ne correspond pas",customErrorDrawable);
                    return;
                }

                LatLng pos = null;

                try{
                    pos = getLocationFromAddress(villeEntre);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(pos == null){
                    ville.setError("Impossible de trouver ce lieu", customErrorDrawable);
                    return;
                }
              String sexe = "";

              if(bHomme){
                  sexe = "Homme";
              }

              if(bFemme){
                  sexe = "Femme";
              }

                user = new User(UUID.randomUUID().toString(), nomEntre, prenomEntre, sexe, villeEntre, numeroEntre, mailEntre, "", dateNaissance.getText().toString());
                user.setLatitude(pos.latitude);
                user.setLongitude(pos.longitude);
                Intent signUpStudy = new Intent(getBaseContext(), SignUpStudiesActivity.class);
                startActivityForResult(signUpStudy, 0);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void showCalendrier(final View v){
        dialogCalendrier.setContentView(R.layout.calendrier);
        calendarDate = dialogCalendrier.findViewById(R.id.calendarView);
        buttonCalendrier = dialogCalendrier.findViewById(R.id.buttonCalendrier);

        calendar = Calendar.getInstance();

        CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                newDay = day;
                newMonth = month;
                newYears = year;
            }
        };

        calendarDate.setOnDateChangeListener(myCalendarListener);


        buttonCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateNaissance.setText(newDay+"-"+newMonth+"-"+newYears);
                dialogCalendrier.dismiss();
            }
        });



        dialogCalendrier.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCalendrier.show();
    }

    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(SignUpInformationActivity.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 10);
            if (address == null) {
                return null;
            }

            if(address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }else{
                return null;
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


}