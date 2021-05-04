package com.example.nalone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.signUpActivities.SignUpMainActivity;
import com.example.nalone.util.Constants;
import com.example.nalone.util.CryptoUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.currentUser;

public class MainActivity extends AppCompatActivity {
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
                launchForgetPasswordActivity();
            }
        });


        textViewSinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRegisterActivity();
            }
        });

        textViewConnexion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final String textAddress = editTextAddress.getText().toString();
                final String textPass = editTextPass.getText().toString();

                if (textAddress.equalsIgnoreCase("")) {
                    editTextAddress.setError(getResources().getString(R.string.enter_address_error));
                    return;
                }

                if (textPass.equalsIgnoreCase("")) {
                    editTextPass.setError(getResources().getString(R.string.enter_password_error));
                    return;
                }

                if (!textAddress.equalsIgnoreCase("") && !textPass.equalsIgnoreCase("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Log.w("Response", "Try to connect client");
                    connectUser(textAddress, textPass);
                }
            }
        });

        if (currentUser != null) {
            editTextAddress.setText(currentUser.getEmail());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            connectUserFromGoogle(account);
        } catch (ApiException e) {
            Log.w("GoogleError", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void connectUser(final String mail, final String pass) {

        final SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPreferences.edit();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("mail", mail);
        params.putCryptParameter("password", pass);

        JSONController.getJsonObjectFromUrl(Constants.URL_SIGN_IN, MainActivity.this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    try {
                        editor.putString("mail", CryptoUtils.encrypt(mail));
                        editor.putString("password", CryptoUtils.encrypt(pass));
                        editor.apply();
                        loadUserData(jsonObject);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        Log.w("Response", "Erreur:" + e.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                }else if(jsonObject.length() == 4){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.mail_not_verified), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Mail not verified");
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.mail_or_password_incorrect), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur : " + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserData(JSONObject json) throws JSONException {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", json.getString("uid"));

        JSONController.getJsonObjectFromUrl(Constants.URL_ME, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                USER = (User) JSONController.convertJSONToObject(jsonObject, User.class);
                launchHomeActivity();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void connectUserFromGoogle(final GoogleSignInAccount account) {
        final SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPreferences.edit();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("mail", account.getEmail());
        params.putCryptParameter("type", "Google");
        params.putCryptParameter("statut", "OK");
        params.putCryptParameter("key", Constants.g_key);

        Log.w("params", params.toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_SIGN_IN, MainActivity.this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    try {
                        editor.putString("connection", "google");
                        editor.putString("mail", CryptoUtils.encrypt(account.getEmail()));
                        editor.apply();
                        loadUserData(jsonObject);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        Log.w("Response", "Erreur:" + e.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                }else if(jsonObject.length() == 4){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.mail_not_verified), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Mail not verified");
                    progressBar.setVisibility(View.GONE);
                }else if(jsonObject.length() == 5){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.not_register), Toast.LENGTH_SHORT).show();
                    launchRegisterActivity();
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.mail_or_password_incorrect), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur : " + volleyError.toString());
            }
        });
    }

    private void launchHomeActivity() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    private void launchRegisterActivity() {
        startActivity(new Intent(MainActivity.this, SignUpMainActivity.class));
    }

    private void launchForgetPasswordActivity() {
        startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
    }


    @Override
    public void onBackPressed() {}
}


