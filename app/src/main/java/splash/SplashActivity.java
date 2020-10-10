package splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.nalone.util.Constants.user_id;
import static com.example.nalone.util.Constants.user_mail;

public class SplashActivity extends AppCompatActivity {

    public static Activity activity;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
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
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                String mail = snapshot.child("mail").getValue(String.class);
                                if (mail.equalsIgnoreCase(acct.getEmail().replace(".", ","))) {
                                    user_id = finalI + "";
                                    user_mail = mail;
                                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();
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

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}