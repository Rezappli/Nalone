package splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.ConnectionService;
import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.example.nalone.MyFirebaseInstance;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.profil.ParametresFragment;
import com.example.nalone.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.USER_STORAGE_REF;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.mAuth;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.range;
import static com.example.nalone.util.Constants.widthScreen;
import static com.example.nalone.util.Constants.load;

public class SplashActivity extends AppCompatActivity {

    private GoogleSignInAccount acct = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = this.getSharedPreferences(ParametresFragment.SHARED_PREFS, MODE_PRIVATE);
        range = settings.getInt(ParametresFragment.sharedRange, 50);
    }

    @Override
    public void onStart() {
        super.onStart();
        widthScreen = getResources().getDisplayMetrics().widthPixels;
        heightScreen = getResources().getDisplayMetrics().heightPixels;
        Log.w("Service", "Start service");
        startService(new Intent(this, ConnectionService.class));
        init();

    }

    public void init() {

        Constants.application = getApplication();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (!load) {
                if (currentUser.isEmailVerified()) {
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
                                            Log.w("SPLASH", "City : " + USER.getCity());

                                            USER_REFERENCE = mStoreBase.collection("users").document(USER.getUid());
                                            load = true;
                                            if(!USER.isBan()) {
                                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                                            }else{
                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                            }
                                        } else {
                                            load = true;
                                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                        }
                                    } else {
                                        Log.d("SPLASH", "Error getting documents: ", task.getException());
                                        load = true;
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SPLASH", "Erreur : " + e.getMessage());
                        }
                    });
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }else{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        } else {
            load = true;
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

}

