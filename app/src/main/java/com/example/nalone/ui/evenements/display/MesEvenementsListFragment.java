package com.example.nalone.ui.evenements.display;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.Evenement;
import com.example.nalone.Horloge;
import com.example.nalone.R;
import com.example.nalone.StatusEvent;
import com.example.nalone.User;
import com.example.nalone.Visibility;
import com.example.nalone.ui.evenements.EvenementsFragment;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.load;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.timeFormat;


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
    private CardView addEvent;
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
        addEvent = rootView.findViewById(R.id.addEvent);
        cardViewInvits = rootView.findViewById(R.id.cardViewInvits);
        textViewNbInvit = rootView.findViewById(R.id.nbInvits);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_evenements_to_navigation_create_event);
            }
        });

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

        initAdapter();

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

                                        holder.mTitle.setText((e.getName()));
                                        holder.mDate.setText((dateFormat.format(e.getDate().toDate())));
                                        holder.mTime.setText((timeFormat.format(e.getDate().toDate())));
                                        holder.mVille.setText((e.getCity()));
                                        //holder.mDescription.setText((e.getDescription()));
                                        holder.mProprietaire.setText(e.getOwner());
                                        holder.textViewNbMembers.setText(e.getNbMembers() + "");


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

   /* private void adapterEvents() {
        DocumentReference ref = mStoreBase.document("users/"+USER_ID);

        mStoreBase.collection("users").document(USER_ID).collection("events").whereNotEqualTo("user", ref)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        linearSansEvent.setVisibility(View.VISIBLE);
                    }
                });

        mStoreBase.collection("users").document(USER_ID).collection("events").whereNotEqualTo("user", ref).whereEqualTo("status", "add")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        events = new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                events.add(document.getId());
                                Log.w("event", " Ajout list");
                                iterator++;
                            }
                            Log.w("event", " Check list");
                            if(!events.isEmpty()) {
                                Query query = mStoreBase.collection("events").whereIn("uid", events);

                                FirestoreRecyclerOptions<Evenement> options = new FirestoreRecyclerOptions.Builder<Evenement>().setQuery(query, Evenement.class).build();

                                adapter = new FirestoreRecyclerAdapter<Evenement, EventViewHolder>(options) {
                                    @NonNull
                                    @Override
                                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evenements_join, parent, false);
                                        return new EventViewHolder(view);
                                    }

                                    @Override
                                    protected void onBindViewHolder(@NonNull final EventViewHolder holder, int i, @NonNull final Evenement e) {
                                        //holder.mImageView.setImageResource(e.getImage());
                                        holder.mTitle.setText((e.getName()));
                                        holder.mDate.setText((dateFormat.format(e.getDate().toDate())));
                                        holder.mTime.setText((timeFormat.format(e.getDate().toDate())));
                                        holder.mVille.setText((e.getCity()));
                                        holder.mProprietaire.setText(e.getOwner());
                                        holder.textViewNbMembers.setText(e.getNbMembers() + "");

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

                                                            }

                                                        }
                                                    }
                                                });


                                        holder.mAfficher.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //Constants.targetZoom = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude());
                                                //EvenementsFragment.viewPager.setCurrentItem(0);
                                                InfosEvenementsActivity.EVENT_LOAD = e;
                                                InfosEvenementsActivity.type = "nouveau";
                                                navController.navigate(R.id.action_navigation_evenements_to_navigation_infos_events);
                                            }
                                        });

                                        holder.mInscrire.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                e.setNbMembers(e.getNbMembers()-1);
                                                mStoreBase.collection("events").document(e.getUid()).set(e);
                                                mStoreBase.collection("events").document(e.getUid()).collection("members").document(USER_ID).delete();
                                                mStoreBase.collection("users").document(USER_ID).collection("events").document(e.getUid()).delete();
                                                Toast.makeText(getContext(), "Vous vous êtes désinscrit de l'évènement " + e.getName() + " !", Toast.LENGTH_SHORT).show();
                                                createFragment();
                                                //EvenementsFragment.viewPager.setCurrentItem(2);

                                            }
                                        });


                                        if(eventTermine(new Date(),e.getDate().toDate())){
                                            //holder.linearTermine.setVisibility(View.VISIBLE);
                                            holder.mInscrire.setText("Supprimer");
                                            holder.cardViewTermine.setVisibility(View.VISIBLE);
                                            holder.mInscrire.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mStoreBase.collection("users")
                                                            .document(USER_ID).collection("events")
                                                            .document(e.getUid()).delete();
                                                   // createFragment();
                                                   // EvenementsFragment.viewPager.setCurrentItem(2);
                                                   reload();
                                                }
                                            });

                                            //createFragment();
                                        }

               /* if(e.getImage_url() != null) {
                    if(!Cache.fileExists(e.getUid())) {
                        StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(g.getUid(), img);
                                            g.setImage_url(Cache.getImageDate(g.getUid()));
                                            mStoreBase.collection("groups").document(g.getUid()).set(g);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Uri imgCache = Cache.getUriFromUid(g.getUid());
                        if(Cache.getImageDate(g.getUid()).equalsIgnoreCase(g.getImage_url())) {
                            Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                        }else{
                            StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(g.getUid(), img);
                                                g.setImage_url(Cache.getImageDate(g.getUid()));
                                                mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                */
                                /*    }
                                };
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                mRecyclerView.setAdapter(adapter);
                                adapter.startListening();
                                swipeContainer.setRefreshing(false);

                                Log.w("count", iterator + "");
                            }else{
                                linearSansEvent.setVisibility(View.VISIBLE);
                            }
                        }else{
                            linearSansEvent.setVisibility(View.VISIBLE);
                        }
                    }



                });




    }*/

    public boolean eventTermine(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date","startDate : " + startDate);
        Log.w("date","endDate : "+ endDate);
        Log.w("date","different : " + different);

        if(different < 0){
            return true;
        }else{
            return false;
        }
    }


   /* private class EventViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView mVille;
        public TextView mProprietaire;
        public Button mInscrire, mAfficher;
        public CardView mCarwViewOwner;
        private TextView textViewNbMembers;
        private LinearLayout linearNonTermine;
        private LinearLayout linearTermine;
        private CardView cardViewTermine;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOwnerEventList);
            mTitle = itemView.findViewById(R.id.titleEventList);
            mDate = itemView.findViewById(R.id.dateEventList);
            mTime = itemView.findViewById(R.id.timeEventList);
            mVille = itemView.findViewById(R.id.villeEventList);
            mProprietaire = itemView.findViewById(R.id.ownerEventList);
            mAfficher = itemView.findViewById(R.id.buttonAfficherEventList);
            mInscrire = itemView.findViewById(R.id.buttonInscrirEventList);
            mCarwViewOwner = itemView.findViewById(R.id.backGroundOwner);
            textViewNbMembers = itemView.findViewById(R.id.textViewNbMembers);
            cardViewTermine = itemView.findViewById(R.id.cardViewEnd);
        }
    }*/

    @Override
    public void onResume() {
        createFragment();
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