package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.nalone.util.Constants.mAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button resetPasswordButton;
    private EditText fieldMailResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordButton = findViewById(R.id.buttonSendResetPassword);
        fieldMailResetPassword = findViewById(R.id.editTextAddressSendResetPassword);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldMailResetPassword.getText().toString().matches("")) {
                    fieldMailResetPassword.setError("Entrez une adresse mail");
                } else if (!fieldMailResetPassword.getText().toString().contains("@") || !fieldMailResetPassword.getText().toString().contains(".")) {
                    fieldMailResetPassword.setError("Entrez une adresse mail valide");
                } else {
                    sendEmail(fieldMailResetPassword.getText().toString());
                }
            }
        });

    }

    public void sendEmail(String mail) {
        mAuth.sendPasswordResetEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Le mail de récupération vient de partir ! Vérifiez vos spams", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}