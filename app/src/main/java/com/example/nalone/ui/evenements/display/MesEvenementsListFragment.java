package com.example.nalone.ui.evenements.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Adapter.ItemEventListAdapter;
import com.example.nalone.Adapter.ItemFiltreAdapter;
import com.example.nalone.Adapter.ItemImagePersonAdapter;
import com.example.nalone.Adapter.ItemMesEventListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MesEvenementsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MesEvenementsListFragment extends Fragment implements CoreListener {

    private View rootView;
    private RecyclerView mRecyclerViewEvent;
    private ItemMesEventListAdapter mAdapterEventList;
    private RecyclerView.LayoutManager mLayoutManagerMesEvent;

    private RecyclerView mRecyclerViewInscrit;
    private ItemImagePersonAdapter mAdapterInscrits;
    private RecyclerView.LayoutManager mLayoutManagerInscrit;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MesEvenementsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EvenementsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MesEvenementsListFragment newInstance(String param1, String param2) {
        MesEvenementsListFragment fragment = new MesEvenementsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mes_evenements_lits, container, false);

        final List<Evenement> itemEvents = new ArrayList<>();
        final List<ItemImagePerson> membres_inscrits = new ArrayList<>();

        for(int i = 0; i < EVENTS_LIST.size(); i++){
            MarkerOptions m = new MarkerOptions();
            Evenement e = Constants.EVENTS_LIST.get(i+"");

            m.title(e.getNom());

            if(e.getVisibilite().equals(Visibilite.PRIVE)){
                if(e.getMembres_inscrits().contains(USER_ID)){
                    itemEvents.add(e);
                }
            }else{
                itemEvents.add(e);
            }

        }

        mAdapterEventList = new ItemMesEventListAdapter(itemEvents);

        mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewMesEventList);
        mLayoutManagerMesEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerMesEvent);
        mRecyclerViewEvent.setAdapter(mAdapterEventList);

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

    @Override
    public void onDataChangeListener() {
        //updateItems();
    }
}