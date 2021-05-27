package com.example.nalone.ui.evenements.display;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
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

public class PlanningRegistrationsFragment extends Fragment {

    private ImageView imageTypeEvent;
    private ImageView showMoreButton;
    private TextView nextEventName, nextEventCity, nextEventDate, nextEventTime, nextEventNbMembers, nextEventStatus, textViewTitleDebut, differenceDate;
    private List<Evenement> eventsSoon, eventsEnd;
    private Evenement nextEvent;
    private LinearLayout linearPlanning, linearNoResult, linearSoon, linearEnd, linearNext;
    private RecyclerView mRecyclerSoon, mRecyclerEnd;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planning_registrations, container, false);
        textViewTitleDebut = view.findViewById(R.id.textViewTitleDebut);
        differenceDate = view.findViewById(R.id.differenceDate);
        nextEventName = view.findViewById(R.id.nextEventName);
        nextEventCity = view.findViewById(R.id.nextEventCity);
        nextEventDate = view.findViewById(R.id.nextEventDate);
        nextEventTime = view.findViewById(R.id.nextEventTime);
        nextEventNbMembers = view.findViewById(R.id.nextEventNbMembers);
        nextEventStatus = view.findViewById(R.id.textViewStatus);
        imageTypeEvent = view.findViewById(R.id.imageTypeEvent);
        showMoreButton = view.findViewById(R.id.showMoreButton);
        linearNext = view.findViewById(R.id.linearNext);
        linearNoResult = view.findViewById(R.id.linearNoResult);
        linearEnd = view.findViewById(R.id.linearEnd);
        linearSoon = view.findViewById(R.id.linearSoon);
        linearNext.setVisibility(View.GONE);
        linearSoon.setVisibility(View.GONE);
        linearEnd.setVisibility(View.GONE);

        mRecyclerEnd = view.findViewById(R.id.recyclerViewEnd);
        mRecyclerSoon = view.findViewById(R.id.recyclerViewSoon);

        showMoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), InfosEvenementsActivity.class);
            intent.putExtra("event", nextEvent);
            startActivity(intent);
        });

        callNextEvent();
        callEventEnd();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callNextEvent() {
        nextEvent = null;
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    if (jsonArray.length() > 0) {
                        nextEvent = (Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(0), Evenement.class);
                        linearNext.setVisibility(View.VISIBLE);
                        linearNoResult.setVisibility(View.GONE);
                        callEventSoon();
                        initNextEvent();
                    } else {
                        linearNext.setVisibility(View.GONE);
                        linearNoResult.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException | ParseException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callEventEnd() {
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("status", "FINI");
        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    if (jsonArray.length() > 0) {
                        eventsEnd = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsEnd.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        initRecyclerEnd();
                        linearEnd.setVisibility(View.VISIBLE);
                    } else {
                        linearEnd.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callEventSoon() {
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("status", "BIENTOT");
        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {

                    if (jsonArray.length() > 1) {
                        eventsSoon = new ArrayList<>();
                        for (int i = 1; i < jsonArray.length(); i++) {
                            eventsSoon.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        initRecyclerSoon();
                        linearSoon.setVisibility(View.VISIBLE);
                    } else {
                        linearSoon.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initNextEvent() throws ParseException {
        nextEvent.replaceFields(nextEventName, nextEventCity, nextEventNbMembers, nextEventDate, nextEventTime, imageTypeEvent);
        if (nextEvent.getStatusEvent() == StatusEvent.ENCOURS) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_from));
        } else if (nextEvent.getStatusEvent() == StatusEvent.BIENTOT) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_in));
        }
        TimeUtil.differenceDate(new Date(), new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(nextEvent.getStartDate()), differenceDate);
    }

    private void initRecyclerEnd() {
        //EvenementAdapter mAdapterEnd = new EvenementAdapter(this.eventsEnd, R.layout.item_evenement_bis, false);
        this.mRecyclerEnd.setAdapter(new EvenementAdapter(this.eventsEnd, R.layout.item_evenement_bis, false));
        //final LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        this.mRecyclerEnd.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void initRecyclerSoon() {
        //EvenementAdapter mAdapterSoon = new EvenementAdapter(this.eventsSoon, R.layout.item_evenement_bis, false);
        this.mRecyclerSoon.setAdapter(new EvenementAdapter(this.eventsSoon, R.layout.item_evenement_bis, false));
        //final LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        this.mRecyclerSoon.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

}