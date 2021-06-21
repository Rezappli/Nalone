package com.nolonely.mobile.ui.evenements.display;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.ClusterManager;
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONFragment;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.VisibilityMap;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.CustomMarker;
import com.nolonely.mobile.objects.CustomRenderer;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.ui.evenements.InfosEvenementsActivity;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nolonely.mobile.enumeration.VisibilityMap.ALL;
import static com.nolonely.mobile.enumeration.VisibilityMap.FRIEND;
import static com.nolonely.mobile.enumeration.VisibilityMap.PUBLIC;
import static com.nolonely.mobile.enumeration.VisibilityMap.REGISTERED;
import static com.nolonely.mobile.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.nolonely.mobile.util.Constants.USER;
import static com.nolonely.mobile.util.Constants.range;


public class EventMapFragment extends JSONFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraMoveStartedListener {

    private View rootView;

    private MapView mMapView;
    private GoogleMap mMap;
    private TextView textViewLocationPrive, textViewLocationAll, textViewLocationPublic, textViewLocationCreate, textViewLocationInscrit;
    private ImageView imageViewLocationPrive, imageViewLocationAll, imageViewLocationPublic, imageViewLocationCreate, imageViewLocationInscrit;
    private VisibilityMap currentVisibilityMap;
    private List<Evenement> publicList;
    private List<Evenement> friendList;
    private List<Evenement> registeredList;
    private CardView loading;
    private static CameraPosition posCam = null;
    private Circle circle = null;
    // Bottom sheet
    private BottomSheetBehavior bottomSheetBehaviorDetails;

    private View viewGrey;
    private TextView textViewDetailName, textViewDetailCity, textViewDetailDate, textViewDetailTime, textViewDetailNbMembers;
    private ImageView imageViewDetailCategory, imageEvent;

    private ClusterManager<CustomMarker> clusterManager;
    private LocationManager locationManager;

    private LatLng specificEvent;
    private AlertDialog positionDialog;
    private LottieAnimationView iconPosition;
    private boolean positionIsFocused;
    private TextView textViewPrice;
    private CardView cardViewPrice;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button buttonGo;

    public EventMapFragment() {
    }

    public void focusSpecificEvent(LatLng latLng) {
        specificEvent = latLng;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);


        mMapView = rootView.findViewById(R.id.mapView);
        createFragment();

        if (USER != null) {
            initGoogleMap(savedInstanceState);
        }

        return rootView;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void createFragment() {

        CardView cardViewLocationCreate = rootView.findViewById(R.id.cardViewLocationCreate);
        CardView cardViewLocationPrive = rootView.findViewById(R.id.cardViewLocationPrivate);
        CardView cardViewLocationPublic = rootView.findViewById(R.id.cardViewLocationPublic);
        CardView cardViewLocationInscrit = rootView.findViewById(R.id.cardViewLocationInscrit);
        CardView cardViewLocationAll = rootView.findViewById(R.id.cardViewLocationAll);
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
        imageEvent = rootView.findViewById(R.id.imageEvent);
        buttonGo = rootView.findViewById(R.id.buttonGo);
        textViewPrice = rootView.findViewById(R.id.textViewPrice);
        cardViewPrice = rootView.findViewById(R.id.cardViewPrice);

        iconPosition = rootView.findViewById(R.id.iconPosition);
        iconPosition.setOnClickListener(v -> {
            if (locationManager.isLocationEnabled()) {
                if (!positionIsFocused) {
                    iconPosition.setMinFrame(0);
                    iconPosition.setMaxFrame(37);
                    positionIsFocused = true;
                    iconPosition.playAnimation();

                    Location location = mMap.getMyLocation();
                    LatLng latLng = null;
                    if (locationManager != null) {
                        latLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                            13));
                }
            } else {
                createGpsDisabledAlert();
            }
        });
        currentVisibilityMap = ALL;
        //Bottom sheet
        final View bottomSheetDetails = rootView.findViewById(R.id.sheetEvent);

        textViewDetailName = rootView.findViewById(R.id.textViewDetailName);
        textViewDetailCity = rootView.findViewById(R.id.textViewDetailCity);
        textViewDetailDate = rootView.findViewById(R.id.textViewDetailDate);
        textViewDetailTime = rootView.findViewById(R.id.textViewDetailTime);
        textViewDetailNbMembers = rootView.findViewById(R.id.textViewDetailNbMembers);

        bottomSheetBehaviorDetails = BottomSheetBehavior.from(bottomSheetDetails);

