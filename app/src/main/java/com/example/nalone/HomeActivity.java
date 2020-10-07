package com.example.nalone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nalone.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.example.nalone.util.Constants.user_mail;
import static com.example.nalone.util.Constants.user_id;

public class HomeActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constants.NALONE_BUNDLE != null){
            Log.w("Fragment", "Lecture d'un save instance");
            String s = Constants.NALONE_BUNDLE.getString("MyArrayList");
            Log.w("Fragment", "Valeur : "+s);
        }

        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();
        setContentView(R.layout.activity_home);




        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recherche, R.id.navigation_amis, R.id.navigation_evenements, R.id.navigation_messages)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        checkUserRegister();
    }


    private void checkUserRegister() {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            user_mail = acct.getEmail();
        }

        if(user_mail == null || user_id == null){
            DatabaseReference id_users = Constants.firebaseDatabase.getReference("id_users");
            id_users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String id_users_text = snapshot.getValue(String.class);
                    int nb_users = Integer.parseInt(id_users_text);
                    for (int i = 0; i < nb_users; i++) {

                        DatabaseReference authentificationRef = Constants.firebaseDatabase.getReference("authentification/" + i);
                        final int finalI = i;
                        authentificationRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                String mail = snapshot.child("mail").getValue(String.class);
                                if (mail.equalsIgnoreCase(user_mail)) {
                                    user_id = finalI+"";
                                    user_mail = mail;
                                    DatabaseReference rootRef = Constants.firebaseDatabase.getReference("authentification/");
                                    final String finalUser_id = user_id;
                                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.hasChild(finalUser_id)) {
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

                        if(i == nb_users){
                            if(user_id == null) {
                                Log.w("Connexion", "User inconnu dans la base");
                                Intent intent = new Intent(getBaseContext(), SignUpInformationActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


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
        Constants.COUNTER = 0;
        ErrorClass.checkInternetConnection();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Constants.COUNTER ++;
        if(Constants.COUNTER == 1){
            CustomToast t = new CustomToast(getBaseContext(), "Appuyer de nouveau pour quitter", false, true);
            t.show();
        }else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            CustomToast t = new CustomToast(getBaseContext(), "Appuyer de nouveau pour quitter", false, true);
            t.show();    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}