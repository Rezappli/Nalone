package com.nolonely.mobile.ui.evenements.display;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.StatusEvent;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.util.Constants;
import com.nolonely.mobile.util.TimeUtil;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.nolonely.mobile.util.Constants.USER;

public class InfosEventCreationActivity extends AppCompatActivity {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    private Evenement EVENT_LOAD;

    public static String type;
    private TextView nbParticipants;
    private int participants;
    private ImageView imageEvenement, buttonPartager, buttonCanceled;
    private LinearLayout linearButton;
    private CardView cardViewTermine;
    private ImageView buttonBack;
    private CardView cardViewProgress;
    private View viewGrey;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_event_creation);
        createFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createFragment() {
        if (getIntent() != null) {
            EVENT_LOAD = (Evenement) getIntent().getSerializableExtra("event");
        }
        participants = 0;
        nbParticipants = findViewById(R.id.nbParticipants);
        linearButton = findViewById(R.id.linearButton);
        cardViewTermine = findViewById(R.id.cardViewTermine);
        buttonBack = findViewById(R.id.buttonBack);
        imageEvenement = findViewById(R.id.imageEvenement);
        buttonCanceled = findViewById(R.id.buttonCanceled);

        buttonCanceled.setOnClickListener(v -> deleteEvent());
        buttonBack.setOnClickListener(v -> onBackPressed());
        cardViewProgress = findViewById(R.id.cardViewProgress);
        viewGrey = findViewById(R.id.viewGrey);

        buttonPartager = findViewById(R.id.buttonPartager);
        buttonPartager.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Clicked on Share ", Toast.LENGTH_SHORT).show();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });


        if (EVENT_LOAD.getStatusEvent() == StatusEvent.FINI) {
            linearButton.setVisibility(View.GONE);
            cardViewTermine.setVisibility(View.VISIBLE);
        }

        if (EVENT_LOAD != null) {
            initWidgets();
        }
    }

    private void initWidgets() {
        TextView mTitle = findViewById(R.id.title);
        TextView mDateStart = findViewById(R.id.dateStart);
        TextView mDateEnd = findViewById(R.id.dateEnd);
        TextView mTimeStart = findViewById(R.id.timeStart);
        TextView mTimeEnd = findViewById(R.id.timeEnd);
        TextView mDescription = findViewById(R.id.description);

        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());

        nbParticipants.setText(EVENT_LOAD.getNbMembers() + "");

        if (EVENT_LOAD.getDescription().matches("")) {
            mDescription.setVisibility(View.GONE);
        } else {
            mDescription.setText(EVENT_LOAD.getDescription());
        }

        try {
            mDateStart.setText(TimeUtil.dateOfDateLetter(EVENT_LOAD.getStartDate()));
            mDateEnd.setText(TimeUtil.dateOfDateLetter(EVENT_LOAD.getEndDate()));
        } catch (ParseException e) {
            Log.w("Response", "Erreur:" + e.getMessage());
            Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

        mTimeStart.setText(TimeUtil.timeOfDate(EVENT_LOAD.getStartDate()));
        mTimeEnd.setText(TimeUtil.timeOfDate(EVENT_LOAD.getEndDate()));

        Constants.setEventImage(EVENT_LOAD, imageEvenement);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.alert_dialog_delete_event))
                .setPositiveButton(getResources().getText(R.string.button_yes), (dialog, id) -> {
                    cardViewProgress.setVisibility(View.VISIBLE);
                    viewGrey.setVisibility(View.VISIBLE);
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
                })
                .setNegativeButton(getResources().getText(R.string.button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
    }


}