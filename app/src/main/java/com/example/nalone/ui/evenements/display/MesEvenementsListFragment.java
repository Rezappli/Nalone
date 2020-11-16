package com.example.nalone.ui.evenements.display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.adapter.ItemImagePersonAdapter;
import com.example.nalone.adapter.ItemMesEventListAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.CreateEventActivity;
import com.example.nalone.Evenement;
import com.example.nalone.R;
import com.example.nalone.Visibility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.nolonelyBundle;


public class MesEvenementsListFragment extends Fragment implements CoreListener {

    private View rootView;
    private RecyclerView mRecyclerViewEvent;
    private ItemMesEventListAdapter mAdapterEventList;
    private RecyclerView.LayoutManager mLayoutManagerMesEvent;

    private RecyclerView mRecyclerViewInscrit;
    private ItemImagePersonAdapter mAdapterInscrits;
    private RecyclerView.LayoutManager mLayoutManagerInscrit;

    public static String nameEvent;
    public static String cityEdit;
    public static String adresseEdit;
    public static String dateEdit;
    public static String timeEdit;
    public static String descEdit;
    public static Visibility visibiliteEdit;

    private LinearLayout linearSansEvent;
    private ImageView addEvent;

    private ArrayList<Evenement> itemEvents = new ArrayList<>();

    public MesEvenementsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mes_evenements_lits, container, false);


        linearSansEvent = rootView.findViewById(R.id.linearSansEvent);
        mRecyclerViewEvent = rootView.findViewById(R.id.recyclerViewMesEventList);
        addEvent = rootView.findViewById(R.id.create_event_button);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CreateEventActivity.class));
            }
        });

        updateEvents();

        return rootView;
    }

    private void updateEvents() {
        itemEvents.clear();

        mStoreBase.collection("events")
                .whereEqualTo("ownerdoc", USER_REFERENCE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Evenement e = document.toObject(Evenement.class);
                                    itemEvents.add(e);
                                }
                                onUpdateAdapter();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("SPLASH", "Erreur : " + e.getMessage());
            }
        });

        /*for(int i = 0; i < EVENTS_LIST.size(); i++){
            MarkerOptions m = new MarkerOptions();
            Evenement e = Constants.EVENTS_LIST.get(i+"");

            m.title(e.getNom());

            if (e.getProprietaire().equalsIgnoreCase(USER_ID)) {
                itemEvents.add(e);
            }
        }*/

        if(itemEvents.isEmpty()){
            mRecyclerViewEvent.setVisibility(View.GONE);
            linearSansEvent.setVisibility(View.VISIBLE);
        }else{
            mRecyclerViewEvent.setVisibility(View.VISIBLE);
            linearSansEvent.setVisibility(View.GONE);
        }

        mAdapterEventList = new ItemMesEventListAdapter(itemEvents);


        mLayoutManagerMesEvent = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerViewEvent.setLayoutManager(mLayoutManagerMesEvent);
        mRecyclerViewEvent.setAdapter(mAdapterEventList);

        mAdapterEventList.setOnItemClickListener(new ItemMesEventListAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                /*Constants.targetZoom = MARKERS_EVENT.get(itemEvents.get(position).getId()+"").getPosition();
                EvenementsFragment.viewPager.setCurrentItem(0);*/
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

                /*Evenement e = EVENTS_LIST.get(itemEvents.get(position).getId()+"");

                nameEvent = e.getNom();
                cityEdit = e.getVille();
                adresseEdit = e.getAdresse();
                dateEdit = sdf.format(e.getDate());
                timeEdit = e.getTime();
                descEdit = e.getDescription();
                visibiliteEdit = e.getVisibilite();

                CreateEventActivity.edit = true;

                startActivity(new Intent(getContext(), CreateEventActivity.class));
                Toast.makeText(getContext(), "Edit event", Toast.LENGTH_SHORT).show();*/

            }
        });

    }

    @Override
    public void onDataChangeListener() {
        updateEvents();
    }

    //@Override
    public void onUpdateAdapter() {

    }

    private void removeEvent(int position) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(nolonelyBundle.getSerializable("my_events") == null) {
            nolonelyBundle.putSerializable("my_events", itemEvents);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
            if (nolonelyBundle.getSerializable("my_events") != null) {
                itemEvents = (ArrayList<Evenement>) nolonelyBundle.getSerializable("my_events");
                onUpdateAdapter();
            } else {
                updateEvents();
            }
        }
}