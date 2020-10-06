package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
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
    private static final String CHANNEL_ID = "0";
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


        Notification.SystemService = getSystemService(NotificationManager.class);

        Notification notif = new Notification(getBaseContext(), "NoLonely - Ajout d'un événmenet", "Un événement publique vient d'être ajouté !", NotificationCompat.PRIORITY_DEFAULT, R.drawable.ic_launcher_foreground);
        notif.show();
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
                    DatabaseReference id_users = FirebaseDatabase.getInstance().getReference("id_users");
                    id_users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String id_users_text = snapshot.getValue(String.class);
                            int nb_users = Integer.parseInt(id_users_text);
                            Log.w("Connexion", "Nb users :"+nb_users);

                            for(int i = 0; i < nb_users; i++) {
                                DatabaseReference authentificationRef = FirebaseDatabase.getInstance().getReference("authentification/"+i);
                                final int finalI = i;
                                authentificationRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String mail = snapshot.child("mail").getValue(String.class);
                                        String password = snapshot.child("password").getValue(String.class);
                                        Log.w("Connexion", "Mail check :"+mail);
                                        Log.w("Connexion", "avec :"+textAddress);
                                        boolean mailFound = false;
                                        if(mail.equalsIgnoreCase(textAddress)){
                                            Log.w("Connexion", "Mail check trouvé");
                                            Log.w("Connexion", "Mail check trouvé:"+textAddress);
                                            mailFound = true;
                                            if(password.equalsIgnoreCase(textPass)){
                                                HomeActivity.user_mail = mail;
                                                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                                startActivityForResult(intent, 0);
                                            }else{
                                                CustomToast toast = new CustomToast(getBaseContext(), "Mot de passe incorrect !", false, true);
                                            }
                                        }

                                        if(!mailFound){
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
    });
        };
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