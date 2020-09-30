package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    private TextView textViewSinscrire;
    private TextView textViewConnexion;
    private EditText editTextPass;
    private EditText editTextAddress;

    private GoogleSignInClient mGoogleSignInClient;
    private AppCompatActivity activity;
    private String id_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();

        /*Google Sign-In method*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if(acct != null){
            //GoogleSignIn();
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            startActivityForResult(intent, 0);
        }else {
            setContentView(R.layout.activity_main);
            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.sign_in_button:
                            GoogleSignIn();
                            break;
                    }
                }
            });

            textViewSinscrire = (TextView) findViewById(R.id.buttonQuit);
            textViewConnexion = (TextView) findViewById(R.id.buttonRetry);
            editTextAddress = (EditText) findViewById(R.id.editTextAddress);
            editTextPass = (EditText) findViewById(R.id.editTextPassword);


            textViewSinscrire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ErrorClass.checkInternetConnection();
                    Intent signUp = new Intent(getBaseContext(), SignUpInformationActivity.class);
                    startActivityForResult(signUp, 0);
                }
            });

            textViewConnexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ErrorClass.checkInternetConnection();
                    final String textAddress = editTextAddress.getText().toString().replace(".", ",");
                    final String textPass = editTextPass.getText().toString();

                    if (textAddress.matches("")) {
                        editTextAddress.setError("Entrez votre adresse");
                        return;
                    }

                    if (textPass.matches("")) {
                        editTextPass.setError("Entrez votre mot de passe");
                        return;
                    }
                    //Lecture d'une connexion mail/mdp via bdd
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("authentification/");
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(textAddress)) {
                                DatabaseReference mailRef = FirebaseDatabase.getInstance().getReference("authentification/" + textAddress);
                                mailRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String MailPasswordBind = dataSnapshot.getValue(String.class);
                                        if (textPass.equals(MailPasswordBind)) {
                                            HomeActivity.user_mail = textAddress;
                                            Intent signUp = new Intent(getBaseContext(), HomeActivity.class);
                                            startActivityForResult(signUp, 0);
                                        } else {
                                            editTextPass.setText("");
                                            Toast.makeText(getApplicationContext(), "Le mot de passe est incorrect !", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "L'adresse mail n'existe pas !", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Log.w(mailRef, "Failed to read value.", databaseError.toException());
                        }
                    });
                }
            });
        }
    }

    public void GoogleSignIn(){
        Intent signInIntent =  mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleError", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ErrorClass.checkInternetConnection();
    }
}