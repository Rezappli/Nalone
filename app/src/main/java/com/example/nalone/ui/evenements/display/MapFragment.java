package com.example.nalone.ui.evenements.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nalone.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Evenement;
import com.example.nalone.Visibility;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


import java.util.ArrayList;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.USER_LATLNG;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.targetZoom;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private ProgressBar progressBar;

    private MapView mMapView;
    private GoogleMap mMap;
    private ImageView buttonAdd;

    private RecyclerView mRecyclerViewEvent;
    private RecyclerView.LayoutManager mLayoutManagerEvent;

    private static ArrayList<Evenement> itemEvents = new ArrayList<>();

    private NavController navController;



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        progressBar = rootView.findViewById(R.id.progressBar2);
        mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewEventMap);
        buttonBack.setVisibility(View.GONE);
        mMapView = rootView.findViewById(R.id.mapView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);


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
               /* Intent createEvent = new Intent(getActivity().getBaseContext(), CreateEventActivity.class);
                startActivityForResult(createEvent,0);*/
                navController.navigate(R.id.action_navigation_evenements_to_navigation_create_event);

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
        mMap = googleMap;
        updateMap();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateMap() {
        if (mMap != null) {
            mMap.clear();

            Log.w("Map", "LatLng : " + USER_LATLNG);

            if (targetZoom == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_LATLNG, 13));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetZoom, 13));
            }

            itemEvents.clear();

            float[] results = new float[1];

            /*if(USER.get_register_events() != null) {
                for (int i = 0; i < USER.get_register_events().size(); i++) {
                    getEventData(USER.get_register_events().get(i).getId(), new FireStoreEventsListeners() {
                        @Override
                        public void onDataUpdate(Evenement e) {
                            MarkerOptions m = new MarkerOptions().title(e.getName())
                                    .snippet("Cliquez pour plus d'informations")
                                    .icon(getEventColor(e));
                            mMap.addMarker(m).setTag(e.getUid());
                        }
                    });
                }
            }*/
        }
    }



    @Override
    public void onPause(){
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory(){
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    private BitmapDescriptor getEventColor(Evenement e) {
        BitmapDescriptor couleur;
        if(e.getVisibility().equals(Visibility.PRIVATE)) {
            if (e.getOwnerDoc().equals(USER_REFERENCE)) {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if(e.getRegister_users().contains(USER_REFERENCE)){
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }else{
                couleur = null;
            }
        }else{
            if (e.getOwnerDoc().equals(USER_REFERENCE)) {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        }

        return couleur;
    }


    //@Override
    public void onUpdateAdapter() {
            //mAdapterEvent = new ItemEventAdapter(itemEvents);

            mLayoutManagerEvent = new LinearLayoutManager(
                    rootView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            mRecyclerViewEvent.setLayoutManager(mLayoutManagerEvent);
            //mRecyclerViewEvent.setAdapter(mAdapterEvent);

            if(mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mRecyclerViewEvent.setPadding(0, 0, 0, 60);
                        return false;
                    }
                });


                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mRecyclerViewEvent.setPadding(0, 0, 0, 0);
                    }
                });

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        /*getEventData((String) marker.getTag(), new FireStoreEventsListeners() {
                            @Override
                            public void onDataUpdate(Evenement e) {
                                InfosEvenementsActivity.EVENT_LOAD = e;
                                startActivity(new Intent(getContext(), InfosEvenementsActivity.class));
                            }
                        });*/
                    }
                });
            }

            /*mAdapterEvent.setOnItemClickListener(new ItemEventAdapter.OnItemClickListener() {
                @Override
                public void onAddClick(int position) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(itemEvents.get(position).getLocation().getLatitude(), itemEvents.get(position).getLocation().getLongitude()), 13));
                }
            });*/


        mRecyclerViewEvent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}