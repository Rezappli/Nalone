package com.example.nalone;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.nalone.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import static com.example.nalone.util.Constants.firebaseDatabase;
import static com.example.nalone.util.Constants.user_mail;
import static com.example.nalone.util.Constants.user_id;


public class HomeActivity extends AppCompatActivity{

    private CustomToast t;
    private Handler h = new Handler();
    private Runnable r;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ErrorClass.activity = this;
        ErrorClass.checkInternetConnection();
        setContentView(R.layout.activity_home);

        t = new CustomToast(this, "Appuyer de nouveau pour quitter", false, true);;


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recherche, R.id.navigation_amis, R.id.navigation_evenements, R.id.navigation_messages)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

       r = new Runnable() {
            @Override
            public void run() {
                if(t.isShow()){
                    t.setShow(false);
                }
            }
        };

        checkUserRegister();
        checkNotification();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkNotification() {
        Notification.SystemService = getSystemService(NotificationManager.class);
        final DatabaseReference notification = firebaseDatabase.getReference("notifications/"+user_id);
        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NotificationData> listNotifications = (List<NotificationData>) snapshot.getValue(List.class);

                if (listNotifications != null) {
                    for (int i = 0; i < listNotifications.size(); i++) {
                        Notification notif = new Notification(getBaseContext(), listNotifications.get(i));
                        notif.show();
                    }

                    notification.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkUserRegister() {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Log.w("connexion", "connexion par google");
            user_mail = acct.getEmail().replace(".", ",");
        }

        if(user_mail == null || user_id == null){
            Log.w("connexion", "connexion par email & mdp");
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
                                    user_mail = mail.replace(".", ",");
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
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ErrorClass.checkInternetConnection();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        h.postDelayed(r, 3500);
        if(!t.isShow()){
            t.show();
        }else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}