package com.example.nalone.ui.amis.display;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Adapter.ItemListAmisAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USERS_PICTURE_URI;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.heightScreen;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mProfilRef;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.widthScreen;

public class AmisFragment extends Fragment implements CoreListener{

    private SearchView search_bar;
    private RecyclerView mRecyclerView;
    private NavController navController;
    private ItemListAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView resultat;
    private final List<ItemPerson> tempList = new ArrayList<>();
    private final List<ItemPerson> items = new ArrayList<>();
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

        updateItems();



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

    private void removeFriend(int id) {
        if(USERS_LIST.get(USER_ID).getAmis().size() == 1) {
            USERS_LIST.get(USER_ID).getAmis().set(0, "");
        }else{
            USERS_LIST.get(USER_ID).getAmis().remove(id+"");
        }

        if(USERS_LIST.get(id+"").getAmis().size() == 1) {
            USERS_LIST.get(id+"").getAmis().set(0, "");
        }else{
            USERS_LIST.get(id+"").getAmis().remove(USER_ID);
        }

        USERS_DB_REF.setValue(USERS_LIST);

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();

        updateItems();

    }



    private void updateItems() {
        items.clear();
        for (int i = 0; i < USERS_LIST.get(USER_ID).getAmis().size(); i++) {
            User u = USERS_LIST.get(USERS_LIST.get(USER_ID).getAmis().get(i));
            if(u != null) {
                int id = Integer.parseInt(USERS_LIST.get(USER_ID).getAmis().get(i));
                items.add(new ItemPerson(id, R.drawable.ic_baseline_account_circle_24,
                        u.getPrenom() + " " + u.getNom(), 0, u.getDescription(),
                        u.getVille(), u.getCursus(), u.getNbCreation(), u.getNbParticipation(), u.getCentreInterets()));
            }
        }

        mAdapter = new ItemListAmisAdapter(items, getContext());

        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemListAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                showPopUpProfil(items.get(position).getId(), items.get(position).getNom(),items.get(position).getVille(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), items.get(position).getCentresInterets());
            }

            @Override
            public void onDelete(int position) {
                removeFriend(items.get(position).getId());
            }
        });

    }

    public void showPopUpProfil(int id, String name,String ville, String desc, String nbCreate, String nbParticipate, List<String> centresInteret) {
        PopupProfilFragment.id = id;
        PopupProfilFragment.name = name;
        PopupProfilFragment.ville = ville;
        PopupProfilFragment.desc = desc;
        PopupProfilFragment.nbCreate = nbCreate;
        PopupProfilFragment.nbParticipate = nbParticipate;
        PopupProfilFragment.centresInteret = centresInteret;

        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
    }

    @Override
    public void onResume(){
        updateItems();
        super.onResume();
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }
}