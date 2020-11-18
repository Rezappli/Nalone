package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    private View rootView;
    private ArrayList<ItemPerson> invits = new ArrayList<>();
    private ProgressBar loading;


    public MesInvitationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listeners.add(this);
       rootView = inflater.inflate(R.layout.fragment_mes_invitations, container, false);
        loading = rootView.findViewById(R.id.invits_loading);


        return rootView;
    }

    private void updateItems() {
        /*final boolean[] duplicate = {false};
        invits.clear();
        Log.w("Invitations", "" + USER.get_friends_requests_received().size());

        if(USER.get_friends_requests_received().size() > 0) {
            for (int i = 0; i < USER.get_friends_requests_received().size(); i++) {
                Log.w("Invitations","Chargemment de "+ USER.get_friends_requests_received().get(i).getId());
                getUserData(USER.get_friends_requests_received().get(i).getId(), new FireStoreUsersListeners() {
                    @Override
                    public void onDataUpdate(User u) {
                        ItemPerson it = new ItemPerson(u.getUid(), R.drawable.ic_baseline_account_circle_24,
                                u.getFirst_name() + " " + u.getLast_name(), 0, u.getDescription(),
                                u.getCity(), u.getCursus(), u.get_number_events_create(), u.get_number_events_attend(), u.getCenters_interests());

                        for(ItemPerson i : invits) {
                            if (i.getUid().equalsIgnoreCase(it.getUid())) {
                                duplicate[0] = true;
                                break;
                            }
                        }

                        if(!invits.contains(it)) {
                            invits.add(it);
                            onUpdateAdapter();
                        }
                    }
                });
            }
        }else{
            onUpdateAdapter();
        }*/
    }

    private void acceptFriendRequest(final String uid) {

       /*getUserData(uid, new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                USER.get_friends_requests_received().remove(mStoreBase.collection("users").document(uid));
                u.get_friends_requests_send().remove(USER_REFERENCE);

                USER.get_friends().add(mStoreBase.collection("users").document(uid));
                u.get_friends().add(USER_REFERENCE);

                updateUserData(u);
                updateUserData(USER);
            }
        });*/

        Toast.makeText(getContext(), "Vous avez ajouté(e) cet utilisateur", Toast.LENGTH_SHORT).show();
    }

    private void declineFriendRequest(final String uid) {

        /*getUserData(uid, new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                USER.get_friends_requests_received().remove(mStoreBase.collection("users").document(uid));
                u.get_friends_requests_send().remove(USER_REFERENCE);

                updateUserData(u);
                updateUserData(USER);

            }
        });*/

        Toast.makeText(getContext(), "Vous n'avez pas accepté(e) cet utilisateur", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChangeListener() {
        Log.w("Invitations", "Update data on firestore");
        updateItems();
    }

    //@Override
    public void onUpdateAdapter() {
        loading.setVisibility(View.GONE);
        Log.w("Invitations", "Update adapter");
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

        if(nolonelyBundle.getSerializable("invits") != null) {
            if((ArrayList<ItemPerson>)nolonelyBundle.getSerializable("invits") != invits){
                Log.w("Invitations", "Add invits");
                nolonelyBundle.putSerializable("invits", invits);
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(nolonelyBundle.getSerializable("invits") != null){
            invits = (ArrayList<ItemPerson>) nolonelyBundle.getSerializable("invits");
            onUpdateAdapter();
        }else{
            updateItems();
        }
    }
}