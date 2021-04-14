package com.example.nalone.ui.evenements.display;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.NoLonelyActivity;
import com.example.nalone.adapter.EvenementAdapter;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;
import com.example.nalone.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class PlanningActivity extends NoLonelyActivity {

    private ImageView buttonCalendar;
    private ImageView imageTypeEvent;
    private ImageView showMoreButton;
    private TextView nextEventName,nextEventCity,nextEventDate,nextEventTime, nextEventNbMembers, nextEventStatus,textViewTitleDebut,differenceDate;
    private List<Evenement> eventsSoon, eventsEnd;
    private Evenement nextEvent;
    private LinearLayout linearPlanning,linearNoResult, linearSoon, linearEnd;
    private RecyclerView mRecyclerSoon, mRecyclerEnd;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        buttonCalendar = findViewById(R.id.buttonCalendar);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        textViewTitleDebut = findViewById(R.id.textViewTitleDebut);
        differenceDate = findViewById(R.id.differenceDate);
        nextEventName = findViewById(R.id.nextEventName);
        nextEventCity = findViewById(R.id.nextEventCity);
        nextEventDate = findViewById(R.id.nextEventDate);
        nextEventTime = findViewById(R.id.nextEventTime);
        nextEventNbMembers = findViewById(R.id.nextEventNbMembers);
        nextEventStatus = findViewById(R.id.textViewStatus);
        imageTypeEvent = findViewById(R.id.imageTypeEvent);
        showMoreButton = findViewById(R.id.showMoreButton);
        linearPlanning = findViewById(R.id.linearPlanning);
        linearNoResult = findViewById(R.id.linearNoResult);
        linearEnd = findViewById(R.id.linearEnd);
        linearSoon = findViewById(R.id.linearSoon);

        mRecyclerEnd = findViewById(R.id.recyclerViewEnd);
        mRecyclerSoon = findViewById(R.id.recyclerViewSoon);

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfosEvenementsActivity.EVENT_LOAD = nextEvent;
                startActivity(new Intent(getBaseContext(), InfosEvenementsActivity.class));
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),EventPlanningActivity.class));
            }
        });



        callJson();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callJson(){
        nextEvent = null;
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.addParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getBaseContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:"+jsonArray.toString());
                try {

                    nextEvent = (Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(0), Evenement.class);

                    if(nextEvent != null){
                        linearPlanning.setVisibility(View.VISIBLE);
                        linearNoResult.setVisibility(View.GONE);
                        initNextEvent();
                    }else{
                        linearPlanning.setVisibility(View.GONE);
                        linearNoResult.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException | ParseException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });

        params.addParameter("status", "FINI");
        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getBaseContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:"+jsonArray.toString());
                try {

                    eventsEnd = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        eventsEnd.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }
                    if(eventsEnd.isEmpty()){
                        linearEnd.setVisibility(View.GONE);
                    }else{
                        initRecyclerEnd();
                        linearEnd.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });

        params.remove("status");
        params.addParameter("status", "BIENTOT");
        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getBaseContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:"+jsonArray.toString());
                try {

                    eventsSoon = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        eventsSoon.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }
                    if(eventsSoon.isEmpty()){
                        linearSoon.setVisibility(View.GONE);
                    }else{
                        initRecyclerSoon();
                        linearSoon.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initNextEvent() throws ParseException {
        nextEvent.replaceFields(nextEventName,nextEventCity,nextEventNbMembers,nextEventDate,nextEventTime,imageTypeEvent);
        if(nextEvent.getStatusEvent() == StatusEvent.ENCOURS) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_from));
        } else if(nextEvent.getStatusEvent() == StatusEvent.BIENTOT){
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_in));
        }
        TimeUtil.differenceDate(new Date(),new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(nextEvent.getStartDate()),differenceDate);
    }

    private void initRecyclerEnd() {
        EvenementAdapter mAdapterEnd = new EvenementAdapter(this.eventsEnd, R.layout.item_evenement_bis, false);
        this.mRecyclerEnd.setAdapter(mAdapterEnd);
        final LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        this.mRecyclerEnd.setLayoutManager(llm);
    }

    private void initRecyclerSoon() {
        EvenementAdapter mAdapterSoon = new EvenementAdapter(this.eventsSoon, R.layout.item_evenement_bis, false);
        this.mRecyclerSoon.setAdapter(mAdapterSoon);
        final LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        this.mRecyclerSoon.setLayoutManager(llm);
    }



}