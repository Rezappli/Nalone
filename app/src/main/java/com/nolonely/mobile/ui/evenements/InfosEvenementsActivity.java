package com.nolonely.mobile.ui.evenements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nolonely.mobile.HomeActivity;
import com.nolonely.mobile.R;
import com.nolonely.mobile.ReportActivity;
import com.nolonely.mobile.bdd.json.JSONActivity;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.StatusEvent;
import com.nolonely.mobile.enumeration.Visibility;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.ui.amis.display.InfoUserActivity;
import com.nolonely.mobile.util.Constants;
import com.nolonely.mobile.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nolonely.mobile.util.Constants.USER;

public class InfosEvenementsActivity extends JSONActivity {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    private Evenement EVENT_LOAD;

    public static String type;
    private TextView textViewNbMembers;
    private TextView nbParticipants;
    private int participants;
    private ImageView buttonInscription, ownerImage;
    private TextView textViewInscription;
    private ImageView imageEvenement, buttonPartager, buttonSignaler, buttonAfficher;
    private TextView diffDate;
    private Handler handler;
    private boolean inscrit;
    private TextView textViewPartager;
    private TextView textViewTitleDebut;
    private LinearLayout linearButton, linearParticipate;
    private CardView cardViewTermine;
    private ImageView buttonBack;

    private TextView textViewPrice;
    private ImageView imageViewEuro;
    private CardView cardViewPrice;
    private CardView cardViewOwner;
    private BottomSheetBehavior bottomSheetBehaviorParticipate;

    private boolean isRegistered;
    private LinearLayout linearPrice;

