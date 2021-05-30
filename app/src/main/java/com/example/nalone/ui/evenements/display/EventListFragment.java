package com.example.nalone.ui.evenements.display;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.EvenementAdapter;
import com.example.nalone.adapter.TypeEventAdapter;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.TypeEventObject;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.nalone.enumeration.TypeEvent.ART;
import static com.example.nalone.util.Constants.USER;

public class EventListFragment extends Fragment {

    private List<Evenement> eventsPopular;
    private List<Evenement> eventsSuggest;
    private LinearLayout linearLayoutPopular, linearLayoutSuggest, linearNoResult;
    private SearchView searchView;
    private RecyclerView recyclerViewSuggestion;
    private RecyclerView recyclerViewPopular;
    private ImageView imageViewFiltreSearch;
    private List<TypeEventObject> filtreTypeList;
    private RecyclerView recyclerTypeEvent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_list, container, false);
        linearLayoutPopular = rootView.findViewById(R.id.linearPopular);
        linearLayoutSuggest = rootView.findViewById(R.id.linearSuggest);
        linearNoResult = rootView.findViewById(R.id.linearNoResult);
        imageViewFiltreSearch = rootView.findViewById(R.id.filtreSearch);
        searchView = rootView.findViewById(R.id.searchViewSheet);
        recyclerViewSuggestion = rootView.findViewById(R.id.recyclerViewSuggestion);
        recyclerViewPopular = rootView.findViewById(R.id.recyclerViewSearchPopulaire);
        recyclerTypeEvent = rootView.findViewById(R.id.recyclerTypeEvent);
        searchView.setQueryHint("Recherche");
        initFiltres();

        return rootView;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initFiltres() {
        filtreTypeList = new ArrayList<>();

        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_art), getResources().getString(R.string.event_art), ART));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_sport), getResources().getString(R.string.event_sport), TypeEvent.SPORT));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_car), getResources().getString(R.string.event_car), TypeEvent.CAR));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_conference), getResources().getString(R.string.event_conference), TypeEvent.CONFERENCE));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_shop), getResources().getString(R.string.event_shop), TypeEvent.SHOP));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_show), getResources().getString(R.string.event_show), TypeEvent.SHOW));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_game), getResources().getString(R.string.event_game), TypeEvent.GAME));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_gather), getResources().getString(R.string.event_gather), TypeEvent.GATHER));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_movie), getResources().getString(R.string.event_movie), TypeEvent.MULTIMEDIA));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_music), getResources().getString(R.string.event_music), TypeEvent.MUSIC));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_party), getResources().getString(R.string.event_party), TypeEvent.PARTY));
        filtreTypeList.add(new TypeEventObject(getResources().getDrawable(R.drawable.event_science), getResources().getString(R.string.event_science), TypeEvent.SCIENCE));
        TypeEventObject typeEventObject = new TypeEventObject(getContext());

        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> imageList = new ArrayList<>();

        for (Integer i : typeEventObject.getListActivitiesImage()) {
            imageList.add(i);
        }

        Collections.addAll(nameList, typeEventObject.getListActivitiesName());

        TypeEventAdapter typeAdapter = new TypeEventAdapter(nameList, imageList, getContext());
        typeAdapter.setOnItemClickListener(position -> {
            SearchEventActivity.currentType = filtreTypeList.get(position).getmType();
            startActivity(new Intent(getContext(), SearchEventActivity.class));
        });
        recyclerTypeEvent.setAdapter(typeAdapter);
        final LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerTypeEvent.setLayoutManager(llm2);
    }

    private void configureSuggestion() {
        EvenementAdapter adapteSuggestion = new EvenementAdapter(this.eventsSuggest, R.layout.item_evenement, false);
        this.recyclerViewSuggestion.setAdapter(adapteSuggestion);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerViewSuggestion.setLayoutManager(llm);
        adapteSuggestion.setOnItemClickListener(position -> eventsSuggest.get(position).displayEventInfo(getContext(), false));
    }

    private void configurePopular() {
        EvenementAdapter adapterPopulaire = new EvenementAdapter(this.eventsPopular, R.layout.item_evenement, false);
        this.recyclerViewPopular.setAdapter(adapterPopulaire);
        final LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        this.recyclerViewPopular.setLayoutManager(llm1);
        adapterPopulaire.setOnItemClickListener(position -> eventsPopular.get(position).displayEventInfo(getContext(), false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getEventsList() {
        getEventPopular();
        getEventSuggest();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getEventSuggest() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        eventsSuggest = new ArrayList<>();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("type", "SUGGEST");

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_LIST_MAP, requireContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsSuggest.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        configureSuggestion();
                        linearLayoutSuggest.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.w("Response events popular", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response events popular", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getEventPopular() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        eventsPopular = new ArrayList<>();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("type", "POPULAR");

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_LIST_MAP, requireContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        linearLayoutPopular.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsPopular.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                        }
                        configurePopular();
                    } else {
                        linearNoResult.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.w("Response events popular", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response events popular", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }
}