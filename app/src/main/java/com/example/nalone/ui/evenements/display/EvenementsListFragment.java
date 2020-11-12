package com.example.nalone.ui.evenements.display;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nalone.adapter.ItemEventListAdapter;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.adapter.ItemImagePersonAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.Evenement;
import com.example.nalone.InfosEvenementsActivity;
import com.example.nalone.R;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.util.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.nolonelyBundle;

public class EvenementsListFragment extends Fragment implements CoreListener {

    private ArrayList<Evenement> itemEvents = new ArrayList<>();

    private View rootView;
    private RecyclerView mRecyclerViewEvent;
    private ItemEventListAdapter mAdapterEventList;
    private RecyclerView.LayoutManager mLayoutManagerEvent;

    private final List<ItemFiltre> filtres = new ArrayList<>();
    private RecyclerView mRecyclerViewFiltre;
    private ItemFiltreAdapter mAdapterFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;

    private RecyclerView mRecyclerViewInscrit;
    private ItemImagePersonAdapter mAdapterInscrits;
    private RecyclerView.LayoutManager mLayoutManagerInscrit;


    public EvenementsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_evenements_list, container, false);

        final List<ItemImagePerson> membres_inscrits = new ArrayList<>();

        filtres.add(new ItemFiltre("Art"));
        filtres.add(new ItemFiltre("Sport"));
        filtres.add(new ItemFiltre("Musique"));
        filtres.add(new ItemFiltre("Fête"));
        filtres.add(new ItemFiltre("Danse"));
        filtres.add(new ItemFiltre("Numérique"));
        filtres.add(new ItemFiltre("Informatique"));
        filtres.add(new ItemFiltre("Manifestation"));

        mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);

        updateItems();

        return rootView;
    }

    private void updateItems(){
        itemEvents.clear();
        /*float[] results = new float[1];
        for(int i = 0; i < EVENTS_LIST.size(); i++){
            Evenement e = EVENTS_LIST.get(i+"");
            MarkerOptions m = MARKERS_EVENT.get(e.getId()+"");

            m.title(e.getNom());

            if(e.getVisibilite().equals(Visibility.PRIVE)){
                if(e.getMembres_inscrits().contains(USER_ID)){
                    Location.distanceBetween(USER_LATLNG.latitude, USER_LATLNG.longitude,
                            m.getPosition().latitude, m.getPosition().longitude, results);
                    if(results[0] < range) {
                        itemEvents.add(e);
                    }
                }
            }else{
                Location.distanceBetween(USER_LATLNG.latitude, USER_LATLNG.longitude,
                        m.getPosition().latitude, m.getPosition().longitude, results);
                if(results[0] < 50000) {
                    itemEvents.add(e);
                }
            }

        }*/

        mAdapterEventList = new ItemEventListAdapter(itemEvents);

        mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewEventList);
        mLayoutManagerEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerEvent);
        mRecyclerViewEvent.setAdapter(mAdapterEventList);

        mAdapterEventList.setOnItemClickListener(new ItemEventListAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                Constants.targetZoom = new LatLng(itemEvents.get(position).getLocation().getLatitude(), itemEvents.get(position).getLocation().getLongitude());
                EvenementsFragment.viewPager.setCurrentItem(0);
            }

            @Override
            public void onSignInClick(int position) {
                InfosEvenementsActivity.EVENT_LOAD = itemEvents.get(position);
                startActivity(new Intent(getContext(), InfosEvenementsActivity.class));
            }
        });
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }

    @Override
    public void onUpdateAdapter() {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(nolonelyBundle.getSerializable("events_list") == null) {
            nolonelyBundle.putSerializable("events_list", itemEvents);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
            if (nolonelyBundle.getSerializable("events_list") != null) {
                itemEvents = (ArrayList<Evenement>) nolonelyBundle.getSerializable("events_list");
                onUpdateAdapter();
            } else {
                updateItems();
            }

    }
}