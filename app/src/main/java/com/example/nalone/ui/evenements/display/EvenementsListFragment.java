package com.example.nalone.ui.evenements.display;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nalone.enumeration.Visibility;
import com.example.nalone.objects.Evenement;
import com.example.nalone.R;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.objects.User;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.timeFormat;

public class EvenementsListFragment extends Fragment {

    private ArrayList<Evenement> itemEvents = new ArrayList<>();

    private View rootView;

    private final List<ItemFiltre> filtres = new ArrayList<>();
    private RecyclerView mRecyclerViewFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;

    private RecyclerView mRecyclerViewEnCours,mRecyclerViewBientot, mRecyclerViewFini;
    private FirestoreRecyclerAdapter mAdapter;
    private LinearLayout linearSansEvent;
    private ItemFiltreAdapter mAdapterFiltre;
    private NavController navController;

    private int iterator = 0;
    private List<String> events;
    private ProgressBar loading;

    private LinearLayout sansEnCours, sansBientot, sansFini;



    public EvenementsListFragment() {
        // Required empty public constructor
    }

    public void reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_evenements_list, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment() {

        Log.w("creation fragment", "Creation");
        events = new ArrayList<>();
        loading = rootView.findViewById(R.id.loading);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        /*filtres.add(new ItemFiltre("Art"));
        filtres.add(new ItemFiltre("Sport"));
        filtres.add(new ItemFiltre("Musique"));
        filtres.add(new ItemFiltre("Fête"));
        filtres.add(new ItemFiltre("Danse"));
        filtres.add(new ItemFiltre("Numérique"));
        filtres.add(new ItemFiltre("Informatique"));
        filtres.add(new ItemFiltre("Manifestation"));*/

        linearSansEvent = rootView.findViewById(R.id.linearSansEvent);
        sansEnCours = rootView.findViewById(R.id.linearSansEnCours);
        sansBientot = rootView.findViewById(R.id.linearSansBientot);
        sansFini = rootView.findViewById(R.id.linearSansFini);

        linearSansEvent.setVisibility(View.GONE);

       /* mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);*/

        mRecyclerViewEnCours = rootView.findViewById(R.id.recyclerViewEventListEnCours);
        mRecyclerViewBientot = rootView.findViewById(R.id.recyclerViewEventListBientot);
        mRecyclerViewFini = rootView.findViewById(R.id.recyclerViewEventListFini);

        initAdapter();


    }

    private void initAdapter(){
        Log.w("creation fragment", "Initialisation");
        adapterEvents(StatusEvent.ENCOURS, mRecyclerViewEnCours);
        adapterEvents(StatusEvent.BIENTOT, mRecyclerViewBientot);
        adapterEvents(StatusEvent.FINI, mRecyclerViewFini);

    }
    private void adapterEvents(final StatusEvent se, RecyclerView recyclerView) {

        Query query = mStoreBase.collection("events").whereNotEqualTo("ownerDoc",USER_REFERENCE.getId())
                .whereEqualTo("visibility",Visibility.PUBLIC)
                .whereEqualTo("statusEvent", se);
        FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

        mAdapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_list_, parent, false);
                return new EventViewHolder(view);
            }

                                    @Override
                                    protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                                        //holder.mImageView.setImageResource(e.getImage());
                                        Log.w("creation fragment", "Adapter");

                                            holder.mTitle.setText((e.getName()));
                                            holder.mDate.setText((dateFormat.format(e.getStartDate().toDate())));
                                            holder.mTime.setText((timeFormat.format(e.getStartDate().toDate())));
                                            holder.mVille.setText((e.getCity()));
                                            //holder.mDescription.setText((e.getDescription()));
                                            holder.mProprietaire.setText(e.getOwner());
                                            holder.textViewNbMembers.setText(e.getNbMembers()+"");


                                            mStoreBase.collection("users").whereEqualTo("uid", e.getOwnerDoc().getId())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    User u = document.toObject(User.class);
                                                                    if (u.getCursus().equalsIgnoreCase("Informatique")) {
                                                                        holder.mCarwViewOwner.setCardBackgroundColor(Color.RED);
                                                                    }

                                                                    if (u.getCursus().equalsIgnoreCase("TC")) {
                                                                        holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                                                                    }

                                                                    if (u.getCursus().equalsIgnoreCase("MMI")) {
                                                                        holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                                                                    }

                                                                    if (u.getCursus().equalsIgnoreCase("GB")) {
                                                                        holder.mCarwViewOwner.setCardBackgroundColor(Color.parseColor("#41EC57"));
                                                                    }

                                                                    if (u.getCursus().equalsIgnoreCase("LP")) {
                                                                        holder.mCarwViewOwner.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                                                    }

                                                                    Constants.setUserImage(u, getContext(), holder.mImageView);
                                                                }

                                                            }
                                                        }
                                                    });

                                                holder.mAfficher.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        InfosEvenementsActivity.EVENT_LOAD = e;
                                                        //InfosEvenementsActivity.type = "nouveau";
                                                        navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
                                                    }
                                                });




                                            if (se == StatusEvent.BIENTOT){
                                                sansBientot.setVisibility(View.GONE);
                                            }
                                            if (se == StatusEvent.ENCOURS){
                                                sansEnCours.setVisibility(View.GONE);

                                            }
                                            if (se == StatusEvent.FINI){
                                                sansFini.setVisibility(View.GONE);

                                            }

//                                            loading.setVisibility(View.GONE);
                                        }
                                };
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter != null){
            mAdapter.stopListening();
            mAdapter = null;
        }
    }
}