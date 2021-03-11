

package com.example.nalone.ui.evenements.display;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.nalone.adapter.TypeEventAdapter;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.enumeration.VisibilityMap;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.TypeEventObject;
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

    private NavController navController;

    private RecyclerView mRecyclerView;
    private MapEvenementAdapter adapteSuggestion, adapterPopulaire ;
    private TypeEventAdapter typeAdapter;
    private static VisibilityMap currentVisibilityMap = VisibilityMap.ALL;

    private double unit = 74.6554;

    private List<Evenement> nearby_events;
    private int iterator = 0;
    private CardView  loading;


    // Bottom sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private SearchView searchView;
    private RecyclerView recyclerViewSuggestion, recyclerViewPopular, recyclerTypeEvent;


    private ImageView imageViewFiltreSearch, imageViewExpanded;
    private boolean isOpen;

    private List<TypeEventObject> filtreTypeList;



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
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

        imageViewFiltreSearch = rootView.findViewById(R.id.filtreSearch);

        //Bottom sheet
        final View bottomSheet = rootView.findViewById(R.id.sheet);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        searchView = rootView.findViewById(R.id.searchViewSheet);
        recyclerViewSuggestion = rootView.findViewById(R.id.recyclerViewSuggestion);
        recyclerViewPopular = rootView.findViewById(R.id.recyclerViewSearchPopulaire);

        /* INTERRESSANT
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewSearchEvent.smoothScrollBy(recyclerViewSearchEvent.getScrollState(),recyclerViewSearchEvent.getScrollState()+200);
            }
        });*/


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        isOpen = false;
                        searchView.setQuery ("", false);
                        searchView.setIconified(true);
                        searchView.clearFocus();
                        imageViewFiltreSearch.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        imageViewFiltreSearch.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isOpen = true;
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
        imageViewExpanded = rootView.findViewById(R.id.imageExpanded);
        imageViewExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                else
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

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
                currentVisibilityMap = VisibilityMap.ALL;
            }
        });

        cardViewLocationPrive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationPrive);
                imageViewLocationPrive.setImageDrawable(getResources().getDrawable(R.drawable.location_private_30));
                updateMap(VisibilityMap.PRIVATE);
                currentVisibilityMap = VisibilityMap.PRIVATE;
            }
        });

        cardViewLocationInscrit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationInscrit);
                imageViewLocationInscrit.setImageDrawable(getResources().getDrawable(R.drawable.location_inscrit_30));
                updateMap(VisibilityMap.REGISTER);
                currentVisibilityMap = VisibilityMap.REGISTER;
            }
        });

        cardViewLocationPublic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationPublic);
                imageViewLocationPublic.setImageDrawable(getResources().getDrawable(R.drawable.location_public_30));
                updateMap(VisibilityMap.PUBLIC);
                currentVisibilityMap = VisibilityMap.PUBLIC;
            }
        });

        cardViewLocationCreate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hiddeText(textViewLocationCreate);
                imageViewLocationCreate.setImageDrawable(getResources().getDrawable(R.drawable.location_create_30));
                updateMap(VisibilityMap.CREATE);
                currentVisibilityMap = VisibilityMap.CREATE;
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

        /* FILTRES TYPE EVENT */
        recyclerTypeEvent = rootView.findViewById(R.id.recyclerTypeEvent);

        return rootView;
    }

    private void configureRecyclerView() {
        this.adapteSuggestion = new MapEvenementAdapter(this.nearby_events, false);
        this.recyclerViewSuggestion.setAdapter(this.adapteSuggestion);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerViewSuggestion.setLayoutManager(llm);
        adapteSuggestion.setOnItemClickListener(new MapEvenementAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = nearby_events.get(position);
                //InfosEvenementsActivity.type = "nouveau";
                navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
            }
        });

        this.adapterPopulaire = new MapEvenementAdapter(this.nearby_events, false);
        this.recyclerViewPopular.setAdapter(this.adapterPopulaire);
        final LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerViewPopular.setLayoutManager(llm1);
        adapterPopulaire.setOnItemClickListener(new MapEvenementAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = nearby_events.get(position);
                //InfosEvenementsActivity.type = "nouveau";
                navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
            }
        });
        /* INTERESSANT
        this.recyclerViewSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                        dateSearchEvent.setText(firstViewHolder.mDate.getText());
                    }
                }
            }
        });*/

        initFiltres();
        this.typeAdapter = new TypeEventAdapter(this.filtreTypeList);
        typeAdapter.setOnItemClickListener(new TypeEventAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                SearchEventActivity.currentType = filtreTypeList.get(position).getmType();
                startActivity(new Intent(getContext(),SearchEventActivity.class));
            }
        });
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerTypeEvent.setAdapter(this.typeAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerTypeEvent.setLayoutManager(llm2);
    }

    private void initFiltres() {
        filtreTypeList = new ArrayList<>();

        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_art),getResources().getString(R.string.event_art),TypeEvent.ART));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_sport),getResources().getString(R.string.event_sport),TypeEvent.SPORT));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_car),getResources().getString(R.string.event_car),TypeEvent.CAR));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_conference),getResources().getString(R.string.event_conference),TypeEvent.CONFERENCE));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_shop),getResources().getString(R.string.event_shop),TypeEvent.SHOP));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_show),getResources().getString(R.string.event_show),TypeEvent.SHOW));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_game),getResources().getString(R.string.event_game),TypeEvent.GAME));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_gather),getResources().getString(R.string.event_gather),TypeEvent.GATHER));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_movie),getResources().getString(R.string.event_movie),TypeEvent.MOVIE));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_music),getResources().getString(R.string.event_music),TypeEvent.MUSIC));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_party),getResources().getString(R.string.event_party),TypeEvent.PARTY));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_science),getResources().getString(R.string.event_science),TypeEvent.SCIENCE));

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        if(mMap != null) {
            updateMap(currentVisibilityMap);
        }
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

