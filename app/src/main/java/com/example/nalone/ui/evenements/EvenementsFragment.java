package com.example.nalone.ui.evenements;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.nalone.Evenement;
import com.example.nalone.HomeActivity;
import com.example.nalone.R;
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

public class EvenementsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;
    private int nb_evenements;

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
        Log.w("Map", "Waiting data...");

        getFromFirebase(new OnDataReceiveCallback(){
            public void onDataReceived(List<Evenement> listEvenements){
                Log.w("Map", "List receive !");
                Log.w("Map", "Events number : " + listEvenements.size());
                mMap.clear();
                for(int i = 0; i < listEvenements.size(); i++) {
                    final Evenement e = listEvenements.get(i);

                    final LatLng location = getLocationFromAddress(e.getAdresse()+","+e.getVille());
                    Log.w("Map", "Event name : " + e.getNom());
                    BitmapDescriptor iconColor = null;
                    if(e.getVisibilite().equals(Visibilite.PUBLIC)) {
                        iconColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    }else{
                        iconColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                    }
                    mMap.addMarker(new MarkerOptions().position(location).title(e.getNom()).snippet(e.getDescription())
                            .icon(iconColor));

                    Log.w("Map", "Marker add !");
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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference id_evenements = database.getReference("id_evenements");
        id_evenements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nb_evenements = Integer.parseInt((String) snapshot.getValue());
                Log.w("Map", "Event found : " + nb_evenements);
                final List<Evenement> listEvenements = new ArrayList<>();
                for(int i = 0; i < nb_evenements; i++){
                    DatabaseReference event = database.getReference("evenements").child(i+"");
                    final int finalI = i;
                    event.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int id = finalI;
                            String nom = snapshot.child("nom").getValue( String.class );
                            String desc = snapshot.child("description").getValue( String.class );
                            String adresse = snapshot.child("adresse").getValue(String.class);
                            String visibiliteValue = snapshot.child("visibilite").getValue(String.class);
                            Visibilite visibilite;
                            if(visibiliteValue.equalsIgnoreCase("PRIVE")){
                                visibilite = Visibilite.PRIVE;

                            }else{
                                visibilite = Visibilite.PUBLIC;
                            }

                            String ville = snapshot.child("ville").getValue(String.class);

                            Evenement e = new Evenement(id, nom, desc, adresse, ville, visibilite, null, null);
                            for(int j = 0; j < listEvenements.size(); j++){
                                if(listEvenements.get(j).getId() == id){
                                    listEvenements.remove(j);
                                }
                            }

                            if(visibilite.equals(Visibilite.PRIVE)) {
                                String membres_inscrits_text = snapshot.child("membres_inscrits").getValue(String.class);
                                List<String> membres_inscrits = Arrays.asList(membres_inscrits_text.split(","));
                                for (int h = 0; h < membres_inscrits.size(); h++) {
                                    Log.w("Membre", "Id user login:"+ HomeActivity.user_id);
                                    Log.w("Membre", "Event member check:"+ membres_inscrits.get(h));
                                    /*if(HomeActivity.user_id.equalsIgnoreCase(membres_inscrits.get(h))) {
                                        listEvenements.add(e);
                                    }*/
                                }
                            }else{
                                listEvenements.add(e);
                            }



                            Log.w("Map", "List size before sending :" +listEvenements.size());
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

        Log.w("Evenement", "Loading coordinate from : " + strAddress);

        Geocoder coder = new Geocoder(getContext());
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