        bottomSheetBehaviorDetails.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        viewGrey.setVisibility(View.VISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        viewGrey.setOnClickListener(v -> bottomSheetBehaviorDetails.setState(BottomSheetBehavior.STATE_COLLAPSED));

        cardViewLocationAll.setOnClickListener(v -> {
            hiddeText(textViewLocationAll);
            imageViewLocationAll.setImageDrawable(getResources().getDrawable(R.drawable.location_all_30));
            updateMap(VisibilityMap.ALL);
            currentVisibilityMap = VisibilityMap.ALL;
        });

        cardViewLocationPrive.setOnClickListener(v -> {
            hiddeText(textViewLocationPrive);
            imageViewLocationPrive.setImageDrawable(getResources().getDrawable(R.drawable.location_private_30));
            updateMap(FRIEND);
            currentVisibilityMap = FRIEND;
        });

        cardViewLocationInscrit.setOnClickListener(v -> {
            hiddeText(textViewLocationInscrit);
            imageViewLocationInscrit.setImageDrawable(getResources().getDrawable(R.drawable.location_inscrit_30));
            updateMap(VisibilityMap.REGISTERED);
            currentVisibilityMap = VisibilityMap.REGISTERED;
        });

        cardViewLocationPublic.setOnClickListener(v -> {
            hiddeText(textViewLocationPublic);
            imageViewLocationPublic.setImageDrawable(getResources().getDrawable(R.drawable.location_public_30));
            updateMap(VisibilityMap.PUBLIC);
            currentVisibilityMap = VisibilityMap.PUBLIC;
        });

        cardViewLocationCreate.setOnClickListener(v -> {
            hiddeText(textViewLocationCreate);
            imageViewLocationCreate.setImageDrawable(getResources().getDrawable(R.drawable.location_create_30));
            updateMap(VisibilityMap.CREATE);
            currentVisibilityMap = VisibilityMap.CREATE;
        });


    }

