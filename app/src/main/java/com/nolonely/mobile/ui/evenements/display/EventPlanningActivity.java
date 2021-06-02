package com.nolonely.mobile.ui.evenements.display;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.PlanningEvenementAdapter;
import com.nolonely.mobile.json.JSONController;
import com.nolonely.mobile.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.util.Constants;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.nolonely.mobile.util.Constants.USER;

public class EventPlanningActivity extends AppCompatActivity implements DatePickerListener {
    private RecyclerView mRecycler;
    private List<Evenement> evenementList;
    private PlanningEvenementAdapter mAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_planning);
        HorizontalPicker picker = findViewById(R.id.horizontalPicker);
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
    }

    private void configureRecyclerView() {
        this.mAdapter = new PlanningEvenementAdapter(this.evenementList, true);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.mRecycler.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        this.mRecycler.setLayoutManager(llm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getEvent(String date) {
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("date", date);

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_DATE, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    evenementList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        evenementList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }
                    configureRecyclerView();
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSelected(DateTime dateSelected) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(dateSelected.toDate());
        getEvent(s);
    }

}