package com.nolonely.mobile.ui.evenements.display;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.EvenementAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.StatusEvent;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.ui.evenements.InfosEvenementsActivity;
import com.nolonely.mobile.util.Constants;
import com.nolonely.mobile.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nolonely.mobile.enumeration.StatusEvent.BIENTOT;
import static com.nolonely.mobile.enumeration.StatusEvent.ENCOURS;
import static com.nolonely.mobile.enumeration.StatusEvent.FINI;
import static com.nolonely.mobile.util.Constants.USER;

public class PlanningRegistrationsFragment extends Fragment {

    private ImageView imageTypeEvent;
    private TextView nextEventName, nextEventCity, nextEventDate, nextEventTime, nextEventNbMembers, textViewTitleDebut, differenceDate;
    private List<Evenement> eventsNow, eventsSoon, eventsEnd, eventsRecycler;
    private Evenement nextEvent;
    private EvenementAdapter evenementAdapter;
    private LinearLayout linearPlanning, linearNext, linearNoResultBis;
    private ProgressBar progressPlanningRegistration;
    private boolean nowChecked, nextChecked, endChecked, soonChecked;
    public Handler handler;
    private TextView textViewNow, textViewSoon, textViewEnd;


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
        nextEventNbMembers = view.findViewById(R.id.textViewNbMembers);
        imageTypeEvent = view.findViewById(R.id.imageTypeEvent);
        TextView showMoreButton = view.findViewById(R.id.showMoreButton);
        linearNext = view.findViewById(R.id.linearNext);
        linearNoResultBis = view.findViewById(R.id.linearNoResultBis);
        progressPlanningRegistration = view.findViewById(R.id.progressPlanningRegistration);
        linearNext.setVisibility(View.GONE);
        linearPlanning = view.findViewById(R.id.linearPlanning);
        linearPlanning.setVisibility(View.GONE);

        showMoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), InfosEvenementsActivity.class);
            intent.putExtra("event", nextEvent);
            startActivity(intent);
        });

        handler = new Handler();

        RecyclerView mRecyclerRegistrations = view.findViewById(R.id.recycleViewPlanningRegistrations);
        eventsRecycler = new ArrayList<>();
        evenementAdapter = new EvenementAdapter(eventsRecycler, R.layout.item_evenement_registration, false);
        evenementAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), InfosEvenementsActivity.class);
            intent.putExtra("event", eventsRecycler.get(position));
            intent.putExtra("isRegistered", true);
            startActivity(intent);
        });
        mRecyclerRegistrations.setAdapter(evenementAdapter);
        mRecyclerRegistrations.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        callNextEvent();
        callEventEnd();
        callEventNow();
        callEventSoon();

        textViewNow = view.findViewById(R.id.textViewPlanningRegistrationsNow);
        textViewSoon = view.findViewById(R.id.textViewPlanningRegistrationsSoon);
        textViewEnd = view.findViewById(R.id.textViewPlanningRegistrationsEnd);
        textViewNow.setOnClickListener(v -> updateDrawableClicked(ENCOURS));
        textViewSoon.setOnClickListener(v -> updateDrawableClicked(BIENTOT));
        textViewEnd.setOnClickListener(v -> updateDrawableClicked(FINI));
        return view;
    }

    private void updateDrawableClicked(StatusEvent statusEvent) {
        switch (statusEvent) {
            case FINI:
                changeTextViewBackground(textViewEnd, true);
                changeTextViewBackground(textViewSoon, false);
                changeTextViewBackground(textViewNow, false);
                updateRecyclerView(eventsEnd);
                break;
            case ENCOURS:
                changeTextViewBackground(textViewEnd, false);
                changeTextViewBackground(textViewSoon, false);
                changeTextViewBackground(textViewNow, true);
                updateRecyclerView(eventsNow);
                break;
            case BIENTOT:
                changeTextViewBackground(textViewEnd, false);
                changeTextViewBackground(textViewSoon, true);
                changeTextViewBackground(textViewNow, false);
                updateRecyclerView(eventsSoon);
                break;
        }
    }

    @Override
    public void onResume() {
        handler.post(runnable);
        super.onResume();
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    private void changeTextViewBackground(TextView textView, boolean isClicked) {
        textView.setBackground(ContextCompat.getDrawable(getContext(), isClicked ? R.drawable.tab_indicator : R.drawable.custom_button_signup));
        textView.setTextColor(isClicked ? Color.WHITE : getResources().getColor(R.color.secundaryTextColor));
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (endChecked && nowChecked && soonChecked && nextChecked) {
                if (linearPlanning.getVisibility() == View.GONE) {
                    linearPlanning.setVisibility(View.VISIBLE);
                    progressPlanningRegistration.setVisibility(View.GONE);
                    if (!eventsNow.isEmpty()) {
                        updateDrawableClicked(ENCOURS);
                    } else if (!eventsSoon.isEmpty()) {
                        updateDrawableClicked(BIENTOT);
                    } else if (!eventsEnd.isEmpty()) {
                        updateDrawableClicked(FINI);
                    } else {
                        updateDrawableClicked(ENCOURS);
                    }
                } else {
                    try {
                        if (nextEvent != null) {
                            TimeUtil.differenceDate(new Date(), new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(nextEvent.getStartDate()), differenceDate);
                        } else {
                            handler.removeCallbacks(runnable);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            handler.postDelayed(runnable, 0);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }

    private void updateRecyclerView(List<Evenement> evenements) {
        Log.w("EVENEMENTTT", evenements.size() + "");
        eventsRecycler.clear();
        if (!evenements.isEmpty()) {
            linearNoResultBis.setVisibility(View.GONE);
            eventsRecycler.addAll(evenements);
        } else {
            linearNoResultBis.setVisibility(View.VISIBLE);
        }
        evenementAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callNextEvent() {
        nextEvent = null;

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), JSONParams(BIENTOT), new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    if (jsonArray.length() > 0) {
                        nextEvent = (Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(0), Evenement.class);
                        linearNext.setVisibility(View.VISIBLE);
                        initNextEvent();
                    } else {
                        linearNext.setVisibility(View.GONE);
                    }

                    nextChecked = true;
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
    private JSONObjectCrypt JSONParams(StatusEvent statusEvent) {
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("status", statusEvent);
        params.putCryptParameter("type", "REGISTRATION");

        return params;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callEventNow() {

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), JSONParams(ENCOURS), new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    eventsNow = new ArrayList<>();

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsNow.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                    }

                    nowChecked = true;

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
    private void callEventEnd() {

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), JSONParams(FINI), new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    eventsEnd = new ArrayList<>();

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsEnd.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                    }
                    endChecked = true;

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

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_NEXT, getContext(), JSONParams(BIENTOT), new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Value:" + jsonArray.toString());
                try {
                    eventsSoon = new ArrayList<>();

                    if (jsonArray.length() > 1) {
                        for (int i = 1; i < jsonArray.length(); i++) {
                            eventsSoon.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                    }
                    soonChecked = true;

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
        } else if (nextEvent.getStatusEvent() == BIENTOT) {
            textViewTitleDebut.setText(getResources().getString(R.string.event_start_in));
        }
    }

}