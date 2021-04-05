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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.enumeration.FiltreDate;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class PlanningActivity extends AppCompatActivity {

    private ImageView buttonCalendar, buttonBack;
    private TextView nextEventName,nextEventCity,nextEventDate,nextEventTime, nextEventNbMembers, nextEventStatus;
    private Evenement nextEvent;


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


        nextEventStatus = findViewById(R.id.nextEventStatus);
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

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callJson(){
        nextEvent = null;
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.addParameter("uid", USER.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_EVENT_NEXT, getBaseContext(), params, new JSONObjectListener() {

            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                nextEvent = (Evenement) JSONController.convertJSONToObject(jsonObject, Evenement.class);
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }


        });
    }

}