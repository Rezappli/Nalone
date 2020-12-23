

package com.example.nalone.ui.evenements.display;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nalone.Horloge;
import com.example.nalone.StatusEvent;
import com.example.nalone.ui.evenements.CreateEventFragment;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.range;
import static com.example.nalone.util.Constants.targetZoom;
import static com.example.nalone.util.Constants.timeFormat;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;

    private MapView mMapView;
    private GoogleMap mMap;
    private ImageView buttonAdd;
    private CardView cardViewLocationPrive,cardViewLocationAll, cardViewLocationPublic, cardViewLocationCreate, cardViewLocationInscrit;
    private TextView textViewLocationPrive,textViewLocationAll, textViewLocationPublic, textViewLocationCreate, textViewLocationInscrit;
    private ImageView imageViewLocationPrive,imageViewLocationAll, imageViewLocationPublic,imageViewLocationCreate,imageViewLocationInscrit;

    private static ArrayList<Evenement> itemEvents = new ArrayList<>();

    private NavController navController;

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;

    private double unit = 74.6554;

    private double minLat = USER.getLocation().getLatitude() - (range / unit);
    private double maxLat = USER.getLocation().getLatitude() + (range / unit);

    private final double minLong = USER.getLocation().getLongitude() - (range / unit);
    private final double maxLong = USER.getLocation().getLongitude() + (range / unit);

    private List<String> nearby_events;
    private int iterator = 0;
    private CardView cardViewButtonAdd, loading;
    private List<String> myEvents = new ArrayList<>();
    private List<String> event_prive, event_public, event_create, event_inscrit;
    private boolean zoom;
    private Horloge horloge = new Horloge();


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        event_prive = new ArrayList<>();
        event_inscrit = new ArrayList<>();
        event_public = new ArrayList<>();
        event_create = new ArrayList<>();
        buttonBack.setVisibility(View.GONE);
        mMapView = rootView.findViewById(R.id.mapView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewEventMap);
        cardViewButtonAdd = rootView.findViewById(R.id.addEvent);
        cardViewLocationCreate = rootView.findViewById(R.id.cardViewLocationCreate);
        cardViewLocationPrive = rootView.findViewById(R.id.cardViewLocationPrivate);
        cardViewLocationPublic = rootView.findViewById(R.id.cardViewLocationPublic);
        cardViewLocationInscrit = rootView.findViewById(R.id.cardViewLocationInscrit);
        cardViewLocationAll = rootView.findViewById(R.id.cardViewLocationAll);
        textViewLocationCreate = rootView.findViewById(R.id.textViewLocationCreate);
        textViewLocationPrive = rootView.findViewById(R.id.textViewLocationPrivate);
        textViewLocationPublic = rootView.findViewById(R.id.textViewLocationPublic);
        textViewLocationInscrit = rootView.findViewById(R.id.textViewLocationInscrit);
        textViewLocationAll = rootView.findViewById(R.id.textViewLocationAll);
        imageViewLocationInscrit = rootView.findViewById(R.id.imageViewLocationInscrit);
        imageViewLocationPrive = rootView.findViewById(R.id.imageViewLocationPrive);
        imageViewLocationPublic = rootView.findViewById(R.id.imageViewLocationPublic);
        imageViewLocationCreate = rootView.findViewById(R.id.imageViewLocationCreate);
        imageViewLocationAll = rootView.findViewById(R.id.imageViewLocationAll);
        loading = rootView.findViewById(R.id.loading);

        cardViewLocationPrive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Query query = mStoreBase.collection("events").whereEqualTo("visibility", "PRIVATE");
                hiddeText(textViewLocationPrive);
                imageViewLocationPrive.setImageDrawable(getResources().getDrawable(R.drawable.location_private_30));
                clickFiltre(query);

            }
        });

        cardViewLocationAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationAll);
                imageViewLocationAll.setImageDrawable(getResources().getDrawable(R.drawable.location_all_30));
                Query query = mStoreBase.collection("events");
                clickFiltre(query);

            }
        });

        cardViewLocationInscrit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationInscrit);
                imageViewLocationInscrit.setImageDrawable(getResources().getDrawable(R.drawable.location_inscrit_30));
                Query query = mStoreBase.collection("users").document(USER_ID).collection("events_join");
                clickFiltre(query);


            }
        });

        cardViewLocationPublic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationPublic);
                DocumentReference ref = mStoreBase.document("users/" + USER_ID);
                imageViewLocationPublic.setImageDrawable(getResources().getDrawable(R.drawable.location_public_30));
                Query query = mStoreBase.collection("events").whereEqualTo("visibility", "PUBLIC");
                clickFiltre(query);
            }
        });

        cardViewLocationCreate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationCreate);
                imageViewLocationCreate.setImageDrawable(getResources().getDrawable(R.drawable.location_create_30));
                DocumentReference ref = mStoreBase.document("users/" + USER_ID);
                Query query = mStoreBase.collection("events").whereEqualTo("ownerDoc", ref);
                clickFiltre(query);

            }
        });
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
                CreateEventFragment.edit = false;
                CreateEventFragment.EVENT_LOAD = null;
                navController.navigate(R.id.action_navigation_evenements_to_navigation_create_event);
            }
        });

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void clickFiltre(Query query) {
        zoom = true;
        adapterEvents(query);
        updateMap(query);
    }

    private void hiddeText(TextView tv) {
        textViewLocationInscrit.setVisibility(View.VISIBLE);
        textViewLocationPublic.setVisibility(View.VISIBLE);
        textViewLocationPrive.setVisibility(View.VISIBLE);
        textViewLocationCreate.setVisibility(View.VISIBLE);
        textViewLocationAll.setVisibility(View.VISIBLE);
        tv.setVisibility(View.GONE);
        imageViewLocationInscrit.setImageDrawable(getResources().getDrawable(R.drawable.location_inscrit_24));
        imageViewLocationPrive.setImageDrawable(getResources().getDrawable(R.drawable.location_private_24));
        imageViewLocationPublic.setImageDrawable(getResources().getDrawable(R.drawable.location_public_24));
        imageViewLocationCreate.setImageDrawable(getResources().getDrawable(R.drawable.location_create_24));
        imageViewLocationAll.setImageDrawable(getResources().getDrawable(R.drawable.location_all_24));
    }

    private void adapterEvents(final Query query) {

        mStoreBase.collection("users").
                document(USER.getUid()).collection("events")
                .whereEqualTo("status", "add").whereNotEqualTo("user", USER_REFERENCE).limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                event_inscrit.add(document.getId());
                            }

                            startQuery(query);
                        }else{
                            startQuery(query);
                        }
                    }
                });

    }

    private void startQuery(Query query) {
        Query queryF = query.whereGreaterThan("latitude", minLat)
                .whereLessThan("latitude", maxLat);

        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(queryF, Evenement.class).build();

        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement, parent, false);
                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                //holder.mImageView.setImageResource(e.getImage());
                e.setStatusEvent(horloge.verifStatut(new Date(), e.getDate().toDate()));
                mStoreBase.collection("events").document(e.getUid()).set(e);
                if(e.getStatusEvent() == StatusEvent.EXPIRE){
                    //holder.linearTermine.setVisibility(View.VISIBLE);
                    mStoreBase.collection("events").document(e.getUid())
                            .collection("members")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentReference ref = mStoreBase.collection("events").document(e.getUid());
                                        DocumentReference userOwner = mStoreBase.collection("users").document(ref.getId());

                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            User u = document.toObject(User.class);
                                            u.setNumber_events_attend(u.getNumber_events_attend() + 1);
                                            mStoreBase.collection("users").document(document.getId()).set(u);
                                            mStoreBase.collection("users").document(document.getId()).collection("events").document(e.getUid()).delete();
                                        }
                                        mStoreBase.collection("events").document(e.getUid()).delete();
                                    }
                                }
                            });
                    //createFragment();
                }else {
                    holder.mTitle.setText((e.getName()));
                    holder.mDate.setText((dateFormat.format(e.getDate().toDate())));
                    holder.mTime.setText((timeFormat.format(e.getDate().toDate())));
                    holder.mVille.setText((e.getCity()));
                    holder.mProprietaire.setText(e.getOwner());

                    if (event_inscrit.contains(e.getUid()))
                        holder.mImageInscrit.setVisibility(View.VISIBLE);

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

                                            Constants.setUserImage(u, getContext(), holder.mImageView);

                                        }

                                    }
                                }
                            });

                    holder.mCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(e.getLatitude(), e.getLongitude()), 15);
                            mMap.animateCamera(location);
                        }
                    });
                    loading.setVisibility(View.GONE);
                    cardViewButtonAdd.setVisibility(View.VISIBLE);
                }
            }
        };
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
                public void onInfoWindowClick(final Marker marker) {
                    if (marker.getTag() != null) {
                        mStoreBase.collection("events").whereEqualTo("uid", marker.getTag()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    InfosEvenementsActivity.EVENT_LOAD = doc.toObject(Evenement.class);
                                    navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
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
        public TextView mProprietaire;
        public CardView mCarwViewOwner;
        public ImageView mImageInscrit;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardViewEvent);
            mImageView = itemView.findViewById(R.id.imageUser1);
            mTitle = itemView.findViewById(R.id.title1);
            mDate = itemView.findViewById(R.id.date1);
            mTime = itemView.findViewById(R.id.time1);
            mVille = itemView.findViewById(R.id.ville1);
            mProprietaire = itemView.findViewById(R.id.owner1);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            mImageInscrit = itemView.findViewById(R.id.imageView29);


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
        Query query = mStoreBase.collection("events");
        updateMap(query);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        mMap.setMyLocationEnabled(true);
        if(adapter == null || adapter.getItemCount() == 0){
            loading.setVisibility(View.GONE);
            cardViewButtonAdd.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateMap(final Query query) {
        if (mMap != null) {
            mMap.clear();

            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(USER.getLocation().getLatitude(), USER.getLocation().getLongitude()))
                    .radius(range * 1000)
                    .strokeWidth(3f)
                    .strokeColor(Color.BLUE));

            if (targetZoom == null && !zoom) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(USER.getLocation().getLatitude(),
                        USER.getLocation().getLongitude()), 10));
            }

            itemEvents.clear();

            final float[] results = new float[3];


            query.whereGreaterThan("latitude", minLat)
                    .whereLessThan("latitude", maxLat)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Evenement e = doc.toObject(Evenement.class);
                        if (e.getLongitude() > minLong && e.getLongitude() < maxLong) {
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
                        adapterEvents(query);
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
                event_create.add(e.getUid());
            } else {
                couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                event_public.add(e.getUid());
            }
        }

        return couleur[0];
    }
}
