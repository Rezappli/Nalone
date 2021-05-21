package com.example.nalone.ui.evenements;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Constants;
import com.example.nalone.util.TimeUtil;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.nalone.util.Constants.USER;

public class InfosEvenementsActivity extends AppCompatActivity {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static Evenement EVENT_LOAD;

    public static String type;
    private TextView textViewNbMembers;
    private TextView nbParticipants;
    private int participants;
    private ImageView buttonInscription, ownerImage;
    private TextView textViewInscription;
    private ImageView imageEvenement, buttonPartager, buttonAnnuler;
    private TextView diffDate;
    private Handler handler = new Handler();
    private boolean inscrit;
    private TextView textViewPartager;
    private TextView textViewTitleDebut;
    private LinearLayout linearButton, linearParticipate;
    private CardView cardViewTermine;
    private LinearLayout linearAnnuler;
    private ImageView buttonBack;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment() {
        participants = 0;
        TextView mTitle = findViewById(R.id.title);
        TextView mDate = findViewById(R.id.date);
        TextView mTimer = findViewById(R.id.time);
        TextView mOwner = findViewById(R.id.owner);
        TextView mDescription = findViewById(R.id.description);
        textViewNbMembers = findViewById(R.id.textViewNbMembers);
        nbParticipants = findViewById(R.id.nbParticipants);
        buttonInscription = findViewById(R.id.buttonInscription);
        textViewInscription = findViewById(R.id.textViewParticiper);
        textViewPartager = findViewById(R.id.textViewPartager);
        textViewTitleDebut = findViewById(R.id.textViewTitleDebut);
        linearButton = findViewById(R.id.linearButton);
        linearParticipate = findViewById(R.id.linearParticipate);
        cardViewTermine = findViewById(R.id.cardViewTermine);
        linearAnnuler = findViewById(R.id.linearAnnuler);
        buttonAnnuler = findViewById(R.id.buttonAnnuler);
        ownerImage = findViewById(R.id.ownerImage);
        buttonBack = findViewById(R.id.buttonBack);
        imageEvenement = findViewById(R.id.imageEvenement);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                incription();
            }
        });
        diffDate = findViewById(R.id.differenceDate);

        buttonPartager = findViewById(R.id.buttonPartager);
        buttonPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Clicked on Share ", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppression();
            }
        });

        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        mOwner.setText(EVENT_LOAD.getOwnerName());
        nbParticipants.setText(EVENT_LOAD.getNbMembers() + "");

        if (EVENT_LOAD.getDescription().matches("")) {
            mDescription.setVisibility(View.GONE);
        } else {
            mDescription.setText(EVENT_LOAD.getDescription());
        }

        handler = new Handler();
        handler.postDelayed(runnable, 0);

        try {

            /*§Date date = sdf.parse(EVENT_LOAD.getStartDate());
            String date_text = Constants.formatD.format(date);
            for (int i = 0; i < date_text.length() - 5; i++) {
                char character = date_text.charAt(i);
                if (i == 0) {
                    character = Character.toUpperCase(character);
                }
                final_date_text += character;
            }*/

            //mDate.setText(final_date_text);
            mDate.setText(TimeUtil.dateOfDateLetter(EVENT_LOAD.getStartDate()));
        } catch (ParseException e) {
            Log.w("Response", "Erreur:" + e.getMessage());
            Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

        mTimer.setText(TimeUtil.timeOfDate(EVENT_LOAD.getStartDate()));

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

        setData();
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


    private void suppression() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setMessage(getResources().getString(R.string.alert_dialog_delete_event))
                .setPositiveButton(getResources().getText(R.string.button_yes), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObjectCrypt params = new JSONObjectCrypt();
                        params.putCryptParameter("uid", USER.getUid());
                        params.putCryptParameter("uid_event", EVENT_LOAD.getUid());

                        JSONController.getJsonObjectFromUrl(Constants.URL_EVENT_DELETE, getBaseContext(), params, new JSONObjectListener() {
                            @Override
                            public void onJSONReceived(JSONObject jsonObject) {
                                if (jsonObject.length() == 3) {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.event_delete), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onJSONReceivedError(VolleyError volleyError) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                Log.w("Response", "Erreur:" + volleyError.toString());
                            }
                        });
                    }
                })
                .setNegativeButton(getResources().getText(R.string.button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        if (EVENT_LOAD.getOwner_uid().equalsIgnoreCase(USER.getUid())) {
            inscrit = true;
            linearParticipate.setVisibility(View.GONE);
            linearAnnuler.setVisibility(View.VISIBLE);
            linearButton.setVisibility(View.VISIBLE);
        }

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_event", EVENT_LOAD.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_EVENT_ISREGISTERED, getBaseContext(), params, new JSONObjectListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                inscrit = true;
                textViewInscription.setText(getResources().getString(R.string.unregister));
                buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_oui_50));
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                inscrit = false;
                textViewInscription.setText(getResources().getString(R.string.register));
                buttonInscription.setImageDrawable(getResources().getDrawable(R.drawable.inscrit_50));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void incription() {
        Drawable resources = null;
        String inscriptionText = "";
        if (!inscrit) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.register_message) + " " + EVENT_LOAD.getName(), Toast.LENGTH_SHORT).show();
            inscriptionText = getResources().getString(R.string.unregister);
            resources = getResources().getDrawable(R.drawable.inscrit_oui_50);
            inscrit = true;
            onRegisterUser();
        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.unregister_messge) + " " + EVENT_LOAD.getName(), Toast.LENGTH_SHORT).show();
            inscriptionText = getResources().getString(R.string.register);
            resources = getResources().getDrawable(R.drawable.inscrit_50);
            inscrit = false;
            onUnregisterUser();
        }
        textViewInscription.setText(inscriptionText);
        buttonInscription.setImageDrawable(resources);
        createFragment();
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
                Toast.makeText(getBaseContext(), getResources().getString(R.string.register_message), Toast.LENGTH_SHORT).show();
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
        createFragment();
        super.onResume();
    }

    private String cutString(String s) {
        if (5 > s.length()) {
            return null;
        }

        StringBuilder temp = new StringBuilder();

        int i = 0;
        if (11 != -1) {
            for (i = 11; i < 5 + 11; i++) {
                temp.append(s.charAt(i));
            }
        } else {
            for (i = 0; i < 5; i++) {
                temp.append(s.charAt(i));
            }
        }
        return temp.toString();
    }
}