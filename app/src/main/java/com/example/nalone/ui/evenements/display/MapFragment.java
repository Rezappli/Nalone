package com.example.nalone.ui.evenements.display;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.EvenementAdapter;
import com.example.nalone.adapter.TypeEventAdapter;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.enumeration.VisibilityMap;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.TypeEventObject;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.HomeActivity.fab1;
import static com.example.nalone.enumeration.TypeEvent.ART;
import static com.example.nalone.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.range;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;

    private MapView mMapView;
    private GoogleMap mMap;
    private CardView cardViewLocationPrive, cardViewLocationAll, cardViewLocationPublic, cardViewLocationCreate, cardViewLocationInscrit;
    private TextView textViewLocationPrive, textViewLocationAll, textViewLocationPublic, textViewLocationCreate, textViewLocationInscrit;
    private ImageView imageViewLocationPrive, imageViewLocationAll, imageViewLocationPublic, imageViewLocationCreate, imageViewLocationInscrit;

    private NavController navController;

    private EvenementAdapter adapteSuggestion, adapterPopulaire;
    private TypeEventAdapter typeAdapter;
    private static VisibilityMap currentVisibilityMap = VisibilityMap.ALL;

    private List<Evenement> nearby_events;
    private List<Evenement> eventsPopular;
    private CardView loading;

    private static CameraPosition posCam = null;
    private View viewGrey;
    private Circle circle = null;


    // Bottom sheet
    private BottomSheetBehavior bottomSheetBehavior, bottomSheetBehaviorDetails;
    private SearchView searchView;
    private RecyclerView recyclerViewSuggestion, recyclerViewPopular, recyclerTypeEvent;


    private ImageView imageViewFiltreSearch, imageViewExpanded;
    private boolean isOpen, detailsIsOpen;

    private List<TypeEventObject> filtreTypeList;

    private TextView textViewDetailName, textViewDetailCity, textViewDetailDate, textViewDetailTime, textViewDetailNbMembers;
    private ImageView imageViewDetailCategory;


    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = rootView.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return rootView;
    }

    private void createFragment() {
        buttonBack.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
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
        viewGrey = rootView.findViewById(R.id.viewGreyMap);
        imageViewDetailCategory = rootView.findViewById(R.id.imageViewDetailCategory);

        imageViewFiltreSearch = rootView.findViewById(R.id.filtreSearch);

        //Bottom sheet
        final View bottomSheet = rootView.findViewById(R.id.sheet);
        final View bottomSheetDetails = rootView.findViewById(R.id.sheetEvent);
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

        textViewDetailName = rootView.findViewById(R.id.textViewDetailName);
        textViewDetailCity = rootView.findViewById(R.id.textViewDetailCity);
        textViewDetailDate = rootView.findViewById(R.id.textViewDetailDate);
        textViewDetailTime = rootView.findViewById(R.id.textViewDetailTime);
        textViewDetailNbMembers = rootView.findViewById(R.id.textViewDetailNbMembers);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehaviorDetails = BottomSheetBehavior.from(bottomSheetDetails);
        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        isOpen = false;
                        searchView.setQuery("", false);
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

        bottomSheetBehaviorDetails.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        viewGrey.setVisibility(View.GONE);
                        fab1.show();
                        detailsIsOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        viewGrey.setVisibility(View.VISIBLE);
                        //fab1.hide();
                        detailsIsOpen = true;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        viewGrey.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        viewGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviorDetails.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

        initFiltres();

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

    }

    private void configureRecyclerView() {

        configureSuggestion();
        configurePopular();
        initFiltres();

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


    }

    private void configureSuggestion() {
        this.adapteSuggestion = new EvenementAdapter(this.nearby_events, R.layout.item_evenement, false);
        this.recyclerViewSuggestion.setAdapter(this.adapteSuggestion);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerViewSuggestion.setLayoutManager(llm);
        adapteSuggestion.setOnItemClickListener(new EvenementAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = nearby_events.get(position);
                //InfosEvenementsActivity.type = "nouveau";
                startActivity(new Intent(getContext(), InfosEvenementsActivity.class));

            }
        });
    }

    private void configurePopular() {
        this.adapterPopulaire = new EvenementAdapter(this.eventsPopular, R.layout.item_evenement, false);
        this.recyclerViewPopular.setAdapter(this.adapterPopulaire);
        final LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerViewPopular.setLayoutManager(llm1);
        adapterPopulaire.setOnItemClickListener(new EvenementAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = eventsPopular.get(position);
                //InfosEvenementsActivity.type = "nouveau";
                startActivity(new Intent(getContext(), InfosEvenementsActivity.class));

            }
        });
    }

    private void initFiltres() {
        filtreTypeList = new ArrayList<>();

        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_art), getResources().getString(R.string.event_art), ART));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_sport), getResources().getString(R.string.event_sport), TypeEvent.SPORT));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_car), getResources().getString(R.string.event_car), TypeEvent.CAR));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_conference), getResources().getString(R.string.event_conference), TypeEvent.CONFERENCE));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_shop), getResources().getString(R.string.event_shop), TypeEvent.SHOP));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_show), getResources().getString(R.string.event_show), TypeEvent.SHOW));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_game), getResources().getString(R.string.event_game), TypeEvent.GAME));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_gather), getResources().getString(R.string.event_gather), TypeEvent.GATHER));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_movie), getResources().getString(R.string.event_movie), TypeEvent.MULTIMEDIA));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_music), getResources().getString(R.string.event_music), TypeEvent.MUSIC));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_party), getResources().getString(R.string.event_party), TypeEvent.PARTY));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_science), getResources().getString(R.string.event_science), TypeEvent.SCIENCE));

        recyclerTypeEvent = rootView.findViewById(R.id.recyclerTypeEvent);
        this.typeAdapter = new TypeEventAdapter(this.filtreTypeList);
        typeAdapter.setOnItemClickListener(new TypeEventAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                SearchEventActivity.currentType = filtreTypeList.get(position).getmType();
                startActivity(new Intent(getContext(), SearchEventActivity.class));
            }
        });
        this.recyclerTypeEvent.setAdapter(this.typeAdapter);
        final LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerTypeEvent.setLayoutManager(llm2);
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

        clusterManager = new ClusterManager<>(getContext(), mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        addRandomPointsToMap();

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                posCam = mMap.getCameraPosition();
            }
        });

        if (posCam == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(5, USER.getLongitude()), 10));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(posCam));
        }
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("latitude", USER.getLatitude());
        params.putCryptParameter("longitude", USER.getLongitude());
        params.putCryptParameter("range", range);

        nearby_events = new ArrayList<>();
        eventsPopular = new ArrayList<>();

        JSONController.getJsonArrayFromUrl(Constants.URL_NEARBY_EVENTS, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            nearby_events.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        configureSuggestion();
                        updateMap(VisibilityMap.ALL);
                        updateCircle();
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });


        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_POPULAR, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsPopular.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        if (recyclerViewPopular == null)
                            configurePopular();
                        adapterPopulaire.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.w("Response events popular", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response events popular", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
        loading.setVisibility(View.GONE);


    }

    public void addRandomPointsToMap() {
        int LOWER = 1, UPPER = 10;
        int sydneyLatitude = -30;
        int sydneyLongitude = 120;
        LatLng location = new LatLng(0, 0);
        for (int i = 0; i < 5; i++) {
            int latitude = sydneyLatitude + (int) (Math.random() * (UPPER - LOWER)) + LOWER;
            int longitude = sydneyLongitude + (int) (Math.random() * (UPPER - LOWER)) + LOWER;
            location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location));
            MyItem offsetItem = new MyItem(latitude, longitude, "test", "This is a test item");
            clusterManager.addItem(offsetItem);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }

    public class MyItem implements ClusterItem {
        private final LatLng position;
        private final String title;
        private final String snippet;

        public MyItem(double lat, double lng, String title, String snippet) {
            position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSnippet() {
            return snippet;
        }
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> clusterManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateMap(VisibilityMap visibilityMap) {
        mMap.clear();
        switch (visibilityMap) {
            case ALL:
                for (Evenement e : nearby_events) {
                    addMarkerOnMap(e);
                }
                break;
            case PUBLIC:
                for (Evenement e : nearby_events) {
                    if (e.getVisibility().equals(Visibility.PUBLIC)) {
                        addMarkerOnMap(e);
                    }
                }
                break;
            case PRIVATE:
                for (Evenement e : nearby_events) {
                    if (e.getVisibility().equals(Visibility.PRIVATE)) {
                        addMarkerOnMap(e);
                    }
                }
                break;
            case CREATE:
                for (Evenement e : nearby_events) {
                    if (e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                        addMarkerOnMap(e);
                    }
                }
                break;
            case REGISTER:
                break;
        }
    }

    private void updateCircle() {
        if (mMap != null) {
            if (circle != null) {
                circle.remove();
                circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(USER.getLatitude(), USER.getLongitude()))
                        .radius(range * 1000)
                        .strokeWidth(3f)
                        .strokeColor(Color.BLUE));
            } else {
                if (USER != null) {
                    circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(USER.getLatitude(), USER.getLongitude()))
                            .radius(range * 1000)
                            .strokeWidth(3f)
                            .strokeColor(Color.BLUE));
                }
            }
        }
    }

    private void addMarkerOnMap(Evenement e) {

        MarkerOptions mk = new MarkerOptions();

        //mk.title(e.getName());
        mk.position(new LatLng(e.getLatitude(), e.getLongitude()));

        if (e.getVisibility().equals(Visibility.PUBLIC)) {
            mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            if (e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }

        mMap.addMarker(mk).setTag(e);
        MyItem offsetItem = new MyItem(e.getLatitude(), e.getLongitude(), null, null);
        clusterManager.addItem(offsetItem);
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                InfosEvenementsActivity.EVENT_LOAD = (Evenement) marker.getTag();
                startActivity(new Intent(getContext(), InfosEvenementsActivity.class));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                fab1.hide();
                Evenement e = (Evenement) marker.getTag();

                if (e != null) {
                    textViewDetailNbMembers.setText(e.getNbMembers() + "");
                    textViewDetailCity.setText(e.getCity());
                    textViewDetailDate.setText(e.getStartDate());
                    textViewDetailName.setText(e.getName());
                    textViewDetailTime.setText(e.getStartDate());
                    imageViewDetailCategory.setImageResource(e.getImageCategory());

                }

                // Check if a click count was set, then display the click count.
                bottomSheetBehaviorDetails.setState(BottomSheetBehavior.STATE_EXPANDED);

                return false;
            }
        });


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
        createFragment();
        updateCircle();
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