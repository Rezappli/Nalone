package com.example.nalone.ui.evenements.display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.enumeration.FiltreDate;
import com.example.nalone.enumeration.VisibilityMap;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.enumeration.TypeEvent.*;
import static com.example.nalone.util.Constants.USER;

public class PlanningActivity extends AppCompatActivity {

    private ImageView buttonCalendar, buttonBack,imageTypeEvent,showMoreButton;
    private TextView nextEventName,nextEventCity,nextEventDate,nextEventTime, nextEventNbMembers, nextEventStatus;
    private List<Evenement> eventsSoon, eventsEnd;
    private Evenement nextEvent;
    private LinearLayout linearPlanning,linearNoResult, linearSoon, linearEnd;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        buttonCalendar = findViewById(R.id.buttonCalendar);
        buttonBack = findViewById(R.id.buttonBack);
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
                       // initRecyclerEnd();
                        linearEnd.setVisibility(View.GONE);
                    }else{
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
                        // initRecyclerEnd();
                        linearSoon.setVisibility(View.GONE);
                    }else{
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String final_date_text = "";
        nextEventName.setText(nextEvent.getName());
        nextEventCity.setText(nextEvent.getCity());
        nextEventNbMembers.setText(nextEvent.getNbMembers()+"");
        Date date = sdf.parse(nextEvent.getStartDate());
        String date_text = Constants.formatD.format(date);
        for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }
        nextEventDate.setText(final_date_text);
        nextEventTime.setText(cutString(nextEvent.getStartDate(), 5, 11));
        nextEventName.setText(nextEvent.getName());
        imageTypeEvent.setImageResource(nextEvent.getImageCategory());

    }

    private String cutString(String s, int length, int start){
        if(length > s.length()){
            return null;
        }

        String temp = "";

        int i = 0;
        if(start != -1){
            for(i=start; i<length+start; i++){
                temp += s.charAt(i);
            }
        }else{
            for(i=0; i<length; i++){
                temp += s.charAt(i);
            }
        }
        return temp;
    }

}