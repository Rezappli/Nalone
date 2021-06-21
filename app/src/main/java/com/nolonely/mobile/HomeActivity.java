package com.nolonely.mobile;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nolonely.mobile.bdd.json.JSONActivity;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.bdd.sql_lite.DatabaseManager;
import com.nolonely.mobile.enumeration.TypeEvent;
import com.nolonely.mobile.enumeration.Visibility;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.signUpActivities.SpinnerAdapter;
import com.nolonely.mobile.ui.NotificationActivity;
import com.nolonely.mobile.ui.amis.display.AmisFragment;
import com.nolonely.mobile.ui.evenements.creation.MainCreationEventActivity;
import com.nolonely.mobile.ui.evenements.display.EventMapFragment;
import com.nolonely.mobile.ui.evenements.display.PlanningFragment;
import com.nolonely.mobile.ui.message.MessagesActivity;
import com.nolonely.mobile.ui.profil.ProfilActivity;
import com.nolonely.mobile.ui.recherche.SearchFragment;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import static com.nolonely.mobile.util.Constants.USER;


public class HomeActivity extends JSONActivity implements AdapterView.OnItemSelectedListener {

    private ImageView buttonNotif, buttonChat;
    private CardView cardViewPrivate, cardViewPublic;
    private boolean isOpen = false;
    private ImageView item_profil;
    private View bottomSheetVisibility, bottomSheetType;
    private Visibility currentVisibility;
    protected View viewGrey;
    private ImageView imageViewLogo;

    private BottomSheetBehavior bottomSheetBehaviorVisibility, bottomSheetBehaviorType;

    private boolean typeChoosed;

    final Fragment fragment1 = new EventMapFragment();
    final Fragment fragment2 = new PlanningFragment();
    final Fragment fragment3 = new SearchFragment();
    final Fragment fragment4 = new AmisFragment();
    final FragmentManager fm = getSupportFragmentManager();

    Fragment active = fragment1;

    private CardView cardViewNoConnection, cardViewPrivateImpossible;
    private TextView textViewPrivate;
    public SearchView searchView;

    private CoordinatorLayout mainView;
    public static String ACTION_RECEIVE_SEARCH = "ACTION_RECEIVE_SEARCH";
    public static String EXTRA_SEARCH_QUERY = "EXTRA_SEARCH_QUERY";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        cardViewNoConnection = findViewById(R.id.cardViewNoConnection);

        mainView = findViewById(R.id.main_view);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        typeChoosed = false;
        scheduleJob();

