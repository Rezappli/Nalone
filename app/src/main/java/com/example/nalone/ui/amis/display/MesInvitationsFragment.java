package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nalone.adapter.ItemInvitAmisAdapter;
import com.example.nalone.User;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.listeners.FireStoreUsersListeners;

import java.util.ArrayList;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.nolonelyBundle;
import static com.example.nalone.util.Constants.updateUserData;

public class MesInvitationsFragment extends Fragment implements CoreListener {

    private RecyclerView mRecyclerView;
    private ItemInvitAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemPerson> invits = new ArrayList<>();
    private View rootView;


    public MesInvitationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listeners.add(this);
       rootView = inflater.inflate(R.layout.fragment_mes_invitations, container, false);


        return rootView;
    }

    private void updateItems() {
        invits.clear();

        for(int i = 0; i < USER.get_friends_requests_received().size(); i++){
            getUserData(USER.get_friends_requests_received().get(i).getId(), new FireStoreUsersListeners() {
                @Override
                public void onDataUpdate(User u) {
                    ItemPerson it = new ItemPerson(u.getUid(), R.drawable.ic_baseline_account_circle_24,
                            u.getFirst_name() + " " + u.getLast_name(), 0, u.getDescription(),
                            u.getCity(), u.getCursus(), u.get_number_events_create(), u.get_number_events_attend(), u.getCenters_interests());
                    invits.add(it);
                    onUpdateAdapter();
                }
            });
        }
    }

    private void acceptFriendRequest(final String uid) {

        getUserData(uid, new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                USER.get_friends_requests_received().remove(mStoreBase.collection("users").document(uid));
                u.get_friends_requests_send().remove(USER_REFERENCE);

                USER.get_friends().add(mStoreBase.collection("users").document(uid));
                u.get_friends().add(USER_REFERENCE);

                updateUserData(USER);
                updateUserData(u);
            }
        });


        //save value

        Toast.makeText(getContext(), "Vous avez ajouté(e) cet utilisateur", Toast.LENGTH_SHORT).show();

        updateItems();
    }

    private void declineFriendRequest(String uid) {

        /*if(USERS_LIST.get(USER_ID).getDemande_amis_envoye().size() == 1) {
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
            USERS_LIST.get(id+"").getDemande_amis_envoye().remove(USER_ID);
        }

        if(USERS_LIST.get(id+"").getDemande_amis_recu().size() == 1) {
            USERS_LIST.get(id+"").getDemande_amis_recu().set(0, "");
        }else{
            USERS_LIST.get(id+"").getDemande_amis_recu().remove(USER_ID);
        }

        USERS_DB_REF.setValue(USERS_LIST);

        Toast.makeText(getContext(), "Vous n'avez pas accepté(e) cet utilisateur", Toast.LENGTH_SHORT).show();

        updateItems();*/

    }

    @Override
    public void onDataChangeListener() {
        updateItems();
    }

    @Override
    public void onUpdateAdapter() {
        mAdapter = new ItemInvitAmisAdapter(invits, getContext());

        mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitAmis);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemInvitAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                acceptFriendRequest(invits.get(position).getUid());
            }

            @Override
            public void onRemoveClick(int position) {
                declineFriendRequest(invits.get(position).getUid());
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
            if (nolonelyBundle.getSerializable("invitations") != null) {
                invits = (ArrayList<ItemPerson>) nolonelyBundle.getSerializable("invitations");
                onUpdateAdapter();
            } else {
                updateItems();
            }
    }

}