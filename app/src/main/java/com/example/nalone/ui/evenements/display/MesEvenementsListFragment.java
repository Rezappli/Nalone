package com.example.nalone.ui.evenements.display;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Horloge;
import com.example.nalone.R;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;


public class MesEvenementsListFragment extends Fragment {

    private View rootView;

    public static Visibility visibiliteEdit;

    private LinearLayout linearSansEvent;

    //private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewEnCours,mRecyclerViewBientot, mRecyclerViewFini;
    private FirestoreRecyclerAdapter adapter;

    private int iterator = 0;


    private ArrayList<Evenement> itemEvents = new ArrayList<>();

    private CardView mesEvents;
    private NavController navController;
    private SwipeRefreshLayout swipeContainer;
    private List<String> events;
    private int nbInvit;
    private CardView cardViewInvits;
    private TextView textViewNbInvit;
    private Horloge horloge;

    private LinearLayout sansEnCours, sansBientot, sansFini;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mes_evenements_list, container, false);

        createFragment();
        return rootView;
    }

    private void reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private void createFragment() {
        nbInvit = 0;
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.AmisSwipeRefreshLayout);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        this.configureSwipeRefreshLayout();
        linearSansEvent = rootView.findViewById(R.id.linearSansEvent);
        //mRecyclerView = rootView.findViewById(R.id.recyclerViewMesEventList);
        mesEvents = rootView.findViewById(R.id.mesEvents);
        cardViewInvits = rootView.findViewById(R.id.cardViewInvits);
        textViewNbInvit = rootView.findViewById(R.id.nbInvits);

        mStoreBase.collection("users").document(USER.getUid()).collection("events_received")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.w("Invitations", document.getId());
                                nbInvit++;
                                Log.w("Invitations", nbInvit+"");
                            }
                        }
                        Log.w("Invitations", nbInvit+"");
                        if(nbInvit != 0){
                            Log.w("Invitations", "Pop up");
                            cardViewInvits.setVisibility(View.VISIBLE);
                            textViewNbInvit.setText(nbInvit+"");
                        }else{
                            cardViewInvits.setVisibility(View.GONE);
                        }
                        //loading.setVisibility(View.GONE);
                    }
                });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        cardViewInvits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_evenements_to_navigation_invit_event);
            }
        });
        mesEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_evenements_to_navigation_creations_evenements);
            }
        });

        mRecyclerViewEnCours = rootView.findViewById(R.id.recyclerViewEventListEnCours);
        mRecyclerViewBientot = rootView.findViewById(R.id.recyclerViewEventListBientot);
        mRecyclerViewFini = rootView.findViewById(R.id.recyclerViewEventListFini);

        sansEnCours = rootView.findViewById(R.id.linearSansEnCours);
        sansBientot = rootView.findViewById(R.id.linearSansBientot);
        sansFini = rootView.findViewById(R.id.linearSansFini);

        mStoreBase.collection("users").document(USER_ID).collection("events_join")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.w("Statut event_join"," statut");
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Evenement e = doc.toObject(Evenement.class);
                                //e.setStatusEvent(horloge.verifStatut(new Date(), e.getStartDate().toDate()));
                                //Log.w("Statut event_join",horloge.verifStatut(new Date(), e.getStartDate().toDate()) + " statut");
                                mStoreBase.collection("users").document(USER_ID).collection("events_join").document(e.getUid()).set(e);
                            }
                            initAdapter();
                        }
                    }
                });

    }

    private void initAdapter(){
        adapterEvents(StatusEvent.ENCOURS, mRecyclerViewEnCours);
        adapterEvents(StatusEvent.BIENTOT, mRecyclerViewBientot);
        adapterEvents(StatusEvent.FINI, mRecyclerViewFini);

    }
    private void adapterEvents(final StatusEvent se, final RecyclerView recyclerView) {

       /* mStoreBase.collection("events").whereEqualTo("statutEvent", se).whereNotEqualTo("ownerDoc",USER_REFERENCE)
                .get()
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        if(se == StatusEvent.EN_COURS){
                            sansEnCours.setVisibility(View.VISIBLE);
                        }
                    }
                });*/
        mStoreBase.collection("users").document(USER_ID).collection("events_join")
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        linearSansEvent.setVisibility(View.VISIBLE);
                    }
                });

                            Log.w("event", " Check list");
        Query query = mStoreBase.collection("users").document(USER_ID).collection("events_join").whereEqualTo("statusEvent", se);

        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();


        adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {

                                    @NonNull
                                    @Override
                                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list_, parent, false);
                                        return new EventViewHolder(view);
                                    }

                                    @Override
                                    protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                                        //holder.mImageView.setImageResource(e.getImage());
                                        Log.w("creation fragment", "Adapter mes events");

                                        holder.mTitle.setText((e.getName()));
                                        //holder.mDate.setText((dateFormat.format(e.getStartDate().toDate())));
                                        //holder.mTime.setText((timeFormat.format(e.getStartDate().toDate())));
                                        holder.mVille.setText((e.getCity()));
                                        //holder.mDescription.setText((e.getDescription()));
                                        holder.mProprietaire.setText(e.getOwner_uid());
                                        holder.textViewNbMembers.setText(e.getNbMembers() + "");


                                        holder.mAfficher.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                InfosEvenementsActivity.EVENT_LOAD = e;
                                                InfosEvenementsActivity.type = "nouveau";
                                                navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
                                            }
                                        });


                                        if (se == StatusEvent.BIENTOT) {
                                            sansBientot.setVisibility(View.GONE);
                                        }
                                        if (se == StatusEvent.ENCOURS) {
                                            sansEnCours.setVisibility(View.GONE);
                                        }
                                        if (se == StatusEvent.FINI) {
                                            sansFini.setVisibility(View.GONE);
                                        }

//                                            loading.setVisibility(View.GONE);
                                    }
                                };
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                                recyclerView.setAdapter(adapter);
                                adapter.startListening();

                                Log.w("count", iterator + "");

                                Log.w("count", iterator + "");



    }



    private class EventViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        //public TextView mDescription;
        public TextView mProprietaire;
        public CardView mAfficher;
        public CardView mCarwViewOwner;
        public  TextView textViewNbMembers;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            //mDescription = itemView.findViewById(R.id.descriptionEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.cardViewEventList);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);

        }
    }

    private void configureSwipeRefreshLayout(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initAdapter();
            }
        });
    }

    @Override
    public void onResume() {
        //createFragment();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
            adapter = null;
        }
    }

}