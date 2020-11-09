package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nalone.Adapter.ItemInvitAmisAdapter;
import com.example.nalone.CoreListener;
import com.example.nalone.CustomToast;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.listeners;

public class MesInvitationsFragment extends Fragment implements CoreListener {

    private RecyclerView mRecyclerView;
    private ItemInvitAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final List<ItemPerson> invits = new ArrayList<>();
    private View rootView;


    public MesInvitationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listeners.add(this);
       rootView = inflater.inflate(R.layout.fragment_mes_invitations, container, false);

        updateItems();

        return rootView;
    }

    private void updateItems() {
        invits.clear();
        for(int i = 0; i < USERS_LIST.size(); i++){
            User u = USERS_LIST.get(i+"");
            if(u != null) {
                if (!USER_ID.equalsIgnoreCase(i + "")) {
                    if(USERS_LIST.get(USER_ID).getDemande_amis_recu().contains(i+"")) {
                        invits.add(new ItemPerson(i, R.drawable.ic_baseline_account_circle_24,
                                u.getPrenom() + " " + u.getNom(), 0, u.getDescription(),
                                u.getVille(), u.getCursus(), u.getNbCreation(), u.getNbParticipation(), u.getCentreInterets()));
                    }
                }
            }
        }

        mAdapter = new ItemInvitAmisAdapter(invits, getContext());

        mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitAmis);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemInvitAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                acceptFriendRequest(invits.get(position).getId());
            }

            @Override
            public void onRemoveClick(int position) {
                declineFriendRequest(invits.get(position).getId());
            }
        });
    }

    private void acceptFriendRequest(int id) {
        Log.w("Invits", "ID : "+id);
        if(USERS_LIST.get(USER_ID).getAmis().size() == 1) {
            USERS_LIST.get(USER_ID).getAmis().set(0, id+"");
        }else{
            USERS_LIST.get(USER_ID).getAmis().add(id+"");
        }

        if(USERS_LIST.get(id+"").getAmis().size() == 1) {
            USERS_LIST.get(id+"").getAmis().set(0, USER_ID);
        }else{
            USERS_LIST.get(id+"").getAmis().add(USER_ID);
        }

        if(USERS_LIST.get(USER_ID).getDemande_amis_envoye().size() == 1) {
            USERS_LIST.get(USER_ID).getDemande_amis_envoye().set(0, "");
        }else{
            USERS_LIST.get(USER_ID).getDemande_amis_envoye().remove(id+"");
        }

        if(USERS_LIST.get(USER_ID).getDemande_amis_recu().size() == 1) {
            USERS_LIST.get(USER_ID).getDemande_amis_recu().set(0, "");
        }else{
            USERS_LIST.get(USER_ID).getDemande_amis_recu().remove(id+"");
        }

        if(USERS_LIST.get(id+"").getDemande_amis_envoye().size() == 1) {
            USERS_LIST.get(id+"").getDemande_amis_envoye().set(0, "");
        }else{
            USERS_LIST.get(id+"").getDemande_amis_envoye().remove(USER_ID);
        }

        if(USERS_LIST.get(id+"").getDemande_amis_recu().size() == 1) {
            USERS_LIST.get(id+"").getDemande_amis_recu().set(0, "");
        }else{
            USERS_LIST.get(id+"").getDemande_amis_recu().remove(USER_ID);
        }

        USERS_DB_REF.setValue(USERS_LIST);

        updateItems();
        CustomToast t = new CustomToast(getContext(), "Vous avez ajouté(e) cet utilisateur", false, true);
        t.show();
    }

    private void declineFriendRequest(int id) {

        if(USERS_LIST.get(USER_ID).getDemande_amis_envoye().size() == 1) {
            USERS_LIST.get(USER_ID).getDemande_amis_envoye().set(0, "");
        }else{
            USERS_LIST.get(USER_ID).getDemande_amis_envoye().remove(id);
        }

        if(USERS_LIST.get(USER_ID).getDemande_amis_recu().size() == 1) {
            USERS_LIST.get(USER_ID).getDemande_amis_recu().set(0, "");
        }else{
            USERS_LIST.get(USER_ID).getDemande_amis_recu().remove(id);
        }

        if(USERS_LIST.get(id+"").getDemande_amis_envoye().size() == 1) {
            USERS_LIST.get(id+"").getDemande_amis_envoye().set(0, "");
        }else{
            USERS_LIST.get(id+"").getDemande_amis_envoye().remove(Integer.parseInt(USER_ID));
        }

        if(USERS_LIST.get(id+"").getDemande_amis_recu().size() == 1) {
            USERS_LIST.get(id+"").getDemande_amis_recu().set(0, "");
        }else{
            USERS_LIST.get(id+"").getDemande_amis_recu().remove(USER_ID);
        }

        USERS_DB_REF.setValue(USERS_LIST);

        Toast.makeText(getContext(), "Vous n'avez pas accepté(e) cet utilisateur", Toast.LENGTH_SHORT).show();

        updateItems();

    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }
}