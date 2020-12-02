

package com.example.nalone.ui.evenements.display;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.InfosEvenementsActivity;
import com.example.nalone.R;
import com.example.nalone.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Evenement;
import com.example.nalone.Visibility;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.range;
import static com.example.nalone.util.Constants.targetZoom;
import static com.example.nalone.util.Constants.timeFormat;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private ProgressBar progressBar;

    private MapView mMapView;
    private GoogleMap mMap;
    private ImageView buttonAdd;

    private static ArrayList<Evenement> itemEvents = new ArrayList<>();

    private NavController navController;

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;

    private double unitLat = 74.6554;
    private double unitLong = 74;

    private double minLat = USER.getLocation().getLatitude() - (range / unitLat);
    private double maxLat = USER.getLocation().getLatitude() + (range / unitLat);

    private final double minLong = USER.getLocation().getLongitude() - (range / unitLong);
    private final double maxLong = USER.getLocation().getLongitude() + (range / unitLong);

    private List<String> nearby_events;
    private int iterator = 0;
    private CardView cardViewButtonAdd;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        progressBar = rootView.findViewById(R.id.progressBar2);
        buttonBack.setVisibility(View.GONE);
        mMapView = rootView.findViewById(R.id.mapView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewEventMap);
        cardViewButtonAdd = rootView.findViewById(R.id.addEvent);

        nearby_events = new ArrayList<>();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        initGoogleMap(savedInstanceState);

        buttonAdd = rootView.findViewById(R.id.create_event_button);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_evenements_to_navigation_create_event);
            }
        });

        return rootView;
    }

    private void adapterEvents() {
        Query query = mStoreBase.collection("events").whereIn("uid", nearby_events);
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement, parent, false);
                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                holder.mImageView.setImageResource(e.getImage());
                holder.mTitle.setText((e.getName()));
                holder.mDate.setText((dateFormat.format(e.getDate().toDate())));
                holder.mTime.setText((timeFormat.format(e.getDate().toDate())));
                holder.mVille.setText((e.getCity()));
                holder.mDescription.setText((e.getDescription()));
                holder.mProprietaire.setText(e.getOwner());

                iterator++;

                mStoreBase.collection("users").whereEqualTo("uid", e.getOwnerDoc().getId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        final User u = document.toObject(User.class);
                                        if (u.getCursus().equalsIgnoreCase("Informatique")) {
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.RED);
                                        }

                                        if (u.getCursus().equalsIgnoreCase("TC")) {
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                        }

                                        if (u.getCursus().equalsIgnoreCase("MMI")) {
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                        }

                                        if (u.getCursus().equalsIgnoreCase("GB")) {
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                        }

                                        if (u.getCursus().equalsIgnoreCase("LP")) {
                                            holder.mCarwViewOwner.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                        }

                                        if(u.getImage_url() != null) {
                                            if(!Cache.fileExists(u.getUid())) {
                                                StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                                                if (imgRef != null) {
                                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            if (task.isSuccessful()) {
                                                                Uri img = task.getResult();
                                                                if (img != null) {
                                                                    Cache.saveUriFile(u.getUid(), img);
                                                                    u.setImage_url(Cache.getImageDate(u.getUid()));
                                                                    mStoreBase.collection("users").document(u.getUid()).set(u);
                                                                    Glide.with(getContext()).load(img).fitCenter().centerCrop().into(holder.mImageView);
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }else{
                                                Uri imgCache = Cache.getUriFromUid(u.getUid());
                                                Log.w("Cache", "Image Cache : " + Cache.getImageDate(u.getUid()));
                                                Log.w("Cache", "Data Cache : " + u.getImage_url());
                                                if(Cache.getImageDate(u.getUid()).equalsIgnoreCase(u.getImage_url())) {
                                                    Log.w("image", "get image from cache");
                                                    Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(holder.mImageView);
                                                }else{
                                                    StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                                                    if (imgRef != null) {
                                                        imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    Uri img = task.getResult();
                                                                    if (img != null) {
                                                                        Cache.saveUriFile(u.getUid(), img);
                                                                        u.setImage_url(Cache.getImageDate(u.getUid()));
                                                                        mStoreBase.collection("users").document(u.getUid()).set(u);
                                                                        Glide.with(getContext()).load(img).fitCenter().centerCrop().into(holder.mImageView);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        });




                holder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }
        };
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

        if (mMap != null) {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mRecyclerView.setPadding(0, 0, 0, 60);
                    return false;
                }
            });


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mRecyclerView.setPadding(0, 0, 0, 0);
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (marker.getTag() != null) {
                        mStoreBase.collection("events").whereEqualTo("uid", marker.getTag()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    InfosEvenementsActivity.EVENT_LOAD = doc.toObject(Evenement.class);
                                    startActivity(new Intent(getContext(), InfosEvenementsActivity.class));
                                }
                            }
                        });
                    }

                }
            });
        }

    }

    private class EventViewHolder extends RecyclerView.ViewHolder {

        public CardView mCardView;
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        public TextView mDescription;
        public TextView mProprietaire;
        public CardView mCarwViewOwner;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardViewEvent);
            mImageView = itemView.findViewById(R.id.imageUser1);
            mTitle = itemView.findViewById(R.id.title1);
            mDate = itemView.findViewById(R.id.date1);
            mTime = itemView.findViewById(R.id.time1);
            mVille = itemView.findViewById(R.id.ville1);
            mDescription = itemView.findViewById(R.id.description1);
            mProprietaire = itemView.findViewById(R.id.owner1);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);

        }
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
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateMap() {
        if (mMap != null) {
            mMap.clear();


            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(USER.getLocation().getLatitude(), USER.getLocation().getLongitude()))
                    .radius(range * 1000)
                    .strokeWidth(3f)
                    .strokeColor(Color.BLUE));

            if (targetZoom == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(USER.getLocation().getLatitude(), USER.getLocation().getLongitude()), 13));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetZoom, 13));
            }

            itemEvents.clear();

            Log.w("Events", "MinLat : " + minLat);
            Log.w("Events", "MaxLat : " + maxLat);
            Log.w("Events", "MinLong : " + minLong);
            Log.w("Events", "MaxLong : " + maxLong);
            Log.w("Events", "Coordinate : " + USER.getLocation());

            final float[] results = new float[3];

            mStoreBase.collection("events")
                    .whereGreaterThan("latitude", minLat)
                    .whereLessThan("latitude", maxLat)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Evenement e = doc.toObject(Evenement.class);
                        Log.w("Events", "Found " + e.getName());
                        Log.w("Events", "E longitude " + e.getLongitude());
                        if (e.getLongitude() > minLong && e.getLongitude() < maxLong) {
                            Log.w("Events", "Found and add" + e.getName());
                            Location.distanceBetween(USER.getLocation().getLatitude(), USER.getLocation().getLongitude(),
                                    e.getLatitude(), e.getLongitude(), results);
                            if (results[0] <= range * 1000) {
                                MarkerOptions m = new MarkerOptions().title(e.getName())
                                        .snippet("Cliquez pour plus d'informations")
                                        .icon(getEventColor(e))
                                        .position(new LatLng(e.getLatitude(), e.getLongitude()));
                                mMap.addMarker(m).setTag(e.getUid());
                                nearby_events.add(e.getUid());
                            }
                        }
                    }

                    if (nearby_events.size() > 0) {
                        adapterEvents();
                    }
                    Log.w("iterator", iterator+"");
                   /* if (nearby_events.size() <= 1){

                        cardViewButtonAdd.setPadding(100, 0, 0, 0);
                    }*/
                }
            });
        }
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    private BitmapDescriptor getEventColor(Evenement e) {
        final BitmapDescriptor[] couleur = new BitmapDescriptor[1];
        if (e.getVisibility().equals(Visibility.PRIVATE)) {
            if (e.getOwnerDoc().equals(USER_REFERENCE)) {
                couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                mStoreBase.collection("events").document(e.getUid()).collection("members").document(USER.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            } else {
                                couleur[0] = null;
                            }
                        } else {
                            Log.w("Error", "Error : " + task.getException());
                            couleur[0] = null;
                        }
                    }
                });
            }
        } else {
            if (e.getOwnerDoc().equals(USER_REFERENCE)) {
                couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        }

        return couleur[0];
    }
}
