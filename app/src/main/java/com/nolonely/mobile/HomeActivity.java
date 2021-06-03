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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.TypeEvent;
import com.nolonely.mobile.enumeration.Visibility;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.signUpActivities.SpinnerAdapter;
import com.nolonely.mobile.ui.NotificationActivity;
import com.nolonely.mobile.ui.evenements.creation.MainCreationEventActivity;
import com.nolonely.mobile.ui.message.MessagesActivity;
import com.nolonely.mobile.ui.profil.ProfilActivity;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import static com.nolonely.mobile.util.Constants.USER;


public class HomeActivity extends NoLonelyActivity implements AdapterView.OnItemSelectedListener {
    
    private ImageView buttonBack, buttonNotif, buttonChat;
    private CardView cardViewPrivate, cardViewPublic;
    private boolean isOpen = false;
    private ImageView item_profil;
    private View bottomSheetVisibility, bottomSheetType;
    private Visibility currentVisibility;
    protected View viewGrey;

    private BottomSheetBehavior bottomSheetBehaviorVisibility, bottomSheetBehaviorType;

    private boolean typeChoosed;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        typeChoosed = false;
        scheduleJob();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.GONE);
        buttonNotif = findViewById(R.id.buttonNotif);
        buttonChat = findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), MessagesActivity.class)));
        bottomSheetVisibility = findViewById(R.id.sheetCreateEvent);
        viewGrey = findViewById(R.id.viewGrey);
        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        item_profil = findViewById(R.id.item_profil);
        item_profil.setOnClickListener(v -> {
            updateUserInformations(); //Mise à jour des informations utilisateurs
            startActivity(new Intent(getBaseContext(), ProfilActivity.class)); //Affichage de la page de profil
        });


        cardViewPrivate.setOnClickListener(v -> openType(Visibility.PRIVATE));

        cardViewPublic.setOnClickListener(v -> openType(Visibility.PUBLIC));

        buttonNotif.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), NotificationActivity.class)));

        buttonBack.setVisibility(View.GONE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recherche_friends, R.id.navigation_amis, R.id.navigation_evenements, R.id.navigation_planning)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        Spinner spin = (Spinner) findViewById(R.id.spinnerType);
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
                Log.w("Response", "Mise à jour des informations utilisateurs");
                Log.w("Response", jsonObject.toString());
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", volleyError.toString());
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.update_fail), Toast.LENGTH_SHORT).show();
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
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
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
        //super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (typeChoosed) {
            Log.w("TYPEVENTT", TypeEvent.values()[position].toString());
            goCreateEvent(TypeEvent.values()[position]);
        }
        typeChoosed = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}