package com.example.nalone.ui.evenements.display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Adapter.ItemEventListAdapter;
import com.example.nalone.Adapter.ItemFiltreAdapter;
import com.example.nalone.Adapter.ItemImagePersonAdapter;
import com.example.nalone.Adapter.ItemMesEventListAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.Evenement;
import com.example.nalone.HomeActivity;
import com.example.nalone.R;
import com.example.nalone.Visibilite;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.signUpActivities.SignUpProfilActivity;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.nalone.util.Constants.EVENTS_DB_REF;
import static com.example.nalone.util.Constants.EVENTS_LIST;
import static com.example.nalone.util.Constants.MARKERS_EVENT;
import static com.example.nalone.util.Constants.USERS_DB_REF;
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

        rootView = inflater.inflate(R.layout.fragment_mes_evenements_lits, container, false);

       updateEvents();

        return rootView;
    }

    private void updateEvents() {

        final List<Evenement> itemEvents = new ArrayList<>();

        for(int i = 0; i < EVENTS_LIST.size(); i++){
            MarkerOptions m = new MarkerOptions();
            Evenement e = Constants.EVENTS_LIST.get(i+"");

            m.title(e.getNom());

            if (e.getProprietaire().equalsIgnoreCase(USER_ID)) {
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

        mAdapterEventList.setOnItemClickListener(new ItemMesEventListAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                Constants.targetZoom = MARKERS_EVENT.get(itemEvents.get(position).getId()+"").getPosition();
                EvenementsFragment.viewPager.setCurrentItem(0);
            }

            @Override
            public void onDeleteClick(final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Vous êtes sur le point de supprimer un évènement ! Cette action est irréversible ! Voulez-vous continuez ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "Vous avez supprimé(e) un évènement !", Toast.LENGTH_SHORT).show();
                                removeEvent(position);
                                updateEvents();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create();
                builder.show();
            }

            @Override
            public void onEditClick(int position) {
                Toast.makeText(getContext(), "Edit event", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDataChangeListener() {
        updateEvents();
    }

    private void removeEvent(int position) {
        HashMap<String, Evenement> tempListEvent = new HashMap();
        HashMap<String, MarkerOptions> tempListMarkers = new HashMap();
        for(int i = 0; i < EVENTS_LIST.size(); i++){
            Evenement e = EVENTS_LIST.get(i+"");
            if(position == 0) {
                if (i != position) {
                    tempListEvent.put((i - 1) + "", EVENTS_LIST.get(i + ""));
                    tempListMarkers.put((i - 1) + "", MARKERS_EVENT.get(i + ""));
                    e.setId(i-1);
                }
            }else if(position == EVENTS_LIST.size()-1){
                if(i < position){
                    tempListEvent.put(i + "", EVENTS_LIST.get(i + ""));
                    tempListMarkers.put(i + "", MARKERS_EVENT.get(i + ""));
                    e.setId(i);
                }
            }else {
                if (i < position) {
                    tempListEvent.put(i + "", EVENTS_LIST.get(i + ""));
                    tempListMarkers.put(i + "", MARKERS_EVENT.get(i + ""));
                    e.setId(i);
                }

                if(i == position){
                    tempListEvent.put((i) + "", EVENTS_LIST.get(i+1 + ""));
                    tempListMarkers.put((i) + "", MARKERS_EVENT.get(i+1 + ""));
                    e.setId(i);
                }

                if (i > position) {
                    tempListEvent.put((i - 1) + "", EVENTS_LIST.get(i + ""));
                    tempListMarkers.put((i - 1) + "", MARKERS_EVENT.get(i + ""));
                    e.setId(i-1);
                }
            }
        }
        EVENTS_LIST = tempListEvent;
        for(Map.Entry<String, MarkerOptions> m : MARKERS_EVENT.entrySet()){
            Log.w("Map", "Map " + m.getValue().getTitle());
        }
        MARKERS_EVENT = tempListMarkers;
        EVENTS_DB_REF.setValue(EVENTS_LIST);
    }
}