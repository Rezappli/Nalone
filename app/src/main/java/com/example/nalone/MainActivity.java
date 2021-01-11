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

import com.example.nalone.objects.User;
import com.example.nalone.signUpActivities.SignUpInformationActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.USER_STORAGE_REF;
import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.mAuth;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

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
                .requestIdToken(getString(R.string.default_web_client_id))
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
                startActivityForResult(new Intent(getBaseContext(), ResetPasswordActivity.class), 0);
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

        if(currentUser != null){
            editTextAddress.setText(currentUser.getEmail());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                connectUserFromGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("GoogleSignIn", "Google sign in failed", e);
            }
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

                                if (currentUser.isEmailVerified()) {
                                    loadUser();
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
                                Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }

    public void connectUserFromGoogle(String token){
        Log.w("Google", "Go here");
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.w("Google", "Result");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mStoreBase.collection("users").whereEqualTo("mail", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(!task.getResult().isEmpty()) {
                                            currentUser = mAuth.getCurrentUser();
                                            loadUser();
                                        }else{
                                            startActivity(new Intent(MainActivity.this, SignUpInformationActivity.class));
                                            Toast.makeText(MainActivity.this, "Vous n'êtes pas inscrit !", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MainActivity.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

    private void loadUser(){
        mStoreBase.collection("application").document("maintenance").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.getResult().getBoolean("isMaintenance")){
                    mStoreBase.collection("users")
                            .whereEqualTo("mail", currentUser.getEmail())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().size() > 0) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                USER = document.toObject(User.class);
                                            }
                                            USER_ID = USER.getUid();
                                            MyFirebaseInstance.user_id = USER_ID;
                                            USER_STORAGE_REF = mStore.getReference("users").child(USER.getUid());

                                            USER_REFERENCE = mStoreBase.collection("users").document(USER.getUid());
                                            if(!USER.isBan()) {
                                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                            }else{
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setMessage("Votre compte à été suspendu pour la raison : \n\n"+USER.getBanReason() + "\n\nJusqu'au : " + USER.getTime_ban().toDate().toString())
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                                builder.setCancelable(false);
                                                builder.create();
                                                builder.show();
                                            }
                                        }
                                    } else {
                                        Log.d("SPLASH", "Error getting documents: ", task.getException());
                                        Toast.makeText(MainActivity.this, "Une erreur est survenue !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SPLASH", "Erreur : " + e.getMessage());
                        }
                    });
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Désolé une maintenance est en cours...")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}


