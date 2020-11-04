package com.example.nalone.ui.evenements.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nalone.Adapter.ItemEventAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.CreateEventActivity;
import com.example.nalone.Evenement;
import com.example.nalone.InfosEvenementsActivity;
import com.example.nalone.Visibilite;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.nalone.util.Constants;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.EVENTS_LIST;
import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MapView mMapView;
    private GoogleMap mMap;
    private ImageView buttonAdd;

    private RecyclerView mRecyclerViewEvent;
    private ItemEventAdapter mAdapterEvent;
    private RecyclerView.LayoutManager mLayoutManagerEvent;



    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        mMapView = rootView.findViewById(R.id.mapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);


        mMapView.getMapAsync(this);


        initGoogleMap(savedInstanceState);

        buttonAdd = rootView.findViewById(R.id.create_event_button);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final List<Evenement> itemEvents = new ArrayList<>();
        mMap = googleMap;

        LatLng laval = getLocationFromAddress(USERS_LIST.get(USER_ID).getVille());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laval, 13	));

        for(int i = 0; i < EVENTS_LIST.size(); i++){
            MarkerOptions m = new MarkerOptions();
            Evenement e = Constants.EVENTS_LIST.get(i+"");

            m.title(e.getNom());
            m.snippet("Cliquer pour en savoir plus");
            m.position(getLocationFromAddress(e.getAdresse()+","+e.getVille()));

            if(e.getVisibilite().equals(Visibilite.PRIVE)){
                if(e.getMembres_inscrits().contains(USER_ID)){
                    if(m.getIcon() == null){
                        m.icon(getEventColor(e));
                    }
                    itemEvents.add(e);
                    mMap.addMarker(m).setTag(e.getId());
                }
            }else{
                itemEvents.add(e);
                mMap.addMarker(m).setTag(e.getId());
            }

        }

        mAdapterEvent = new ItemEventAdapter(itemEvents);

        mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewEventMap);
        mLayoutManagerEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerEvent);
        mRecyclerViewEvent.setAdapter(mAdapterEvent);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mRecyclerViewEvent.setPadding(0,0,0,60);
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mRecyclerViewEvent.setPadding(0,0,0,0);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), InfosEvenementsActivity.class);
                InfosEvenementsActivity.initialise(marker);
                startActivity(intent);
            }
        });

        mAdapterEvent.setOnItemClickListener(new ItemEventAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocationFromAddress(itemEvents.get(position)
                .getAdresse()+","+itemEvents.get(position).getVille()), 13	));
            }
        });





        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    private BitmapDescriptor getEventColor(Evenement e) {
        BitmapDescriptor couleur;
        if(e.visibilite.equals(Visibilite.PRIVE)){
            if(e.proprietaire.equalsIgnoreCase(USER_ID)) {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
        }else{
            couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        return couleur;
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

        Geocoder coder = new Geocoder(getActivity().getBaseContext());
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