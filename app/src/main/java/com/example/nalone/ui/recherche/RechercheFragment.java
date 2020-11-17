package com.example.nalone.ui.recherche;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import splash.SplashActivity;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.USER_LATLNG;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.USER_STORAGE_REF;
import static com.example.nalone.util.Constants.currentUser;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.load;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheFragment extends Fragment implements CoreListener {

    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;
    private NavController navController;
    private RecyclerView mRecyclerView;

    private TextView resultat;
    private Context context;

    private List<ItemPerson> items = new ArrayList<>();
    private View rootView;
    private ProgressBar loading;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mRecyclerViewFiltre;
    private ItemFiltreAdapter mAdapterFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;
    private FirestoreRecyclerAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listeners.add(this);
        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);
        loading = rootView.findViewById(R.id.search_loading);

        search_bar = rootView.findViewById(R.id.search_bar);
        resultat = rootView.findViewById(R.id.resultatText);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        //query
        Query query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid());
        //RecyclerOption
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapterUsers(options);

        addFilters();

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar.onActionViewExpanded();
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        return rootView;

    }

    private void addFilters() {

        final List<ItemFiltre> filtres = new ArrayList<>();

        filtres.add(new ItemFiltre("TC"));
        filtres.add(new ItemFiltre("MMI"));
        filtres.add(new ItemFiltre("INFO"));
        filtres.add(new ItemFiltre("GB"));
        filtres.add(new ItemFiltre("LP"));

        mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);

        mAdapterFiltre.setOnItemClickListener(new ItemFiltreAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                Log.w("filtre", "click : "+filtres.get(position).getFiltre());

                if(filtres.get(position).getBackground() == R.color.colorPrimary){
                    filtres.get(position).setBackground(R.drawable.custom_input);
                    adapterFiltre("none");
                }
                else{
                    filtres.get(position).setBackground(R.color.colorPrimary);
                    adapterFiltre(filtres.get(position).getFiltre());
                }
                mAdapterFiltre.notifyDataSetChanged();
                adapter.startListening();
            }
            // adapterUsers(new FirestoreRecyclerOptions.Builder<User>().setQuery(queryFiltreLP, User.class).build());
        });
    }

    public void adapterFiltre(String filtre){
        Query query = null;
        switch (filtre){
            case "INFO" :
                query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid()).whereEqualTo("cursus", "Informatique");
                break;
            case "GB" :
                query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid()).whereEqualTo("cursus", "GB");
                break;
            case "MMI" :
                query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid()).whereEqualTo("cursus", "MMI");
                break;
            case "TC" :
                query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid()).whereEqualTo("cursus", "TC");
                break;
            case "LP" :
                query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid()).whereEqualTo("cursus", "LP");
                break;
            case "none" :
                query = firebaseFirestore.collection("users").whereNotEqualTo("uid", USER.getUid());
                break;
            default:
                break;
        }

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapterUsers(options);
    }

    private void adapterUsers(FirestoreRecyclerOptions<User> options) {
        adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                userViewHolder.villePers.setText(u.getCity());
                userViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
                userViewHolder.button.setImageResource(0);

                userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUpProfil(u);
                    }
                });

                /*getUserData(u.getUid(), new FireStoreUsersListeners() {
                    @Override
                    public void onDataUpdate(final User u) {
                        if (u.getImage_url() != null) {
                            if(!Cache.fileExists(u.getUid())) {
                                StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                                if (imgRef != null) {
                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri img = task.getResult();
                                                if (img != null) {
                                                    Log.w("image", "save image from cache");
                                                    Cache.saveUriFile(u.getUid(), img);
                                                    Glide.with(context).load(img).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                                }
                                            }
                                        }
                                    });
                                }
                            }else{
                                Log.w("image", "get image from cache");
                                Glide.with(context).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                            }
                        }
                    }
                });*/

            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        loading.setVisibility(View.GONE);
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.imageView19);

        }

    }


    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
       /* if(USER.get_friends_requests_send().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_hourglass_top_24;
       }else if(USER.get_friends_requests_received().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_mail_24;
        }else{*/
            PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;
       // }

        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

   /* public void updateItems() {
        final boolean[] duplicate = {true};
        items.clear();

        mStoreBase.collection("users")
                .whereGreaterThan("number", "0")
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User USER_LOAD = document.toObject(User.class);
                                    if(!USER_LOAD.getUid().equalsIgnoreCase(USER.getUid())) {
                                       if (!USER_LOAD.get_friends().contains(USER_REFERENCE)) {
                                            ItemPerson it;
                                            if (USER.get_friends_requests_send().contains(mStoreBase.collection("users").document(USER_LOAD.getUid()))) {
                                                it = new ItemPerson(USER_LOAD.getUid(), R.drawable.ic_baseline_account_circle_24, USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name(), R.drawable.ic_round_hourglass_top_24, USER_LOAD.getDescription(), USER_LOAD.getCity(), USER_LOAD.getCursus(), USER_LOAD.get_number_events_create(), USER_LOAD.get_number_events_attend(), USER_LOAD.getCenters_interests());
                                           } else if (USER.get_friends_requests_received().contains(mStoreBase.collection("users").document(USER_LOAD.getUid()))) {
                                                it = new ItemPerson(USER_LOAD.getUid(), R.drawable.ic_baseline_account_circle_24, USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name(), R.drawable.ic_round_mail_24, USER_LOAD.getDescription(), USER_LOAD.getCity(), USER_LOAD.getCursus(), USER_LOAD.get_number_events_create(), USER_LOAD.get_number_events_attend(), USER_LOAD.getCenters_interests());
                                            } else {
                                                it = new ItemPerson(USER_LOAD.getUid(), R.drawable.ic_baseline_account_circle_24, USER_LOAD.getFirst_name() + " " + USER_LOAD.getLast_name(), 0, USER_LOAD.getDescription(), USER_LOAD.getCity(), USER_LOAD.getCursus(), USER_LOAD.get_number_events_create(), USER_LOAD.get_number_events_attend(), USER_LOAD.getCenters_interests());
                                            }

                                            for(ItemPerson i : items) {
                                                if (i.getUid().equalsIgnoreCase(it.getUid())) {
                                                    duplicate[0] = true;
                                                    break;
                                                }
                                            }

                                            if(!items.contains(it)) {
                                                items.add(it);
                                                onUpdateAdapter();
                                            }


                                        }
                                    }
                                }

                            }else{
                                Log.w("Recherche", "Résultats vide");
                            }
                        } else {
                            Log.d("SPLASH", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("SPLASH", "Erreur : " + e.getMessage());
            }
        });

        /*for(int i = 0; i < USERS_LIST.size(); i++){
            User u = USERS_LIST.get(i+"");
            if(u != null) {
                if (!USER_ID.equalsIgnoreCase(i + "")) {
                    if (!USERS_LIST.get(USER_ID).getAmis().contains(i+"")) {
                        ItemPerson it;
                        if (USERS_LIST.get(USER_ID).getDemande_amis_envoye().contains(i+"")) {
                            it = new ItemPerson(i, R.drawable.ic_baseline_account_circle_24, u.getPrenom() + " " + u.getNom(), R.drawable.ic_round_hourglass_top_24, u.getDescription(), u.getVille(), u.getCursus(), u.getNbCreation(), u.getNbParticipation(), u.getCentreInterets());
                        } else if (USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(i+"")) {
                            it = new ItemPerson(i, R.drawable.ic_baseline_account_circle_24, u.getPrenom() + " " + u.getNom(), R.drawable.ic_round_mail_24, u.getDescription(), u.getVille(), u.getCursus(), u.getNbCreation(),u.getNbParticipation(), u.getCentreInterets());
                        } else {
                            it = new ItemPerson(i, R.drawable.ic_baseline_account_circle_24, u.getPrenom() + " " + u.getNom(), 0, u.getDescription(), u.getVille(), u.getCursus(), u.getNbCreation(),u.getNbParticipation(), u.getCentreInterets());
                        }

                        items.add(it);
                    }
                }
            }
        }


    }*/

    @Override
    public void onDataChangeListener() {
       // updateItems();
        adapter.startListening();
    }

  //  @Override
    public void onUpdateAdapter() {
        loading.setVisibility(View.GONE);
        if(items.size() == 0){
            resultat.setVisibility(View.VISIBLE);
            resultat.setText("Aucun amis à ajouter !");
        }else{
            resultat.setVisibility(View.GONE);
            resultat.setText("");
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        //updateItems();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}