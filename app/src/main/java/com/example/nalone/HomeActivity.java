package com.example.nalone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.NotificationActivity;
import com.example.nalone.ui.evenements.creation.MainCreationEventActivity;
import com.example.nalone.ui.evenements.display.EventPlanningActivity;
import com.example.nalone.ui.profil.MainProfilActivity;
import com.example.nalone.ui.profil.ProfilFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.UUID;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;


public class HomeActivity extends AppCompatActivity{

    private NavController navController;
    public static ImageView buttonBack, buttonNotif, buttonPlanning;
    public static FloatingActionButton fab1;
    private CardView cardViewPrivate, cardViewPublic;
    private CardView cardViewEventArt, cardTypeEventSport,cardTypeEventParty,cardTypeEventMusic,cardTypeEventMovie,cardTypeEventGame
            ,cardTypeEventCar,cardTypeEventGather,cardTypeEventConference,cardTypeEventShop,cardTypeEventShow,cardTypeEventScience;
    private boolean isOpen = false;
    private ImageView item_profil;
    private View bottomSheetVisibility, bottomSheetType;
    private Visibility currentVisibility;

    private BottomSheetBehavior bottomSheetBehaviorVisibility,bottomSheetBehaviorType;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        buttonBack = findViewById(R.id.buttonBack);
        buttonNotif = findViewById(R.id.buttonNotif);
        buttonPlanning = findViewById(R.id.buttonPlanning);
        buttonPlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), EventPlanningActivity.class));
            }
        });
        bottomSheetVisibility =  findViewById(R.id.sheetCreateEvent);
        fab1 = findViewById(R.id.fab1);
        final View viewGrey = findViewById(R.id.viewGrey);
        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        item_profil = findViewById(R.id.item_profil);
        item_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MainProfilActivity.class));
            }
        });

        initCardView();



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
/*        mStoreBase.collection("users").document(USER_ID).collection("notifications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                buttonNotif.setImageDrawable(getResources().getDrawable(R.drawable.notification));
                            }
                    }
                    }
                });*/

        buttonBack.setVisibility(View.GONE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recherche, R.id.navigation_amis, R.id.navigation_evenements, R.id.navigation_messages, R.id.navigation_profil)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        bottomSheetType=  findViewById(R.id.sheetCreateEventType);
        bottomSheetBehaviorType= BottomSheetBehavior.from(bottomSheetType);
        bottomSheetBehaviorType.setHideable(false);
        bottomSheetBehaviorType.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
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
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if(bottomSheetBehaviorType.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
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
                goCreateEvent(TypeEvent.ART);             }
        });
        cardTypeEventSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SPORT);             }
        });
        cardTypeEventParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.PARTY);             }
        });
        cardTypeEventMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.MUSIC);             }
        });
        cardTypeEventMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.MOVIE);             }
        });
        cardTypeEventGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.GAME);             }
        });
        cardTypeEventCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.CAR);             }
        });
        cardTypeEventGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.GATHER);             }
        });
        cardTypeEventConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.CONFERENCE);             }
        });
        cardTypeEventShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SHOP);             }
        });
        cardTypeEventShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(TypeEvent.SHOW);             }
        });
        cardTypeEventScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
goCreateEvent(TypeEvent.SCIENCE);            }
        });
    }

    public void openType(Visibility v){
        currentVisibility = v;
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void goCreateEvent(TypeEvent tp){
        MainCreationEventActivity.currentEvent = new Evenement();
        MainCreationEventActivity.currentEvent.setUid(UUID.randomUUID().toString());
        MainCreationEventActivity.image = null;
        MainCreationEventActivity.currentEvent.setVisibility(currentVisibility);
        MainCreationEventActivity.currentEvent.setEventType(tp);
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
        startActivity(new Intent(getBaseContext(),MainCreationEventActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}