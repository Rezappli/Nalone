

package com.example.nalone.ui.evenements.display;

import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.nalone.HomeActivity;
import com.example.nalone.ui.evenements.BottomSheetFragment;
import com.example.nalone.ui.evenements.creation.MainCreationEventActivity;
import com.example.nalone.util.Horloge;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.ui.evenements.creation.CreateEventFragment;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.R;
import com.example.nalone.objects.User;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.objects.Evenement;
import com.example.nalone.enumeration.Visibility;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
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
import static com.example.nalone.util.Constants.formatD;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.range;
import static com.example.nalone.util.Constants.targetZoom;
import static com.example.nalone.util.Constants.timeFormat;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;

    private MapView mMapView;
    private GoogleMap mMap;
    private CardView cardViewLocationPrive,cardViewLocationAll, cardViewLocationPublic, cardViewLocationCreate, cardViewLocationInscrit;
    private TextView textViewLocationPrive,textViewLocationAll, textViewLocationPublic, textViewLocationCreate, textViewLocationInscrit;
    private ImageView imageViewLocationPrive,imageViewLocationAll, imageViewLocationPublic,imageViewLocationCreate,imageViewLocationInscrit;

    private static ArrayList<Evenement> itemEvents = new ArrayList<>();

    private NavController navController;

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter,adapter1,adapter2;

    private double unit = 74.6554;

    private double minLat = USER.getLocation().getLatitude() - (range / unit);
    private double maxLat = USER.getLocation().getLatitude() + (range / unit);

    private final double minLong = USER.getLocation().getLongitude() - (range / unit);
    private final double maxLong = USER.getLocation().getLongitude() + (range / unit);

    private List<String> nearby_events;
    private int iterator = 0;
    private CardView  loading;
    private List<String> myEvents = new ArrayList<>();
    private List<String> event_prive, event_public, event_create, event_inscrit;
    private boolean zoom;
    private Horloge horloge = new Horloge();

    // Bottom sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private SearchView searchView;
    private RecyclerView recyclerViewSearchEvent;
    private FirestoreRecyclerAdapter adapterSearchEvent;
    private TextView dateSearchEvent;

    private ImageView arrowLeft, arrowRight;



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

        //Bottom sheet
        final View bottomSheet = rootView.findViewById(R.id.sheet);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        searchView = rootView.findViewById(R.id.searchViewSheet);
        recyclerViewSearchEvent = rootView.findViewById(R.id.recyclerViewSearchEvent);
        dateSearchEvent = rootView.findViewById(R.id.dateSearchEvent);
        arrowLeft = rootView.findViewById(R.id.arrowLeftDate);
        arrowRight = rootView.findViewById(R.id.arrowRightDate);
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewSearchEvent.smoothScrollBy(recyclerViewSearchEvent.getScrollState(),recyclerViewSearchEvent.getScrollState()+200);
            }
        });

        Query query = mStoreBase.collection("events");
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        adapterSearchEvent =  new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list_, parent, false);
                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                //holder.mImageView.setImageResource(e.getImage());
                Log.w("creation fragment", "Adapter");

                holder.mTitle.setText((e.getName()));
                if(e.getStartDate().equals(new Date())){
                    holder.mDate.setText("Aujourd'hui");
                }else{
                    String date_text = formatD.format(e.getStartDate().toDate());
                    String final_date_text = "";
                    for (int j = 0; j < date_text.length() - 5; j++) {
                        char character = date_text.charAt(j);
                        if (j == 0) {
                            character = Character.toUpperCase(character);
                        }
                        final_date_text += character;
                    }

                    holder.mDate.setText(final_date_text);
                }

                //holder.mDate.setText((dateFormat.format(e.getStartDate().toDate())));
                holder.mTime.setText((timeFormat.format(e.getStartDate().toDate())));
                holder.mVille.setText((e.getCity()));
                //holder.mDescription.setText((e.getDescription()));
                holder.mProprietaire.setText(e.getOwner());
                holder.textViewNbMembers.setText(e.getNbMembers()+"");


                mStoreBase.collection("users").whereEqualTo("uid", e.getOwnerDoc().getId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User u = document.toObject(User.class);
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

                holder.textViewAfficher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InfosEvenementsActivity.EVENT_LOAD = e;
                        //InfosEvenementsActivity.type = "nouveau";
                        navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
                    }
                });




                loading.setVisibility(View.GONE);
            }
        };
        recyclerViewSearchEvent.setLayoutManager(llm);
        adapterSearchEvent.startListening();
        recyclerViewSearchEvent.setAdapter(adapterSearchEvent);
        recyclerViewSearchEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                if(visiblePosition > -1) {
                    View v = llm.findViewByPosition(visiblePosition);
                    //do something
                    EventViewHolder firstViewHolder = (EventViewHolder) recyclerViewSearchEvent.findViewHolderForLayoutPosition(visiblePosition);
                    if(firstViewHolder != null){
                        dateSearchEvent.setText(firstViewHolder.mDate.getText());
                    }
                }
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        searchView.setQuery ("", false);
                        searchView.setIconified(true);
                        searchView.clearFocus();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheet.setNestedScrollingEnabled(true);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        searchView.setQueryHint("Recherche");
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        cardViewLocationPrive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Query query = mStoreBase.collection("users").document(USER_ID).collection("events_join").whereEqualTo("visibility",Visibility.PRIVATE);
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
                Query query = mStoreBase.collection("events");//.whereEqualTo("visibility",Visibility.PUBLIC);
                zoom = true;
                startQuery(query,true);
                updateMap(query,true);
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


        /*HomeActivity.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                CreateEventFragment.edit = false;
                CreateEventFragment.EVENT_LOAD = null;
                CreateEventFragment.evenementAttente = null;
                //navController.navigate(R.id.action_navigation_evenements_to_navigation_create_event);
                startActivity(new Intent(getContext(), MainCreationEventActivity.class));
            }
        });*/

        /*CardView cardViewBottom = rootView.findViewById(R.id.cardViewBottomSheet);
        cardViewBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(),bottomSheetFragment.getTag());
            }
        });*/
        return rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void clickFiltre(Query query) {
        zoom = true;
        startQuery(query,false);
        updateMap(query,false);
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


    private void startQuery(Query query,boolean all) {




    }



    private class EventViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        //public TextView mDescription;
        public TextView mProprietaire;
        public TextView textViewAfficher, textViewParticiper;
        public CardView mAfficher;
        public CardView mCarwViewOwner;
        public  TextView textViewNbMembers;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            //mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.cardViewEventList);
            textViewAfficher = itemView.findViewById(R.id.textViewAfficher);
            textViewParticiper = itemView.findViewById(R.id.textViewParticiper);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

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
        Query query = mStoreBase.collection("events");//.whereEqualTo("visibility",Visibility.PUBLIC);
        startQuery(query,true);
        updateMap(query,true);
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
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateMap(final Query query, boolean all) {
        final List<Evenement> evenements = new ArrayList<>();
        mStoreBase.collection("users").document(USER_ID).collection("events_join")
                .whereEqualTo("visibility",Visibility.PRIVATE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot doc : task.getResult()){
                                Evenement e = doc.toObject(Evenement.class);
                                MarkerOptions m = new MarkerOptions().title(e.getName())
                                        .snippet("Cliquez pour plus d'informations")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                        .position(new LatLng(e.getLatitude(), e.getLongitude()));

                                Log.w("event icon", "apparaitre icon");
                                mMap.addMarker(m).setTag(e.getUid());
                                nearby_events.add(e.getUid());
                            }
                        }
                    }
                });
        Query queryF;
        if(all){
            queryF = query.whereEqualTo("statusEvent", StatusEvent.BIENTOT).whereEqualTo("visibility",Visibility.PUBLIC).whereGreaterThan("latitude", minLat)
                    .whereLessThan("latitude", maxLat);
        }else{
            queryF = query.whereEqualTo("statusEvent", StatusEvent.BIENTOT).whereGreaterThan("latitude", minLat)
                    .whereLessThan("latitude", maxLat);
        }
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


            queryF.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Evenement e = doc.toObject(Evenement.class);
                        if(e.getOwnerDoc().equals(USER_REFERENCE)){
                            MarkerOptions m = new MarkerOptions().title(e.getName())
                                    .snippet("Cliquez pour plus d'informations")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .position(new LatLng(e.getLatitude(), e.getLongitude()));

                            Log.w("event icon", "apparaitre icon");
                            mMap.addMarker(m).setTag(e.getUid());
                            nearby_events.add(e.getUid());
                        }else if (e.getLongitude() > minLong && e.getLongitude() < maxLong) {
                            Location.distanceBetween(USER.getLocation().getLatitude(), USER.getLocation().getLongitude(),
                                    e.getLatitude(), e.getLongitude(), results);
                            if (results[0] <= range * 1000) {
                                showMarkers(e);
                            }
                        }
                    }

                    Log.w("iterator", iterator+"");
                   /* if (nearby_events.size() <= 1){

                        cardViewButtonAdd.setPadding(100, 0, 0, 0);
                    }*/
                }
            });

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
    }

    private void showMarkers(final Evenement e) {
        final BitmapDescriptor[] couleur = new BitmapDescriptor[1];
        if (e.getVisibility()== Visibility.PRIVATE) {
                mStoreBase.collection("users").document(USER_ID).collection("events_join").document(e.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        Log.w("event private"," OK" + e.getName());

                                        couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                        MarkerOptions m = new MarkerOptions().title(e.getName())
                                                .snippet("Cliquez pour plus d'informations")
                                                .icon(couleur[0])
                                                .position(new LatLng(e.getLatitude(), e.getLongitude()));

                                        Log.w("event icon", "apparaitre icon");
                                        mMap.addMarker(m).setTag(e.getUid());
                                        nearby_events.add(e.getUid());
                                    } else {
                                        Log.w("event private","NOK");
                                        couleur[0] = null;
                                    }
                                }
                            }
                        });
        } else if (e.getOwnerDoc().equals(USER_REFERENCE)) {
            couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            MarkerOptions m = new MarkerOptions().title(e.getName())
                    .snippet("Cliquez pour plus d'informations")
                    .icon(couleur[0])
                    .position(new LatLng(e.getLatitude(), e.getLongitude()));
            Log.w("event icon", "apparaitre icon");
            mMap.addMarker(m).setTag(e.getUid());
            nearby_events.add(e.getUid());
            event_create.add(e.getUid());
        } else {
            couleur[0] = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            MarkerOptions m = new MarkerOptions().title(e.getName())
                    .snippet("Cliquez pour plus d'informations")
                    .icon(couleur[0])
                    .position(new LatLng(e.getLatitude(), e.getLongitude()));
            Log.w("event icon", "apparaitre icon");
            mMap.addMarker(m).setTag(e.getUid());
            nearby_events.add(e.getUid());
            event_public.add(e.getUid());
            Log.w("event public","OK" + e.getName());

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

    @Override
    public void onResume() {
        super.onResume();
        /*searchView.setQuery("", false);
        rootView.requestFocus();*/
    }
}