    private void createGpsDisabledAlert() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
        localBuilder
                .setMessage("Le GPS est inactif, voulez-vous l'activer ?")
                .setCancelable(false)
                .setPositiveButton("Activer GPS ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                showGpsOptions();
                            }
                        }
                );
        localBuilder.setNegativeButton("Ne pas l'activer ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                        getActivity().finish();
                    }
                }
        );
        localBuilder.create().show();
    }

    private void showGpsOptions() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        getActivity().finish();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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
        mMap.setOnCameraMoveStartedListener(this);
        clusterManager = new ClusterManager<>(requireContext(), mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        mMap.setOnCameraMoveListener(() -> posCam = mMap.getCameraPosition());

        if (posCam == null) {
            if (specificEvent == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(USER.getLatitude(), USER.getLongitude()), 10));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(specificEvent, 10));
            }
        } else {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(posCam));
        }

        launchJSONCall(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void checkJSONReceived() {
        if (friendList != null && publicList != null && registeredList != null) {
            loading.setVisibility(View.GONE);
            updateMap(currentVisibilityMap);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void updateMap(VisibilityMap visibilityMap) {
        if (mMap != null)
            mMap.clear();
        clusterManager.clearItems();
        switch (visibilityMap) {
            case ALL:
                for (Evenement e : publicList) {
                    addMarkerOnMap(e, true);
                }
                for (Evenement e : friendList) {
                    addMarkerOnMap(e, false);
                }
                break;
            case PUBLIC:
                for (Evenement e : publicList) {
                    if (!e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                        addMarkerOnMap(e, true);
                    }
                }
                break;
            case FRIEND:
                for (Evenement e : friendList) {
                    addMarkerOnMap(e, false);
                }
                break;
            case CREATE:
                for (Evenement e : publicList) {
                    if (e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                        addMarkerOnMap(e, true);
                    }
                }
                break;
            case REGISTERED:
                for (Evenement e : registeredList) {
                    if (!e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                        addMarkerOnMap(e, publicList.contains(e));
                    }
                }
                break;
        }

        enableMyLocation();
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

    @SuppressLint("SetTextI18n")
    private void addMarkerOnMap(Evenement e, boolean isPublic) {

        CustomMarker m;

        if (isPublic) {
            if (e.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
                m = new CustomMarker(new LatLng(e.getLatitude(), e.getLongitude()), BitmapDescriptorFactory.HUE_GREEN, e);
            } else {
                m = new CustomMarker(new LatLng(e.getLatitude(), e.getLongitude()), BitmapDescriptorFactory.HUE_RED, e);
            }
        } else {
            m = new CustomMarker(new LatLng(e.getLatitude(), e.getLongitude()), BitmapDescriptorFactory.HUE_AZURE, e);
        }

        clusterManager.addItem(m);

        clusterManager.setRenderer(new CustomRenderer(getContext(), mMap, clusterManager));

        mMap.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterClickListener(cluster -> false);

        clusterManager.setOnClusterItemClickListener(item -> {

            Evenement e1 = item.getTag();

            if (e1 != null) {
                Button buttonDisplay = rootView.findViewById(R.id.buttonDisplay);
                buttonDisplay.setOnClickListener(v -> {
                    Intent intent;
                    if (e1.getOwner_uid().equals(USER.getUid())) {
                        intent = new Intent(getContext(), InfosEventCreationActivity.class);
                    } else {
                        intent = new Intent(getContext(), InfosEvenementsActivity.class);
                        for (Evenement evenement : registeredList) {
                            if (evenement.getUid().equals(e1.getUid())) {
                                intent.putExtra("isRegistered", true);
                            }
                        }
                    }
                    intent.putExtra("event", e1);
                    startActivity(intent);
                });

                Date d = null;
                try {
                    d = Constants.allTimeFormat.parse(e1.getStartDate());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                buttonGo.setOnClickListener(v -> {
                    String latitude = String.valueOf(e1.getLatitude());
                    String longitude = String.valueOf(e1.getLongitude());
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                });
                textViewDetailNbMembers.setText(e1.getNbMembers() + "");
                textViewDetailCity.setText(e1.getCity());
                textViewDetailDate.setText(Constants.dateFormat.format(d));
                textViewDetailName.setText(e1.getName());
                textViewDetailName.setText(e1.getName());
                if (e1.getPrice() > 0) {
                    textViewPrice.setText(e1.getPrice() + " €");
                    cardViewPrice.setBackgroundColor(Color.parseColor("#0C6FE6"));
                } else {
                    textViewPrice.setText("Free");
                    cardViewPrice.setBackgroundColor(Color.parseColor("#00D7F3"));
                }
                imageViewDetailCategory.setImageResource(e1.getImageCategory());
                Constants.setEventImage(e1, imageEvent);
                if (d == null) {
                    textViewDetailDate.setText(Constants.dateFormat.format(d));
                } else {
                    textViewDetailTime.setText(Constants.timeFormat.format(d));
                }
            }

            // Check if a click count was set, then display the click count.
            bottomSheetBehaviorDetails.setState(BottomSheetBehavior.STATE_EXPANDED);

            return false;
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (locationManager != null) {
            if (locationManager.isLocationEnabled()) {
                if (positionDialog != null) {
                    if (positionDialog.isShowing()) {
                        positionDialog.dismiss();
                    }
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void doInHaveInternetConnection() {

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("filter", PUBLIC.toString());

        JSONController.getJsonArrayFromUrl(Constants.URL_NEARBY_EVENTS, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    publicList = new ArrayList<>();

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            publicList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                    }
                    checkJSONReceived();
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


        JSONObjectCrypt params1 = new JSONObjectCrypt();
        params1.putCryptParameter("uid", USER.getUid());
        params1.putCryptParameter("filter", FRIEND.toString());

        JSONController.getJsonArrayFromUrl(Constants.URL_NEARBY_EVENTS, getContext(), params1, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    friendList = new ArrayList<>();

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            friendList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                    }

                    checkJSONReceived();
                    updateCircle();
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

        JSONObjectCrypt params2 = new JSONObjectCrypt();
        params2.putCryptParameter("uid", USER.getUid());
        params2.putCryptParameter("filter", REGISTERED.toString());

        JSONController.getJsonArrayFromUrl(Constants.URL_NEARBY_EVENTS, getContext(), params2, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    registeredList = new ArrayList<>();

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            registeredList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                    }

                    checkJSONReceived();
                    updateCircle();
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
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void enableMyLocation() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.getLastKnownLocation()
        //locationManager.lo
        if (locationManager.isLocationEnabled()) {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        return;
                    } else {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        return;
                    } else {
                        mMap.setMyLocationEnabled(false);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            };

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            } else {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10, locationListener);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setMessage(getContext().getResources().getString(R.string.enable_location))
                    .setCancelable(false)
                    .setPositiveButton(getContext().getResources().getString(R.string.button_yes), (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton(getContext().getResources().getString(R.string.button_no), (dialog, id) -> dialog.cancel());
            positionDialog = alertDialog.create();
            positionDialog.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE || reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            if (positionIsFocused) {
                iconPosition.setMinFrame(37);
                iconPosition.setMaxFrame(60);
                iconPosition.playAnimation();
                positionIsFocused = false;
            }
        }
    }
}