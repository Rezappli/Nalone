package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalone.adapter.ItemListAmisAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.listeners.FireStoreUsersListeners;

import java.util.ArrayList;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.nolonelyBundle;
import static com.example.nalone.util.Constants.updateUserData;

public class AmisFragment extends Fragment implements CoreListener{

    private SearchView search_bar;
    private RecyclerView mRecyclerView;
    private NavController navController;
    private ItemListAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    private final ArrayList<ItemPerson> tempList = new ArrayList<>();
    private ArrayList<ItemPerson> items;
    private static String message = "null";
    private View rootView;

    public AmisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listeners.add(this);
        rootView = inflater.inflate(R.layout.amis_fragment, container, false);
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

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

    private void removeFriend(final String uid, final int position) {
        getUserData(uid, new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                USER.get_friends().remove(mStoreBase.collection("users").document(uid));
                u.get_friends().remove(USER_REFERENCE);

                updateUserData(u);
                updateUserData(USER);
            }
        });

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();
    }



    private void updateItems() {
        items = new ArrayList<>();
        if(USER.get_friends().size() > 0) {
            Log.w("amis", "User friend size : " + USER.get_friends().size());
            for (int i = 0; i < USER.get_friends().size(); i++) {
                Log.w("amis", "start process for a friends");

                final int finalI = i;
                getUserData(USER.get_friends().get(i).getId(), new FireStoreUsersListeners() {
                    @Override
                    public void onDataUpdate(User u) {
                        Log.w("Invitations", "Chargement de : "+USER.get_friends().get(finalI).getId());
                        ItemPerson it = new ItemPerson(u.getUid(), R.drawable.ic_baseline_account_circle_24,
                                u.getFirst_name() + " " + u.getLast_name(), 0, u.getDescription(),
                                u.getCity(), u.getCursus(), u.get_number_events_create(), u.get_number_events_attend(), u.getCenters_interests());
                        items.add(it);
                        nolonelyBundle.putSerializable("friends", items);
                        onUpdateAdapter();
                    }
                });
            }
        }else{
            onUpdateAdapter();
        }
    }

    public void showPopUpProfil(final String uid) {
        getUserData(uid,new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                PopupProfilFragment.USER_LOAD = u;
                PopupProfilFragment.button = 0;
                navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
            }
        });
    }

    @Override
    public void onDataChangeListener() {
        Log.w("Amis", "Update data on firestore");
        updateItems();
    }

    @Override
    public void onUpdateAdapter() {
        Log.w("Amis", "Update data on firestore");
        mAdapter = new ItemListAmisAdapter(items, getContext());

        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemListAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                showPopUpProfil(items.get(position).getUid());
            }

            @Override
            public void onDelete(int position) {
                removeFriend(items.get(position).getUid(), position);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(nolonelyBundle.getSerializable("friends") != null){
            items = (ArrayList<ItemPerson>) nolonelyBundle.getSerializable("friends");
            onUpdateAdapter();
        }else{
            updateItems();
        }
    }

}