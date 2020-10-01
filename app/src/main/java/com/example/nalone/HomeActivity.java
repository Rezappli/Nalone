package com.example.nalone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    public static String user_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recherche, R.id.navigation_amis, R.id.navigation_evenements, R.id.navigation_messages)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        checkUserRegister();

    }

    private void checkUserRegister() {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            user_mail = acct.getEmail();
        }

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
                                if (mail.equalsIgnoreCase(user_mail)) {
                                    final String user_id = finalI+"";
                                    user_mail = mail;
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("authentification/");
                                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (!dataSnapshot.hasChild(user_id)) {
                                                Intent intent = new Intent(getBaseContext(), SignUpInformationActivity.class);
                                                startActivityForResult(intent, 0);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
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


        user_mail = user_mail.replace(".", ",");
        Log.w("User","User mail:" + user_mail);
    }

    public void getGoogleInformations(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            
            String personEmail = acct.getEmail();
            acct.getPhotoUrl();
            String personId = acct.getId();
            System.out.println("**** INFORMATIIONS ****");
            System.out.println(personName);
            System.out.println(personGivenName);
            System.out.println(personFamilyName);
            System.out.println(personEmail);
            System.out.println(personId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ErrorClass.checkInternetConnection();
    }
}