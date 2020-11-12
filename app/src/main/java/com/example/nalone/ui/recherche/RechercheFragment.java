package com.example.nalone.ui.recherche;

import android.os.Bundle;
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

import com.example.nalone.adapter.ItemFiltreAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemFiltre;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.amis.display.PopupProfilFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.listeners;

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
    private final List<ItemPerson> items = new ArrayList<>();
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


        updateItems();

        if(items.size() == 0){
            resultat.setVisibility(View.VISIBLE);
            resultat.setText("Aucun amis à ajouter !");
        }else{
            resultat.setVisibility(View.GONE);
            resultat.setText("");
        }

        return rootView;

    }


    public void showPopUpProfil(User u, String name,String ville, String desc, String nbCreate, String nbParticipate, final int button, List centresInteret) {

        PopupProfilFragment.USER_LOAD = u;

        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    public void updateItems() {
        items.clear();
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
        }*/

        mAdapter = new ItemProfilAdapter(items, getContext());
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);


        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new ItemProfilAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {

                /*if (USERS_LIST.get(USER_ID).getDemande_amis_envoye().contains(items.get(position).getId() + "")) {
                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(),items.get(position).getVille(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), R.drawable.ic_round_hourglass_top_24, items.get(position).getCentresInterets());
                } else if (USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(items.get(position).getId() + "")) {
                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(),items.get(position).getVille(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), R.drawable.ic_round_mail_24, items.get(position).getCentresInterets());
                } else {
                    showPopUpProfil(items.get(position).getId(), items.get(position).getNom(),items.get(position).getVille(), items.get(position).getmDescription(), items.get(position).getmNbCreate(), items.get(position).getmNbParticipate(), R.drawable.ic_baseline_add_circle_outline_24, items.get(position).getCentresInterets());
                }*/
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }

    @Override
    public void onUpdateAdapter() {

    }

    @Override
    public void onResume(){
        updateItems();
        super.onResume();
    }
}