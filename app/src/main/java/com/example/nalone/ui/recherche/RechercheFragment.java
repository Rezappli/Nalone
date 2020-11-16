package com.example.nalone.ui.recherche;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.example.nalone.UserFriendData;
import com.example.nalone.adapter.FirestoreRecyclerAdapterUserFriendData;
import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.ui.message.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import splash.SplashActivity;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheFragment extends Fragment implements CoreListener {


    private RechercheViewModel rechercheViewModel;
    private SearchView search_bar;
    private NavController navController;
    private RecyclerView mRecyclerView;
    private ItemProfilAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    private RecyclerView mRecyclerViewFiltre;
    private ItemFiltreAdapter mAdapterFiltre;
    private RecyclerView.LayoutManager mLayoutManagerFiltre;
    private final List<ItemFiltre> filtres = new ArrayList<>();
    private View rootView;
    private ProgressBar loading;


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
        /*if(USER.get_friends_requests_send().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_hourglass_top_24;
        }else if(USER.get_friends_requests_received().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_mail_24;
        }else{
            PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;
        }*/

        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    public void updateItems() {
        CollectionReference usersRef = mStoreBase.collection("users").document(USER.getUid())
                .collection("friends");

        Query query = usersRef.orderBy("add_time");

        FirestoreRecyclerOptions<UserFriendData> options = new FirestoreRecyclerOptions.Builder<UserFriendData>().setQuery(query, UserFriendData.class).build();
        adapter = new FirestoreRecyclerAdapterUserFriendData(options);
        Log.w("Doc", "adapter : " + adapter.getItemCount());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FirestoreRecyclerAdapterUserFriendData.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {
                //ChatActivity.USER_LOAD =
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });


    }


    @Override
    public void onDataChangeListener() {
        updateItems();
    }

    @Override
    public void onStart(){
        super.onStart();
        updateItems();
    }
}