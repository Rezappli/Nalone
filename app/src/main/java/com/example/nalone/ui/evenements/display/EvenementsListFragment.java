package com.example.nalone.ui.evenements.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nalone.Adapter.ItemEventAdapter;
import com.example.nalone.Adapter.ItemEventListAdapter;
import com.example.nalone.Adapter.ItemFiltreAdapter;
import com.example.nalone.Adapter.ItemImagePersonAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.Visibilite;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.util.Constants;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import static com.example.nalone.util.Constants.EVENTS_LIST;
import static com.example.nalone.util.Constants.USER_ID;

public class EvenementsListFragment extends Fragment implements CoreListener {

    private List<Evenement> itemEvents = new ArrayList<>();

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

        /*for(int i = 0; i < 10; i++){
            membres_inscrits.add(new ItemImagePerson(R.drawable.ci_musique));
        }

        mAdapterInscrits = new ItemImagePersonAdapter(membres_inscrits);

        mRecyclerViewInscrit = rootView.findViewById(R.id.recyclerViewMembresInscritsEventList);
        mLayoutManagerInscrit = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewInscrit.setLayoutManager(mLayoutManagerInscrit);
        mRecyclerViewInscrit.setAdapter(mAdapterInscrits);*/

        return rootView;
    }

    private void updateItems(){
        itemEvents.clear();
        for(int i = 0; i < EVENTS_LIST.size(); i++){
            MarkerOptions m = new MarkerOptions();
            Evenement e = EVENTS_LIST.get(i+"");

            m.title(e.getNom());

            if(e.getVisibilite().equals(Visibilite.PRIVE)){
                if(e.getMembres_inscrits().contains(USER_ID)){
                    itemEvents.add(e);
                }
            }else{
                itemEvents.add(e);
            }

        }

        mAdapterEventList = new ItemEventListAdapter(itemEvents);

        mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewEventList);
        mLayoutManagerEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerEvent);
        mRecyclerViewEvent.setAdapter(mAdapterEventList);
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }
}