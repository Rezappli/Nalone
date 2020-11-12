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
    private ArrayList<ItemPerson> items = new ArrayList<>();
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
                newText = newText.toLowerCase();
                tempList.clear();
                boolean check = true;
                if (items.size() > 0) {
                    if (newText.length() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            for (int j = 0; j < newText.length(); j++) {
                                if (items.get(i).getmNomToLowerCase().length() > j) {
                                    if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && j == 0) {
                                        check = true;
                                    }


                                    if (newText.charAt(j) == items.get(i).getmNomToLowerCase().charAt(j) && check) {
                                        check = true;
                                    } else {
                                        check = false;
                                    }


                                    if (check) {
                                        if (!tempList.contains(items.get(i))) {
                                            tempList.add(items.get(i));
                                            if (resultat.getVisibility() == View.VISIBLE) {
                                                resultat.setVisibility(View.GONE);
                                                resultat.setText("");
                                            }
                                        }
                                    } else {
                                        tempList.remove(items.get(i));
                                    }
                                } else {
                                    tempList.remove(items.get(i));
                                }
                            }
                        }


                        if (tempList.size() == 0) {
                            resultat.setVisibility(View.VISIBLE);
                            resultat.setText(R.string.aucun_resultat);
                        }

                        mAdapter = new ItemListAmisAdapter(tempList, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        resultat.setVisibility(View.GONE);
                        resultat.setText("");
                        mAdapter = new ItemListAmisAdapter(items, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setText("Aucun amis à ajouter !");
                }

                return false;
            }
        });

        return rootView;
    }

    private void removeFriend(final String uid) {
        getUserData(uid, new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                USER.get_friends().remove(mStoreBase.collection("users").document(uid));
                u.get_friends().remove(USER_REFERENCE);

                updateUserData(USER);
                updateUserData(u);
            }
        });

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();
    }



    private void updateItems() {
        items.clear();
        for(int i = 0; i < USER.get_friends().size(); i++){
            Log.w("amis", "start process for a friends");

            getUserData(USER.get_friends().get(i).getId(), new FireStoreUsersListeners() {
                @Override
                public void onDataUpdate(User u) {
                    ItemPerson it = new ItemPerson(u.getUid(), R.drawable.ic_baseline_account_circle_24,
                            u.getFirst_name() + " " + u.getLast_name(), 0, u.getDescription(),
                            u.getCity(), u.getCursus(), u.get_number_events_create(), u.get_number_events_attend(), u.getCenters_interests());
                    items.add(it);
                    onUpdateAdapter();
                }
            });
        }
    }

    public void showPopUpProfil(final String uid) {
        getUserData(uid,new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                PopupProfilFragment.USER_LOAD = u;
                navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
            }
        });
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }

    @Override
    public void onUpdateAdapter() {
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
                removeFriend(items.get(position).getUid());
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(nolonelyBundle.getSerializable("items") == null) {
            nolonelyBundle.putSerializable("items", items);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
            if (nolonelyBundle.getSerializable("items") != null) {
                items = (ArrayList<ItemPerson>) nolonelyBundle.getSerializable("items");
                onUpdateAdapter();
                Log.w("SaveInstanceBundle", " Load : " + items);
            } else {
                updateItems();
            }
        }

}