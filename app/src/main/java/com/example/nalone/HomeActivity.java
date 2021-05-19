package com.example.nalone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.VolleyError;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.User;
import com.example.nalone.ui.NotificationActivity;
import com.example.nalone.ui.evenements.creation.MainCreationEventActivity;
import com.example.nalone.ui.evenements.display.PlanningActivity;
import com.example.nalone.ui.profil.ProfilActivity;
import com.example.nalone.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import static com.example.nalone.util.Constants.USER;


public class HomeActivity extends AppCompatActivity {

    private NavController navController;
    public static ImageView buttonBack, buttonNotif, buttonPlanning;
    public static FloatingActionButton fab1;
    private CardView cardViewPrivate, cardViewPublic;
    private CardView cardViewEventArt, cardTypeEventSport, cardTypeEventParty, cardTypeEventMusic, cardTypeEventMovie, cardTypeEventGame, cardTypeEventCar, cardTypeEventGather, cardTypeEventConference, cardTypeEventShop, cardTypeEventShow, cardTypeEventScience;
    private boolean isOpen = false;
    private ImageView item_profil;
    private View bottomSheetVisibility, bottomSheetType;
    private Visibility currentVisibility;

    private BottomSheetBehavior bottomSheetBehaviorVisibility, bottomSheetBehaviorType;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.GONE);
        buttonNotif = findViewById(R.id.buttonNotif);
        buttonPlanning = findViewById(R.id.buttonPlanning);
        buttonPlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), PlanningActivity.class));
            }
        });
        bottomSheetVisibility = findViewById(R.id.sheetCreateEvent);
        fab1 = findViewById(R.id.fab1);
        final View viewGrey = findViewById(R.id.viewGrey);
        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        item_profil = findViewById(R.id.item_profil);
        item_profil.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                updateUserInformations(); //Mise à jour des informations utilisateurs
                startActivity(new Intent(getBaseContext(), ProfilActivity.class)); //Affichage de la page de profil
            }
        });

        initCardView(); //Initalisation des cards views

        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openType(Visibility.PRIVATE);
            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openType(Visibility.PUBLIC);
            }
        });

        buttonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), NotificationActivity.class));
            }
        });

        buttonBack.setVisibility(View.GONE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recherche, R.id.navigation_amis, R.id.navigation_evenements, R.id.navigation_messages)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        bottomSheetType = findViewById(R.id.sheetCreateEventType);
        bottomSheetBehaviorType = BottomSheetBehavior.from(bottomSheetType);
        bottomSheetBehaviorType.setHideable(false);
        bottomSheetBehaviorType.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fab1.show();
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehaviorVisibility.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
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
                            fab1.show();
                            viewGrey.setVisibility(View.GONE);
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab1.hide();
                bottomSheetBehaviorVisibility.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewGrey.setVisibility(View.VISIBLE);
            }
        });

        viewGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviorVisibility.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewGrey.setVisibility(View.GONE);
            }
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
     * Méthode permettant l'initialisation des cards views
     */
    private void initCardView() {
        cardViewEventArt = findViewById(R.id.cardTypeEventArt);
        cardTypeEventSport = findViewById(R.id.cardTypeEventSport);
        cardTypeEventParty = findViewById(R.id.cardTypeEventCar);
        cardTypeEventMusic = findViewById(R.id.cardTypeEventMusic);
        cardTypeEventMovie = findViewById(R.id.cardTypeEventMovie);
        cardTypeEventGame = findViewById(R.id.cardTypeEventGame);
        cardTypeEventCar = findViewById(R.id.cardTypeEventCar);
        cardTypeEventGather = findViewById(R.id.cardTypeEventGather);
        cardTypeEventConference = findViewById(R.id.cardTypeEventConference);
        cardTypeEventShop = findViewById(R.id.cardTypeEventCar);
        cardTypeEventShow = findViewById(R.id.cardTypeEventShow);
        cardTypeEventScience = findViewById(R.id.cardTypeEventScience);
        cardViewEventArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.ART);
            }
        });
        cardTypeEventSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SPORT);
            }
        });
        cardTypeEventParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.PARTY);
            }
        });
        cardTypeEventMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.MUSIC);
            }
        });
        cardTypeEventMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.MULTIMEDIA);
            }
        });
        cardTypeEventGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.GAME);
            }
        });
        cardTypeEventCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.CAR);
            }
        });
        cardTypeEventGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.GATHER);
            }
        });
        cardTypeEventConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.CONFERENCE);
            }
        });
        cardTypeEventShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SHOP);
            }
        });
        cardTypeEventShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SHOW);
            }
        });
        cardTypeEventScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SCIENCE);
            }
        });
    }

    /**
     * Méthode permettant l'ouverture du bottom sheet lors du clique sur le haut de la fenetre
     *
     * @param v
     */
    private void openType(Visibility v) {
        currentVisibility = v;
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * Ouverture de la page de création des évènements
     *
     * @param tp
     */
    private void goCreateEvent(TypeEvent tp) {
        MainCreationEventActivity.currentEvent = new Evenement();
        MainCreationEventActivity.currentEvent.setUid(UUID.randomUUID().toString());
        MainCreationEventActivity.image = null;
        MainCreationEventActivity.currentEvent.setVisibility(currentVisibility);
        MainCreationEventActivity.currentEvent.setCategory(tp);
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
        startActivity(new Intent(getBaseContext(), MainCreationEventActivity.class));
    }

    /**
     * Methode permettant de savoir si il y a des notifications en attente
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getNotifications(){
        if(USER != null) {
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}