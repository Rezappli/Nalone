package com.example.nalone.ui.evenements.display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.MapEvenementAdapter;
import com.example.nalone.enumeration.VisibilityMap;
import com.example.nalone.json.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Constants;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.range;

public class EventPlanningActivity extends AppCompatActivity implements DatePickerListener{
    private RecyclerView mRecycler;
    private List<Evenement> evenementList;
    private MapEvenementAdapter mAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_planning);
        HorizontalPicker picker = findViewById(R.id.horizontalPicker);
        HorizontalPicker picker1 = findViewById(R.id.horizontalPicker);
        picker.setListener(this)
                .setOffset(7)
                .setDateSelectedColor(getResources().getColor(R.color.colorPrimary))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.WHITE)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.WHITE)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .showTodayButton(false)
                .init();
        picker.setBackgroundColor(Color.WHITE);
        picker.setDate(new DateTime());
        mRecycler = findViewById(R.id.recyclerViewPlanning);
        getEvent(new DateTime());
    }

//
    private void configureRecyclerView() {
        this.mAdapter = new MapEvenementAdapter(this.evenementList, true);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.mRecycler.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        this.mRecycler.setLayoutManager(llm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getEvent(DateTime date){
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.addParameter("uid", USER.getUid());
        params.addParameter("date", date);

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_DATE, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    evenementList = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        evenementList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }
                    configureRecyclerView();
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


    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.w("DATETIME", dateSelected+"");
    }
}