        buttonNotif = findViewById(R.id.buttonNotif);
        buttonChat = findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), MessagesActivity.class)));
        bottomSheetVisibility = findViewById(R.id.sheetCreateEvent);
        viewGrey = findViewById(R.id.viewGrey);
        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        textViewPrivate = findViewById(R.id.textViewPrivate);
        cardViewPrivateImpossible = findViewById(R.id.cardViewPrivateImpossible);
        imageViewLogo = findViewById(R.id.ivLogo);
        searchView = findViewById(R.id.svSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (active.equals(fragment2)) {
                    ((PlanningFragment) fragment2).search(query.replaceAll("[^A-Za-z ]", ""));
                } else if (active.equals(fragment3)) {
                    ((SearchFragment) fragment3).search(query.replaceAll("[^A-Za-z ]", ""));
                } else if (active.equals(fragment4)) {
                    ((AmisFragment) fragment4).search(query.replaceAll("[^A-Za-z ]", ""));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnCloseListener(() -> {
            if (active.equals(fragment2)) {
                ((PlanningFragment) fragment2).search(null);
            } else if (active.equals(fragment3)) {
                ((SearchFragment) fragment3).search(null);
            } else if (active.equals(fragment4)) {
                ((AmisFragment) fragment4).search(null);
            }
            return false;
        });

        item_profil = findViewById(R.id.item_profil);
        item_profil.setOnClickListener(v -> {
            updateUserInformations(); //Mise à jour des informations utilisateurs
            startActivity(new Intent(getBaseContext(), ProfilActivity.class)); //Affichage de la page de profil
        });

        if (USER.isHasFriend()) {
            cardViewPrivateImpossible.setVisibility(View.GONE);
        } else {
            cardViewPrivateImpossible.setVisibility(View.VISIBLE);
            textViewPrivate.setText(getString(R.string.private_impossible));
        }

        cardViewPrivate.setOnClickListener(v -> {
            if (USER.isHasFriend())
                openType(Visibility.PRIVATE);
            else
                Toast.makeText(this, getString(R.string.private_impossible), Toast.LENGTH_SHORT).show();
        });

        cardViewPublic.setOnClickListener(v -> openType(Visibility.PUBLIC));

        buttonNotif.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), NotificationActivity.class)));

        Spinner spin = findViewById(R.id.spinnerType);
        spin.setOnItemSelectedListener(this);
        SpinnerAdapter customAdapter = new SpinnerAdapter(getBaseContext(), TypeEvent.listOfImages(getBaseContext()), TypeEvent.listOfNames(getBaseContext()));
        spin.setAdapter(customAdapter);

        bottomSheetType = findViewById(R.id.sheetCreateEventType);
        bottomSheetBehaviorType = BottomSheetBehavior.from(bottomSheetType);
        bottomSheetBehaviorType.setHideable(false);
        bottomSheetBehaviorType.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehaviorVisibility.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        bottomSheetBehaviorVisibility = BottomSheetBehavior.from(bottomSheetVisibility);
        bottomSheetBehaviorVisibility.setHideable(false);
        bottomSheetBehaviorVisibility.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (bottomSheetBehaviorType.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            viewGrey.setVisibility(View.GONE);
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        ImageView imageViewAdd = findViewById(R.id.imageViewAdd);
        imageViewAdd.setOnClickListener(v -> {
            bottomSheetBehaviorVisibility.setState(BottomSheetBehavior.STATE_EXPANDED);
            viewGrey.setVisibility(View.VISIBLE);
        });

        viewGrey.setOnClickListener(v -> {
            bottomSheetBehaviorVisibility.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
            viewGrey.setVisibility(View.GONE);
        });


        if (getIntent() != null && getIntent().hasExtra("longitude")) {
            ((EventMapFragment) fragment1).focusSpecificEvent(new LatLng(getIntent().getDoubleExtra("latitude", 0.0), getIntent().getDoubleExtra("longitude", 0.0)));
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        hideSearchView();
        switch (item.getItemId()) {
            case R.id.navigation_evenements:
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                changeSearchToLogo();
                return true;

            case R.id.navigation_planning:
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                fragment2.onResume();
                changeLogoToSearch();
                return true;

            case R.id.navigation_search:
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                changeLogoToSearch();
                return true;
            case R.id.navigation_amis:
                fm.beginTransaction().hide(active).show(fragment4).commit();
                active = fragment4;
                changeLogoToSearch();
                return true;
        }

        return false;
    };

    private void changeLogoToSearch() {
        imageViewLogo.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
    }

    private void changeSearchToLogo() {
        imageViewLogo.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);

    }

    /**
     * Méthode permettant de mettre à jour les informations de l'utilisateur avant d'ouvrir la page de profil
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUserInformations() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_ME, HomeActivity.this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                USER = (User) JSONController.convertJSONToObject(jsonObject, User.class);
                DatabaseManager databaseManager = new DatabaseManager(getBaseContext());
                databaseManager.updateUser((User) JSONController.convertJSONToObject(jsonObject, User.class));
                Log.w("Response", "Mise à jour des informations utilisateurs");
                Log.w("Response", jsonObject.toString());
                JSONObjectCrypt params = new JSONObjectCrypt();
                params.putCryptParameter("uid", USER.getUid());
                params.putCryptParameter("limit", 2); //fix a limit to 10 users

                JSONController.getJsonArrayFromUrl(Constants.URL_MY_FRIENDS, getBaseContext(), params, new JSONArrayListener() {
                    @Override
                    public void onJSONReceived(JSONArray jsonArray) {
                        if (jsonArray.length() > 0) {
                            USER.setHasFriend(true);

                        } else {
                            USER.setHasFriend(false);
                        }
                    }

                    @Override
                    public void onJSONReceivedError(VolleyError volleyError) {
                        Log.w("Response", "Erreur: " + volleyError.toString());
                    }
                });
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", volleyError.toString());
            }
        });
    }

    /**
     * Méthode permettant l'ouverture du bottom sheet lors du clique sur le haut de la fenetre
     *
     * @param v visibilité
     */
    private void openType(Visibility v) {
        currentVisibility = v;
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    /**
     * Ouverture de la page de création des évènements
     *
     * @param tp type of event
     */
    private void goCreateEvent(TypeEvent tp) {
        MainCreationEventActivity.currentEvent = new Evenement();
        MainCreationEventActivity.currentEvent.setUid(UUID.randomUUID().toString());
        MainCreationEventActivity.image = null;
        MainCreationEventActivity.currentEvent.setVisibility(currentVisibility);
        MainCreationEventActivity.currentEvent.setCategory(tp);
        startActivity(new Intent(getBaseContext(), MainCreationEventActivity.class));
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * Methode permettant de savoir si il y a des notifications en attente
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getNotifications() {
        if (USER != null) {
            launchJSONCall();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
        updateUserInformations(); //Mise à jour des informations utilisateurs
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, NotificationMessagingService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("MyOwnService", "Job scheduled");
        } else {
            Log.d("MyOwnService", "Job scheduling failed");
        }
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (typeChoosed) {
            goCreateEvent(TypeEvent.values()[position]);
        }
        typeChoosed = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void doInHaveInternetConnection() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_NEW_NOTIFICATIONS, HomeActivity.this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                if (jsonArray.length() > 0) {
                    buttonNotif.setImageResource(R.drawable.notification);
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.error_notifications), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Value:" + volleyError.toString());
            }
        });
    }

    @Override
    protected void displayNoConnection() {
        cardViewNoConnection.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hiddeNoConnection() {
        cardViewNoConnection.setVisibility(View.GONE);
    }

    public void hideSearchView() {
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }
}