package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static com.example.nalone.util.Constants.USER;

public class CheckPhoneValidationActivity extends AppCompatActivity {

    private String password;
    private String field;
    private static final int RC_SIGN_IN = 101;
    private int randomNumber;

    private TextInputEditText inputCode1, inputCode2, inputCode3, inputCode4;

    private User user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_validation);

        inputCode1 = findViewById(R.id.code1);
        inputCode2 = findViewById(R.id.code2);
        inputCode3 = findViewById(R.id.code3);
        inputCode4 = findViewById(R.id.code4);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            password = extras.getString("password");
            field = extras.getString("field");
            user = (User) extras.getSerializable("user");
            TextView infoUser = findViewById(R.id.infoUserCheckValidation);
            infoUser.setText(field);
        }
        try {
            Toast.makeText(this, "ENVOIS DU SMS au " + field, Toast.LENGTH_SHORT).show();
            // Construct data
            String apiKey = "apikey=" + "NzhhNTg2ZjRlYmJiMTA2M2M0OWFjY2MwMDJkOGM0ZTY=";
            Random random = new Random();
            randomNumber = random.nextInt(9999);
            String message = "&message=" + "Votre code d'activation est : " + randomNumber;
            String sender = "&sender=" + "NoLonely";
            String numbers = "&numbers=" + field;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

        } catch (Exception e) {
            System.out.println("Error SMS " + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkCode(View view) {
        createUser();

        /*String code = randomNumber + "";
        String code1 = code.charAt(1) + "";
        if (inputCode1.getText().toString() == "0"
                && inputCode2.getText().toString() == "0"
                && inputCode3.getText().toString() == "0"
                && inputCode4.getText().toString() == "0") {
        } else {
            Toast.makeText(this, "Code incorrect", Toast.LENGTH_SHORT).show();
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUser() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", user.getUid());
        params.putCryptParameter("name", user.getName());
        params.putCryptParameter("pseudo", user.getPseudo());
        params.putCryptParameter("center_interest", user.getCenter_interest());
        params.putCryptParameter("city", user.getCity());
        params.putCryptParameter("description", user.getDescription());
        params.putCryptParameter("mail", "none");
        params.putCryptParameter("phone", user.getNumber());
        params.putCryptParameter("number_events_attend", 0);
        params.putCryptParameter("number_events_create", 0);
        params.putCryptParameter("image_url", null);
        params.putCryptParameter("latitude", user.getLatitude());
        params.putCryptParameter("longitude", user.getLongitude());
        params.putCryptParameter("password", password);
        params.putCryptParameter("is_valid", 1);

        JSONController.getJsonObjectFromUrl(Constants.URL_REGISTER, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                USER = user;
                startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.event_error_create), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur: " + volleyError.toString());
            }
        });
    }


}