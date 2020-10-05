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
        Log.w("Map", "Waiting data...");
        LatLng laval = new LatLng(48.0785146,-0.7669906);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laval, 13	));
        getFromFirebase(new OnDataReceiveCallback(){
            public void onDataReceived(List<Evenement> listEvenements){
                mMap.clear();
                for(int i = 0; i < listEvenements.size(); i++) {
                    final Evenement e = listEvenements.get(i);

                    final LatLng location = getLocationFromAddress(e.getAdresse()+","+e.getVille());
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
                            String ville = snapshot.child("ville").getValue(String.class);
                            String visibiliteValue = snapshot.child("visibilite").getValue(String.class);
                            final String proprietaire = snapshot.child("proprietaire").getValue(String.class);
                            Visibilite visibilite;
                            if(visibiliteValue.equalsIgnoreCase("PRIVE")){
                                visibilite = Visibilite.PRIVE;
                            }else{
                                visibilite = Visibilite.PUBLIC;
                            }

                            final Evenement e = new Evenement(id, nom, desc, adresse, ville, visibilite, proprietaire);
                            for(int j = 0; j < listEvenements.size(); j++){
                                if(listEvenements.get(j).getId() == id){
                                    listEvenements.remove(j);
                                }
                            }

                            if(visibilite.equals(Visibilite.PRIVE)) {
                                String membres_inscrits_text = snapshot.child("membres_inscrits").getValue(String.class);
                                final List<String> membres_inscrits = Arrays.asList(membres_inscrits_text.split(","));
                                DatabaseReference user_id_ref = FirebaseDatabase.getInstance().getReference("id_users");
                                user_id_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String nb_users_text = snapshot.getValue(String.class);
                                        final int nb_users = Integer.parseInt(nb_users_text);

                                        DatabaseReference user_id_ref_mail = FirebaseDatabase.getInstance().getReference("users");
                                        user_id_ref_mail.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(int i = 0; i < nb_users; i++){
                                                    String mail = snapshot.child(i+"").child("mail").getValue(String.class);
                                                    String id_user = i+"";
                                                    if(mail.equalsIgnoreCase(HomeActivity.user_mail)){
                                                        for (int h = 0; h < membres_inscrits.size(); h++) {
                                                            if(id_user.equalsIgnoreCase(membres_inscrits.get(h))) {
                                                                if(e.getProprietaire().equalsIgnoreCase(id_user)){
                                                                    e.setCouleur_icone(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                                                }else{
                                                                    e.setCouleur_icone(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                                                }
                                                                listEvenements.add(e);
                                                                Log.w("Apparition", "Ajout d'un evenement privé");
                                                                Log.w("Apparition", "Taille de la liste :" + listEvenements.size());
                                                                callback.onDataReceived(listEvenements);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                listEvenements.add(e);
                                callback.onDataReceived(listEvenements);
                            }



                            Log.w("Apparition", "List size before sending :" +listEvenements.size());
//                            callback.onDataReceived(listEvenements);

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