package com.example.nalone.ui.recherche;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheAmisFragment extends Fragment {

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
    private ImageView qr_code;
    List<String> friends;

    private SwipeRefreshLayout swipeContainer;

    private LinearLayout linearSansRechercheAmis;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_recherche_amis, container, false);

        createFragment();

        return rootView;

    }

    public void createFragment(){
        loading = rootView.findViewById(R.id.search_loading);
        buttonBack.setVisibility(View.GONE);
        linearSansRechercheAmis = rootView.findViewById(R.id.linearSansRechercheGroupe);
        swipeContainer = rootView.findViewById(R.id.AmisSwipeRefreshLayout);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        this.configureSwipeRefreshLayout();

        qr_code = rootView.findViewById(R.id.qr_code_image);

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_amis_to_navigation_camera2);
            }
        });

        search_bar = rootView.findViewById(R.id.search_bar);
        resultat = rootView.findViewById(R.id.resultatText);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        friends = new ArrayList<>();

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status","add")
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
                            addFilters();


                            adapter.startListening();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

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

    }

    private void configureSwipeRefreshLayout(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });
    }


    /*private void updateUI(){
        // 3 - Stop refreshing and clear actual list of users

        adapter.notifyDataSetChanged();
        createFragment();
    }*/

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
                for (int i = 0; i < filtres.size(); i++){
                    if(i != position)
                        filtres.get(i).setBackground(R.drawable.custom_input);
                }

                if(filtres.get(position).getBackground() == R.color.colorPrimary){
                    filtres.get(position).setBackground(R.drawable.custom_input);
                    adapterFiltre("none", friends);
                }
                else{
                    filtres.get(position).setBackground(R.color.colorPrimary);
                    for (int i = 0; i < friends.size(); i++){
                        Log.w("filtres", "Friends : " + friends.get(i));
                    }
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
                Log.w("Add", "ViewHolder Recherche");
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                userViewHolder.villePers.setText(u.getCity());
                userViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());

                userViewHolder.button.setImageResource(0);
                Log.w("Add", "BienHolder Recherche");

                Constants.setUserImage(u, getContext(), userViewHolder.imagePerson);

                userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUpProfil(u);
                    }
                });
                loading.setVisibility(View.GONE);
                linearSansRechercheAmis.setVisibility(View.GONE);
            }
        };
        Log.w("Add", "Set Adapter Recherche");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
        swipeContainer.setRefreshing(false);
        loading.setVisibility(View.GONE);

    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);

        }

    }


    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;

        PopupProfilFragment.type = "recherche";
        navController.navigate(R.id.action_navigation_recherche_amis_to_navigation_popup_profil);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
            adapter = null;
        }
    }

    @Override
    public void onResume(){
        if(adapter != null) {
            adapter.startListening();
        }else{
            createFragment();
        }
        super.onResume();
    }


}