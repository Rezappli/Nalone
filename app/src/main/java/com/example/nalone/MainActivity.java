package com.example.nalone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView textViewSinscrire;
    private TextView textViewConnexion;
    private EditText editTextPass;
    private EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewSinscrire = (TextView) findViewById(R.id.textView3);
        textViewConnexion = (TextView) findViewById(R.id.buttonConnexion);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPass = (EditText) findViewById(R.id.editTextPassword);


        textViewSinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(getBaseContext(), SignUpActivity.class);
                startActivityForResult(signUp, 0);
            }
        });

        textViewConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textAddress = editTextAddress.getText().toString();
                String textPass = editTextPass.getText().toString();

                if(textAddress.matches("")){
                    editTextAddress.setError("Entrez votre adresse");
                    return;
                }

                if(textPass.matches("")){
                    editTextPass.setError("Entrez votre mot de passe");
                    return;
                }

                try {
                    String result = getContent(textAddress, textPass);
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

              /*  Intent signUp = new Intent(getBaseContext(), HomeActivity.class);
                startActivityForResult(signUp, 0);*/
            }
        });
    }

    private String getContent(String address, String pass) throws IOException {
        URL url = new URL("localhost/noLonely/connection.php?m="+address+"&&p="+pass);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = "", line;
        while ((line = rd.readLine()) != null) {
            content += line + "\n";
        }

        return content;
    }
}