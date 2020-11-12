package com.example.nalone.ui.recherche;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private ItemProfilAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    private final List<ItemPerson> tempList = new ArrayList<>();
    private RecyclerView mRecyclerViewFiltre;
    private ItemFiltreAdapter mAdapterFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;
    private List<ItemPerson> items = null;
    private final List<ItemFiltre> filtres = new ArrayList<>();
    private View rootView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listeners.add(this);
        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);

        search_bar = rootView.findViewById(R.id.search_bar);
        resultat = rootView.findViewById(R.id.resultatText);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        filtres.add(new ItemFiltre("TC"));
        filtres.add(new ItemFiltre("MMI"));
        filtres.add(new ItemFiltre("INFO"));
        filtres.add(new ItemFiltre("GB"));

        mAdapterFiltre = new ItemFiltreAdapter(filtres);

        mRecyclerViewFiltre = rootView.findViewById(R.id.recyclerViewFiltre);
        mLayoutManagerFiltre = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewFiltre.setLayoutManager(mLayoutManagerFiltre);
        mRecyclerViewFiltre.setAdapter(mAdapterFiltre);

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


    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        if(USER.get_friends_requests_send().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_hourglass_top_24;
        }else if(USER.get_friends_requests_received().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_mail_24;
        }else{
            PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;
        }


        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    public void updateItems() {
        items = new ArrayList<>();

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

                                            items.add(it);
                                        }
                                    }
                                }
                                onUpdateAdapter();
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

    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }

    @Override
    public void onUpdateAdapter() {
        if(items.size() == 0){
            resultat.setVisibility(View.VISIBLE);
            resultat.setText("Aucun amis à ajouter !");
        }else{
            resultat.setVisibility(View.GONE);
            resultat.setText("");
        }

        mAdapter = new ItemProfilAdapter(items, getContext());
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);


        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                    getUserData(items.get(position).getUid(), new FireStoreUsersListeners() {
                        @Override
                        public void onDataUpdate(User u) {
                            showPopUpProfil(u);
                        }
                    });
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        updateItems();
        super.onResume();
    }
}