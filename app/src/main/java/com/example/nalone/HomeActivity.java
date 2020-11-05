package com.example.nalone;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.nalone.signUpActivities.SignUpInformationActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_LATLNG;
import static com.example.nalone.util.Constants.currentUser;


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

        t = new CustomToast(this, "Appuyer de nouveau pour quitter", false, true);


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
        //checkNotification();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    private void checkNotification() {
        Notification.SystemService = getSystemService(NotificationManager.class);
        final DatabaseReference notification = mFirebase.getReference("notifications/"+user_id);
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
    }*/


    private void checkUserRegister() {
        boolean found = false;
        for(int i = 0; i < USERS_LIST.size(); i++){
            User u = USERS_LIST.get(i+"");
            if(u.getMail().equalsIgnoreCase(currentUser.getEmail())){
                found = true;
                USER_ID = ""+i;
                USER_LATLNG = getLocationFromAddress(u.getVille());
                break;
            }
        }


        if(!found){
            startActivity(new Intent(HomeActivity.this, SignUpInformationActivity.class));
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

    private LatLng getLocationFromAddress(String strAddress) {

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