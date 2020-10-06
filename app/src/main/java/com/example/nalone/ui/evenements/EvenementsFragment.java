package com.example.nalone.ui.evenements;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.nalone.CreateEventActivity;
import com.example.nalone.ErrorClass;
import com.example.nalone.Evenement;
import com.example.nalone.HomeActivity;
import com.example.nalone.R;
import com.example.nalone.SignUpInformationActivity;
import com.example.nalone.Visibilite;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.nalone.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.user_mail;
import static com.example.nalone.util.Constants.user_id;

public class EvenementsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;
    private int nb_evenements;
    private ImageView buttonAdd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_evenements, container, false);

        mMapView = rootView.findViewById(R.id.mapView);

        getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
        getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);


        mMapView.getMapAsync(this);


        initGoogleMap(savedInstanceState);

        buttonAdd = rootView.findViewById(R.id.button2);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEvent = new Intent(getActivity().getBaseContext(), CreateEventActivity.class);
                startActivityForResult(createEvent,0);
            }
        });

        return rootView;
    }



    private void initGoogleMap(Bundle savedInstanceState) {

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Bundle mapViewBundel = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundel == null) {
            mapViewBundel = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng laval = new LatLng(48.0785146,-0.7669906);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laval, 13	));
        Log.w("Map", "Waiting data...");
        getFromFirebase(new OnDataReceiveCallback(){
            public void onDataReceived(List<Evenement> listEvenements){
                mMap.clear();
                Log.w("Map", "Data received");
                for(int i = 0; i < listEvenements.size(); i++) {

                    Evenement e = listEvenements.get(i);
                    LatLng location = getLocationFromAddress(e.getAdresse()+","+e.getVille());
                    Log.w("Map", "Event name : " + e.getNom());
                    Log.w("Map", "Event visibilite : " + e.getVisibilite());
                    Log.w("Map", "Event membres_inscrits : " + e.getMembres_inscrits().toString());
                    Log.w("Map", "Event adresse : " + e.getAdresse());
                    Log.w("Map", "Event ville : " + e.getVille());
                    Log.w("Map", "Event location : " + location.toString());

                    mMap.addMarker(new MarkerOptions().position(location).title(e.getNom()).snippet(e.getDescription())
                            .icon(e.getCouleur_icone()));

                }
            }

        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    private void getFromFirebase(final OnDataReceiveCallback callback){
        final List<Evenement> listEvenements = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference id_evenements = database.getReference("id_evenements");
        id_evenements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nb_evenements = Integer.parseInt((String) snapshot.getValue());
                Log.w("User", "ID User connecte : " +user_id);
                Log.w("User", "Mail User connecte : "+user_mail);
                Log.w("Map", "Event found : " + nb_evenements);
                for(int i = 0; i < nb_evenements; i++){
                    DatabaseReference eventRef = database.getReference("evenements/"+i);
                    eventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Evenement e = snapshot.getValue(Evenement.class);
                            if(e.getVisibilite().equals(Visibilite.PRIVE)) {
                                if(e.getMembres_inscrits().contains(user_id)) {
                                    listEvenements.add(e);
                                }
                            }else{
                                listEvenements.add(e);
                            }
                            callback.onDataReceived(listEvenements);
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


        /*DatabaseReference reference = database.getReference("evenements").child("0");
        reference.addValueEventListener( new ValueEventListener(){
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ){
                String nom = dataSnapshot.child( "nom" ).getValue( String.class );
                String desc = dataSnapshot.child( "desc" ).getValue( String.class );
                callback.onDataReceived(nom, desc);
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ){
            }
        });*/
    }

    @Override
    public void onPause(){
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory(){
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    private LatLng getLocationFromAddress(String strAddress) {

        Log.w("Location", "Loading coordinate from : " + strAddress);

        Geocoder coder = new Geocoder(getActivity());
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