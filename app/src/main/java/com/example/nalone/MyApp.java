package com.example.nalone;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nalone.util.Constants;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.user_id;
import static com.example.nalone.util.Constants.user_mail;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseReference id_evenements = Constants.firebaseDatabase.getReference("id_evenements");
        id_evenements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nb_evenements = Integer.parseInt((String) snapshot.getValue());
                for(int i = 0; i < nb_evenements; i++){
                    DatabaseReference eventRef = Constants.firebaseDatabase.getReference("evenements/"+i);
                    eventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Evenement e = snapshot.getValue(Evenement.class);
                            LatLng location = getLocationFromAddress(e.getAdresse()+","+e.getVille());
                                    Constants.markers.add(new MarkerOptions().position(location).title(e.getNom()).snippet(e.getDescription())
                                            .icon(null));
                                    Constants.events.add(e);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        SystemClock.sleep(2000);
    }

    private LatLng getLocationFromAddress(String strAddress) {

        Log.w("Location", "Loading coordinate from : " + strAddress);

        Geocoder coder = new Geocoder(SplashActivity.activity);
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