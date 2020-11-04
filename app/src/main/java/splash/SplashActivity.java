package splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.CoreListener;
import com.example.nalone.Evenement;
import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.example.nalone.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.nalone.util.Constants.EVENTS_DB_REF;
import static com.example.nalone.util.Constants.EVENTS_LIST;
import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mAuth;
import static com.example.nalone.util.Constants.widthScreen;
import static com.example.nalone.util.Constants.load;

public class SplashActivity extends AppCompatActivity {

    private GoogleSignInAccount acct = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        widthScreen = getResources().getDisplayMetrics().widthPixels;
        heightScreen = getResources().getDisplayMetrics().heightPixels;
        init();
    }

    public void init() {
        currentUser = mAuth.getCurrentUser();
        EVENTS_DB_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Evenement e = ds.getValue(Evenement.class);
                    EVENTS_LIST.put(ds.getKey(), e);
                }

                USERS_DB_REF.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            User u = ds.getValue(User.class);
                            USERS_LIST.put(ds.getKey(), u);
                        }

                        for(CoreListener listener : listeners) {
                            if (listener != null) {
                                listener.onDataChangeListener();
                            }
                        }

                        if(!load) {
                            if (currentUser != null) {
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            load = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }

}