    private View viewGrey;
    private CardView cardViewParticipationCheck;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);
        try {
            createFragment();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment() throws ParseException {
        if (getIntent() != null) {
            EVENT_LOAD = (Evenement) getIntent().getSerializableExtra("event");
            isRegistered = getIntent().getBooleanExtra("isRegistered", false);
        }
        participants = 0;
        textViewNbMembers = findViewById(R.id.textViewNbMembers);
        nbParticipants = findViewById(R.id.nbParticipants);
        buttonInscription = findViewById(R.id.buttonInscription);
        textViewInscription = findViewById(R.id.textViewParticiper);
        textViewPartager = findViewById(R.id.textViewPartager);
        textViewTitleDebut = findViewById(R.id.textViewTitleDebut);
        linearButton = findViewById(R.id.linearButton);
        linearParticipate = findViewById(R.id.linearParticipate);
        cardViewTermine = findViewById(R.id.cardViewTermine);
        ownerImage = findViewById(R.id.ownerImage);
        buttonBack = findViewById(R.id.buttonBack);
        imageEvenement = findViewById(R.id.imageEvenement);
        textViewPrice = findViewById(R.id.textViewPrice);
        imageViewEuro = findViewById(R.id.imageViewEuro);
        cardViewPrice = findViewById(R.id.cardViewPrice);
        linearPrice = findViewById(R.id.linearPrice);
        viewGrey = findViewById(R.id.viewGreyInfosEvenements);
        viewGrey.setOnClickListener(v -> {
            if (bottomSheetBehaviorParticipate.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehaviorParticipate.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            cardViewParticipationCheck.setVisibility(View.GONE);
            viewGrey.setVisibility(View.GONE);
        });
        cardViewParticipationCheck = findViewById(R.id.cardViewParticipationCheck);


        cardViewOwner = findViewById(R.id.cardViewOwner);
        cardViewOwner.setOnClickListener(v -> {

            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", EVENT_LOAD.getOwner_uid());

            JSONController.getJsonObjectFromUrl(Constants.URL_ME, this, params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    Log.w("User", jsonObject.toString());
                    User user = (User) JSONController.convertJSONToObject(jsonObject, User.class);

                    JSONObjectCrypt params = new JSONObjectCrypt();
                    params.putCryptParameter("uid", USER.getUid());

                    JSONController.getJsonArrayFromUrl(Constants.URL_MY_FRIENDS, getBaseContext(), params, new JSONArrayListener() {
                        @Override
                        public void onJSONReceived(JSONArray jsonArray) {
                            try {
                                boolean isFriend = false;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (user == (User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class)) {
                                        isFriend = true;
                                    }
                                }
                                Intent intent = new Intent(getBaseContext(), InfoUserActivity.class);
                                intent.putExtra("user", user);
                                intent.putExtra("isFriend", isFriend);
                                startActivity(intent);

                            } catch (JSONException e) {
                                Log.w("Response", "Erreur: " + e.getMessage());
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
                    Log.w("Response", "Erreur : " + volleyError.toString());
                }
            });
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

        cardViewPrice.setOnClickListener(v -> {
            try {
                incription();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        diffDate = findViewById(R.id.differenceDate);
        buttonAfficher = findViewById(R.id.buttonAfficher);
        buttonAfficher.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            intent.putExtra("longitude", EVENT_LOAD.getLongitude());
            intent.putExtra("latitude", EVENT_LOAD.getLatitude());
            startActivity(intent);
        });
        buttonPartager = findViewById(R.id.buttonPartager);
        buttonPartager.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Clicked on Share ", Toast.LENGTH_SHORT).show();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        buttonSignaler = findViewById(R.id.buttonSignaler);
        buttonSignaler.setOnClickListener(v -> {
            Intent intent = new Intent(InfosEvenementsActivity.this, ReportActivity.class);
            intent.putExtra("type", "event");
            intent.putExtra("uid_report", EVENT_LOAD.getUid());
            startActivity(intent);
        });

        View bottomSheetType = findViewById(R.id.sheetParticipate);
        bottomSheetBehaviorParticipate = BottomSheetBehavior.from(bottomSheetType);
        bottomSheetBehaviorParticipate.setHideable(false);
        bottomSheetBehaviorParticipate.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (cardViewParticipationCheck.getVisibility() == View.GONE)
                            viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        viewGrey.setVisibility(View.VISIBLE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        if (EVENT_LOAD != null) {
            initWidgets();
        }
    }

    private void initWidgets() throws ParseException {
        TextView mTitle = findViewById(R.id.title);
        TextView mDateStart = findViewById(R.id.dateStart);
        TextView mDateEnd = findViewById(R.id.dateEnd);
        TextView mTimeStart = findViewById(R.id.timeStart);
        TextView mTimeEnd = findViewById(R.id.timeEnd);
        TextView mOwner = findViewById(R.id.owner);
        TextView mDescription = findViewById(R.id.description);

        if (EVENT_LOAD.getPrice() > 0) {
            TextView payPrice = findViewById(R.id.payPrice);
            TextView payName = findViewById(R.id.payName);
            TextView payDate = findViewById(R.id.payDate);
            TextView payTime = findViewById(R.id.payTime);
            TextView payCity = findViewById(R.id.payCity);
            EVENT_LOAD.replaceFields(payName, payCity, null, payDate, payTime, null);
            Button payCheck = findViewById(R.id.payCheck);
            TextView payCommission = findViewById(R.id.payCommission);
            TextView payTotal = findViewById(R.id.payTotal);

            payPrice.setText(EVENT_LOAD.getPrice() + "");
            payCommission.setText(EVENT_LOAD.getPrice() / 30 + "");
            payCheck.setOnClickListener(v -> {
                cardViewParticipationCheck.setVisibility(View.VISIBLE);
                bottomSheetBehaviorParticipate.setState(BottomSheetBehavior.STATE_COLLAPSED);
            });
            payTotal.setText(EVENT_LOAD.getPrice() + (EVENT_LOAD.getPrice() / 30) + "");
        }
        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        mOwner.setText(EVENT_LOAD.getOwnerName());

        if (isRegistered) {
            registerClicked();
        } else {
            unregisterClicked();
        }

        nbParticipants.setText(EVENT_LOAD.getNbMembers() + "");

        if (EVENT_LOAD.getDescription().matches("")) {
            mDescription.setVisibility(View.GONE);
        } else {
            mDescription.setText(EVENT_LOAD.getDescription());
        }

        handler = new Handler();
        handler.postDelayed(runnable, 0);

        try {
            mDateStart.setText(TimeUtil.dateOfDateLetter(EVENT_LOAD.getStartDate()));
            mDateEnd.setText(TimeUtil.dateOfDateLetter(EVENT_LOAD.getEndDate()));
        } catch (ParseException e) {
            Log.w("Response", "Erreur:" + e.getMessage());
            Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

        mTimeStart.setText(TimeUtil.timeOfDate(EVENT_LOAD.getStartDate()));
        mTimeEnd.setText(TimeUtil.timeOfDate(EVENT_LOAD.getEndDate()));

        Log.w("STATUS", EVENT_LOAD.getStatusEvent() + "");
        if (EVENT_LOAD.getStatusEvent() == StatusEvent.ENCOURS) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_from));
        } else if (EVENT_LOAD.getStatusEvent() == StatusEvent.FINI) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_end_from));
            textViewTitleDebut.setTextColor(Color.GRAY);
            linearButton.setVisibility(View.GONE);
            cardViewTermine.setVisibility(View.VISIBLE);
            diffDate.setTextColor(Color.GRAY);
        } else if (EVENT_LOAD.getStatusEvent() == StatusEvent.BIENTOT) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_in));
        }

        Constants.setEventImage(EVENT_LOAD, imageEvenement);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                TimeUtil.differenceDate(new Date(), sdf.parse(EVENT_LOAD.getStartDate()), diffDate);
                handler.postDelayed(this, 0);
            } catch (ParseException e) {
                Log.w("Response", "Erreur:" + e.getMessage());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void registerClicked() {
        inscrit = true;
        textViewInscription.setText(getResources().getString(R.string.unregister));
        buttonInscription.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        imageViewEuro.setVisibility(View.GONE);
        textViewPrice.setText(getString(R.string.location_inscrit));
        textViewPrice.setTextColor(Color.WHITE);
        cardViewPrice.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        linearPrice.setBackground(null);
    }

    private void unregisterClicked() {
        inscrit = false;
        textViewInscription.setText(getResources().getString(R.string.register));
        imageViewEuro.setVisibility(View.VISIBLE);
        if (EVENT_LOAD != null) {
            if (EVENT_LOAD.getPrice() == 0) {
                textViewPrice.setText(getString(R.string.free));
                imageViewEuro.setVisibility(View.GONE);
            } else {
                textViewPrice.setText(EVENT_LOAD.getPrice() + "");
                imageViewEuro.setVisibility(View.VISIBLE);
            }
        }
        textViewPrice.setTextColor(Color.BLACK);
        buttonInscription.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        cardViewPrice.setCardBackgroundColor(Color.WHITE);
        linearPrice.setBackground(getResources().getDrawable(R.drawable.custom_border_grey_r150));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void incription() throws ParseException {
        if (!inscrit) {
            if (EVENT_LOAD.getPrice() > 0) {
                bottomSheetBehaviorParticipate.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                registerClicked();
                onRegisterUser();
            }
        } else {
            if (new Timestamp(new Date().getTime()).getTime() - new Timestamp(TimeUtil.dateOfString(EVENT_LOAD.getStartDate()).getTime()).getTime() > 172800) {
                unregisterClicked();
                onUnregisterUser();
            } else {
                Toast.makeText(this, getString(R.string.unregistered_impossible), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onRegisterUser() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_event", EVENT_LOAD.getUid());
        if (EVENT_LOAD.getVisibility().equals(Visibility.PRIVATE)) {
            params.putCryptParameter("status", "waiting");
        } else {
            params.putCryptParameter("status", "add");
        }

        JSONController.getJsonObjectFromUrl(Constants.URL_ADD_USER_TO_EVENT, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.register_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onUnregisterUser() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_event", EVENT_LOAD.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_DELETE_USER_TO_EVENT, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.unregister_messge), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur:" + volleyError.toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        handler.postDelayed(runnable, 0);
        super.onResume();
    }

    @Override
    protected void doInHaveInternetConnection() {

    }

    @Override
    protected void displayNoConnection() {

    }

    @Override
    protected void hiddeNoConnection() {

    }

}