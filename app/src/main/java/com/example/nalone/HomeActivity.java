package com.example.nalone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.nalone.enumeration.Visibility;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.NotificationActivity;
import com.example.nalone.ui.evenements.creation.MainCreationEventActivity;
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

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;


public class HomeActivity extends AppCompatActivity{

    private NavController navController;
    public static ImageView buttonBack, buttonNotif;
    public static FloatingActionButton fab1;
    private CardView cardViewPrivate, cardViewPublic;
    private boolean isOpen = false;
    private ImageView item_profil;

    private BottomSheetBehavior bottomSheetBehavior;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);


        final View bottomSheet = findViewById(R.id.sheetCreateEvent);

        buttonBack = findViewById(R.id.buttonBack);
        buttonNotif = findViewById(R.id.buttonNotif);
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

        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(Visibility.PRIVATE);
            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateEvent(Visibility.PUBLIC);
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

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        fab1.show();
                        viewGrey.setVisibility(View.GONE);
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
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewGrey.setVisibility(View.VISIBLE);
            }
        });

        viewGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewGrey.setVisibility(View.GONE);
            }
        });

    }

    public void goCreateEvent(Visibility v){
        MainCreationEventActivity.currentEvent = new Evenement();
        MainCreationEventActivity.image = null;
        MainCreationEventActivity.currentEvent.setVisibility(v);
        startActivity(new Intent(getBaseContext(),MainCreationEventActivity.class));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }


}