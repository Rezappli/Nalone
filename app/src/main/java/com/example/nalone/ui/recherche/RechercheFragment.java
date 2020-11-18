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
import com.example.nalone.UserFriendData;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
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
    List<String> friends;



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

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        friends = new ArrayList<>();

        mStoreBase.collection("users").document(USER.getUid()).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                friends.add(document.getId());
                            }
                            friends.add(USER.getUid());
                            //query
                            Query query = mStoreBase.collection("users").whereNotIn("uid", friends);
                            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                            adapterUsers(options);
                            adapter.startListening();
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

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
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
                    adapterFiltre("none", friends);
                }
                else{
                    filtres.get(position).setBackground(R.color.colorPrimary);
                    adapterFiltre(filtres.get(position).getFiltre(), friends);
                }
                mAdapterFiltre.notifyDataSetChanged();
                adapter.startListening();
            }
            // adapterUsers(new FirestoreRecyclerOptions.Builder<User>().setQuery(queryFiltreLP, User.class).build());
        });
    }

    public void adapterFiltre(String filtre, List friends){
        Query query = null;
        switch (filtre){
            case "INFO" :
                query = mStoreBase.collection("users").whereNotIn("uid", friends).whereEqualTo("cursus", "Informatique").limit(10);
                break;
            case "GB" :
                query = mStoreBase.collection("users").whereNotIn("uid", friends).whereEqualTo("cursus", "GB").limit(10);
                break;
            case "MMI" :
                query = mStoreBase.collection("users").whereNotIn("uid", friends).whereEqualTo("cursus", "MMI").limit(10);
                break;
            case "TC" :
                query = mStoreBase.collection("users").whereNotIn("uid", friends).whereEqualTo("cursus", "TC").limit(10);
                break;
            case "LP" :
                query = mStoreBase.collection("users").whereNotIn("uid", friends).whereEqualTo("cursus", "LP").limit(10);
                break;
            case "none" :
                query = mStoreBase.collection("users").whereNotIn("uid", friends).limit(10);
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

                if(u.getImage_url() != null) {
                    if (!Cache.fileExists(u.getUid())) {
                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                        if (imgRef != null) {
                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri img = task.getResult();
                                        if (img != null) {
                                            Cache.saveUriFile(u.getUid(), img);
                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Glide.with(getContext()).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                    }
                }

                userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUpProfil(u);
                    }
                });
                loading.setVisibility(View.GONE);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);

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

    @Override
    public void onDataChangeListener() {
        // updateItems();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}