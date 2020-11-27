

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
        import com.example.nalone.Group;
        import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.amis.display.GroupeFragment;
        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapView;
        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.widget.LinearLayout;
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
        import com.example.nalone.InfosEvenementsActivity;
        import com.example.nalone.Visibility;
        import com.google.android.gms.maps.CameraUpdateFactory;

        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.model.BitmapDescriptor;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;


        import java.util.ArrayList;

        import static com.example.nalone.HomeActivity.buttonBack;
        import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
        import static com.example.nalone.util.Constants.USER;
        import static com.example.nalone.util.Constants.USER_ID;
        import static com.example.nalone.util.Constants.USER_LATLNG;
        import static com.example.nalone.util.Constants.USER_REFERENCE;
        import static com.example.nalone.util.Constants.mStore;
        import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.range;
import static com.example.nalone.util.Constants.targetZoom;


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

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        adapterEvents();

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

    private void adapterEvents() {
        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        Query query = mStoreBase.collection("events").whereNotEqualTo("ownerDoc", ref);
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenement,parent,false);
                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                final Evenement event = e;

                holder.mImageView.setImageResource(e.getImage());
                holder.mTitle.setText((e.getName()));
                holder.mDate.setText((e.getDate().toDate().toString()));
                holder.mVille.setText((e.getCity()));
                holder.mDescription.setText((e.getDescription()));
                holder.mProprietaire.setText(e.getOwner());

                mStoreBase.collection("users").whereEqualTo("uid", e.getOwnerDoc().getId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User u = document.toObject(User.class);
                                        if(u.getCursus().equalsIgnoreCase("Informatique")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.RED);
                                        }

                                        if(u.getCursus().equalsIgnoreCase("TC")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("MMI")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("GB")){
                                            holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                        }

                                        if(u.getCursus().equalsIgnoreCase("LP")){
                                            holder.mCarwViewOwner.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                        }

                                    }

                            }
                        }});


                holder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

               /* if(e.getImage_url() != null) {
                    if(!Cache.fileExists(e.getUid())) {
                        StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(g.getUid(), img);
                                            g.setImage_url(Cache.getImageDate(g.getUid()));
                                            mStoreBase.collection("groups").document(g.getUid()).set(g);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(g.getUid());
                        if(Cache.getImageDate(g.getUid()).equalsIgnoreCase(g.getImage_url())) {
                            Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                        }else{
                            StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(g.getUid(), img);
                                                g.setImage_url(Cache.getImageDate(g.getUid()));
                                                mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                */
            }
        };
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

        if(mMap != null) {
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
            mCardView= itemView.findViewById(R.id.cardViewEvent);
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

            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(USER.getLocation().getLatitude(), USER.getLocation().getLongitude()))
                    .radius(range*1000)
                    .strokeWidth(3f)
                    .strokeColor(Color.BLUE));

            if (targetZoom == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_LATLNG, 13));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetZoom, 13));
            }

            itemEvents.clear();

            Log.w("Events", "Range :"+range);

            float units = 78.567f; //km

            //50 => borne inf latitude - (50/78) longitude - (50/78)
            //50 => borne sup latitude + (50/78) longitude + (50/78)

            //si Evenet e : location : inf > location > sup = affiche

            GeoPoint infBorn = new GeoPoint((USER.getLocation().getLatitude()-(range/units)), (USER.getLocation().getLongitude()-(range/units)));
            GeoPoint supBorn = new GeoPoint((USER.getLocation().getLatitude()+(range/units)), (USER.getLocation().getLongitude()+(range/units)));

            Log.w("Events", "Info born : " + infBorn.toString());
            Log.w("Events", "Sup born : " + supBorn.toString());

            mStoreBase.collection("events").whereGreaterThanOrEqualTo("location", infBorn).whereLessThanOrEqualTo("location", supBorn).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        float[] result = new float[3];
                        Evenement e  = doc.toObject(Evenement.class);
                        Log.w("Events", "Event : " + e.getLocation().toString());
                        Log.w("Events", "Event : " + USER.getLocation().toString());
                        Location.distanceBetween(e.getLocation().getLatitude(), e.getLocation().getLongitude(), USER.getLocation().getLatitude(), USER.getLocation().getLongitude(), result);
                        Log.w("Events", "Event : " + e.getName() + " " + result[0]);
                        if(result[0] <= range * 1000) {
                            Log.w("Events", "Event : " + e.getLocation().toString());
                            MarkerOptions m = new MarkerOptions().title(e.getName())
                                    .snippet("Cliquez pour plus d'informations")
                                    .icon(getEventColor(e))
                                    .position(new LatLng(e.getLocation().getLatitude(), e.getLocation().getLongitude()));
                            mMap.addMarker(m).setTag(e.getUid());
                        }
                    }

                }
            });

           /* if(USER.get_register_events() != null) {
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
        BitmapDescriptor couleur;
        if (e.getVisibility().equals(Visibility.PRIVATE)) {
            if (e.getOwnerDoc().equals(USER_REFERENCE)) {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (e.getRegister_users().contains(USER_REFERENCE)) {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            } else {
                couleur = null;
            }
        } else {
            if (e.getOwnerDoc().equals(USER_REFERENCE)) {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                couleur = (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        }

        return couleur;
    }



}
