

package com.example.nalone.ui.evenements.display;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.example.nalone.adapter.MapEvenementAdapter;
import com.example.nalone.enumeration.VisibilityMap;
import com.example.nalone.json.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Horloge;
import com.example.nalone.R;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import android.os.Build;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.objects.Evenement;
import com.example.nalone.enumeration.Visibility;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.range;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;

    private MapView mMapView;
    private GoogleMap mMap;
    private CardView cardViewLocationPrive,cardViewLocationAll, cardViewLocationPublic, cardViewLocationCreate, cardViewLocationInscrit;
    private TextView textViewLocationPrive,textViewLocationAll, textViewLocationPublic, textViewLocationCreate, textViewLocationInscrit;
    private ImageView imageViewLocationPrive,imageViewLocationAll, imageViewLocationPublic,imageViewLocationCreate,imageViewLocationInscrit;
    private Button buttonCreations,buttonPlanning;

    private NavController navController;

    private RecyclerView mRecyclerView;
    private MapEvenementAdapter mAdapter;

    private double unit = 74.6554;

    private List<Evenement> nearby_events;
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

    private ImageView arrowLeft, arrowRight, imageViewFiltreSearch;
    private Animation appear_filtre;



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

       // appear_filtre = ;
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
        buttonCreations = rootView.findViewById(R.id.buttonCreations);
        buttonPlanning = rootView.findViewById(R.id.buttonPlanning);
        imageViewFiltreSearch = rootView.findViewById(R.id.filtreSearch);

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
                        imageViewFiltreSearch.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        imageViewFiltreSearch.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        imageViewFiltreSearch.setVisibility(View.VISIBLE);
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

        cardViewLocationAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationAll);
                imageViewLocationAll.setImageDrawable(getResources().getDrawable(R.drawable.location_all_30));
                updateMap(VisibilityMap.ALL);
            }
        });

        cardViewLocationPrive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationPrive);
                imageViewLocationPrive.setImageDrawable(getResources().getDrawable(R.drawable.location_private_30));
                updateMap(VisibilityMap.PRIVATE);
            }
        });

        cardViewLocationInscrit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationInscrit);
                imageViewLocationInscrit.setImageDrawable(getResources().getDrawable(R.drawable.location_inscrit_30));
                updateMap(VisibilityMap.REGISTER);
            }
        });

        cardViewLocationPublic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationPublic);
                imageViewLocationPublic.setImageDrawable(getResources().getDrawable(R.drawable.location_public_30));
                updateMap(VisibilityMap.PUBLIC);
            }
        });

        cardViewLocationCreate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationCreate);
                imageViewLocationCreate.setImageDrawable(getResources().getDrawable(R.drawable.location_create_30));
                updateMap(VisibilityMap.CREATE);
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
        //this.configureRecyclerView();


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

    private void configureRecyclerView() {
        this.mAdapter = new MapEvenementAdapter(this.nearby_events);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerViewSearchEvent.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.recyclerViewSearchEvent.setLayoutManager(llm);
        mAdapter.setOnItemClickListener(new MapEvenementAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = nearby_events.get(position);
                //InfosEvenementsActivity.type = "nouveau";
                navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
            }
        });
        this.recyclerViewSearchEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    MapEvenementAdapter.EventViewHolder firstViewHolder = (MapEvenementAdapter.EventViewHolder) recyclerViewSearchEvent.findViewHolderForLayoutPosition(visiblePosition);
                    if(firstViewHolder != null){
                        dateSearchEvent.setText(firstViewHolder.mDate.getText());
                    }
                }
            }
        });
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



    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(USER.getLatitude(), USER.getLongitude()), 10));
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.addParameter("uid", USER.getUid());
        params.addParameter("latitude", USER.getLatitude());
        params.addParameter("longitude", USER.getLongitude());
        params.addParameter("range", range);

        JSONController.getJsonArrayFromUrl(Constants.URL_NEARBY_EVENTS, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                    try {
                        nearby_events = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++) {
                            nearby_events.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        configureRecyclerView();
                        updateMap(VisibilityMap.ALL);
                    } catch (JSONException e) {
                        Log.w("Response", "Erreur:"+e.getMessage());
                        Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
        loading.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateMap(VisibilityMap visibilityMap) {
        mMap.clear();
        switch (visibilityMap){
            case ALL:
                for(Evenement e : nearby_events){
                    addMarkerOnMap(e);
                }
                break;
            case PUBLIC:
                for(Evenement e : nearby_events){
                    if(e.getVisibility().equals(Visibility.PUBLIC)) {
                        addMarkerOnMap(e);
                    }
                }
                break;
            case PRIVATE:
                for(Evenement e : nearby_events){
                    if(e.getVisibility().equals(Visibility.PRIVATE)) {
                        addMarkerOnMap(e);
                    }
                }
                break;
            case CREATE:
                for(Evenement e : nearby_events){
                    if(e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                        addMarkerOnMap(e);
                    }
                }
                break;
            case REGISTER:
                break;

        }
    }

    private void addMarkerOnMap(Evenement e) {
        MarkerOptions mk = new MarkerOptions();
        mk.title(e.getName());
        mk.position(new LatLng(e.getLatitude(), e.getLongitude()));
        mk.snippet(getResources().getString(R.string.description_event));
        if(e.getVisibility().equals(Visibility.PUBLIC)){
            mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }else{
            if(e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{
                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }
        mMap.addMarker(mk).setTag(e.getUid());
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

}

