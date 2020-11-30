package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.signUpActivities.SignUpInformationActivity;
import com.example.nalone.signUpActivities.SignUpProfilActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.load;
import static com.example.nalone.util.Constants.mAuth;

public class MainActivity extends AppCompatActivity{
    private TextView textViewSinscrire;
    private TextView textViewConnexion;
    private EditText editTextPass;
    private EditText editTextAddress;
    private TextView passwordForget;
    private ProgressBar progressBar;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                signIn();
            }
        });

        textViewSinscrire = findViewById(R.id.buttonQuit);
        textViewConnexion = findViewById(R.id.buttonRetry);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPass = findViewById(R.id.editTextPassword);
        passwordForget = findViewById(R.id.editTextPasswordForget);
        progressBar = findViewById(R.id.progressBar);

        passwordForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), ResetPassword.class), 0);
            }
        });


        textViewSinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(), SignUpInformationActivity.class), 0);
            }
        });

        textViewConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String textAddress = editTextAddress.getText().toString();
                final String textPass = editTextPass.getText().toString();

                if (textAddress.equalsIgnoreCase("")) {
                    editTextAddress.setError("Entrez votre adresse");
                    return;
                }

                if (textPass.equalsIgnoreCase("")) {
                    editTextPass.setError("Entrez votre mot de passe");
                    return;
                }

                if(!textAddress.equalsIgnoreCase("") && !textPass.equalsIgnoreCase("")){
                    progressBar.setVisibility(View.VISIBLE);
                    connectUser(textAddress, textPass);
                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = new Intent(this, HomeActivity.class);
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
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
            Log.w("GoogleError", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void connectUser(String mail, String pass) {
            mAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = mAuth.getCurrentUser();

                                Log.w("INSCRIPTION", "current user : " + currentUser.getEmail());
                                Log.w("INSCRIPTION", "email valide : " + currentUser.isEmailVerified());

                                if (currentUser.isEmailVerified()) {
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                } else {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Votre adresse mail n'a pas été vérifiée")
                                            .setPositiveButton("Renvoyer", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    sendVerificationEmail();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            })
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                    builder.create();
                                    builder.show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Erreur : " + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void sendVerificationEmail() {
        Log.w("INSCRIPTION", "Envoye d'un mail!");
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Fiouuuu... Le mail vient de partir !",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Une erreur est survenu : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}