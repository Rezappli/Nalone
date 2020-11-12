package splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.listeners.CoreListener;
import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.profil.ParametresFragment;
import com.example.nalone.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_LATLNG;
import static com.example.nalone.util.Constants.USER_STORAGE_REF;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.listeners;
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
        init();
    }

    public void init() {

        //addUser();
        Constants.application = getApplication();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (!load) {
                mStoreBase.collection("users")
                        .whereEqualTo("mail", currentUser.getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                     if(task.getResult().size() > 0) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            USER = document.toObject(User.class);
                                        }
                                        //USER_REFERENCE = task.getResult().ge
                                        USER_ID = USER.getUid();
                                        USER_STORAGE_REF = mStore.getReference("users").child(USER.getUid());
                                        USER_LATLNG = getLocationFromAddress(USER.getCity());
                                        Log.w("SPLASH", "City : " + USER_LATLNG.toString());
                                        load = true;
                                        setUpRealTime();
                                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                                    }else{
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
            }
        } else {
            load = true;
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

    public void setUpRealTime(){
        mStoreBase.collection("users").document(USER.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("REALTIME", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("REALTIME", "Current data: " + snapshot.getData());
                    USER = snapshot.toObject(User.class);
                    for(CoreListener listener : listeners){
                        listener.onDataChangeListener();
                    }
                } else {
                    Log.d("REALTIME", "Current data: null");
                }
            }
        });
    }

    public void addUser(){
        User u = new User(UUID.randomUUID().toString(), "Rezai", "Mathis", "H", "Lannion","0"
        ,"mathisrezai2k@gmail.com", "Informatique", null,
                "Breton", "25/08/2001");

        mStoreBase.collection("users").document(u.getUid())
                .set(u)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error writing document", e);
                    }
                });
    }

    private LatLng getLocationFromAddress(String strAddress) {

        Log.w("Location", "Loading coordinate from : " + strAddress);

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}

