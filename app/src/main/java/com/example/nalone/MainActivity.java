package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import java.io.FileWriter;
import java.io.IOException;

import static com.example.nalone.util.Constants.settingsFile;
import static com.example.nalone.util.Constants.user_mail;
import static com.example.nalone.util.Constants.user_id;

public class MainActivity extends AppCompatActivity {
    private TextView textViewSinscrire;
    private TextView textViewConnexion;
    private EditText editTextPass;
    private EditText editTextAddress;

    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();



        /*Google Sign-In method*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setContentView(R.layout.activity_main);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
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
                if(!textAddress.equalsIgnoreCase("") && !textPass.equalsIgnoreCase("")) {
                    DatabaseReference id_users = FirebaseDatabase.getInstance().getReference("id_users");
                    id_users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String id_users_text = snapshot.getValue(String.class);
                            int nb_users = Integer.parseInt(id_users_text);

                            for (int i = 0; i < nb_users; i++) {
                                DatabaseReference authentificationRef = FirebaseDatabase.getInstance().getReference("authentification/" + i);
                                final int finalI = i;
                                authentificationRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String mail = snapshot.child("mail").getValue(String.class);
                                        String password = snapshot.child("password").getValue(String.class);
                                        boolean mailFound = false;
                                        if (mail.equalsIgnoreCase(textAddress)) {
                                            mailFound = true;
                                            if (password.equalsIgnoreCase(textPass)) {
                                                user_mail = mail;
                                                writeSettingsData(mail+";"+password);
                                                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                                startActivityForResult(intent, 0);
                                            } else {
                                                CustomToast toast = new CustomToast(getBaseContext(), "Mot de passe incorrect !", false, true);
                                            }
                                        }

                                        if (!mailFound) {
                                            CustomToast toast = new CustomToast(getBaseContext(), "Adresse mail introuvable !", false, true);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }

    private void writeSettingsData(String data) {
        try {
            FileWriter myWriter = new FileWriter(settingsFile);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 0) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
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