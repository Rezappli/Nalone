package com.example.nalone.ui.amis.display;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.Group;
import com.example.nalone.adapter.ItemInvitAmisAdapter;
import com.example.nalone.User;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.nolonelyBundle;
import static com.example.nalone.util.Constants.updateUserData;

public class MesInvitationsFragment extends Fragment implements CoreListener {

    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private View rootView;
    private ArrayList<ItemPerson> invits = new ArrayList<>();
    private ProgressBar loading;
    List<String> friends;


    public MesInvitationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listeners.add(this);
       rootView = inflater.inflate(R.layout.fragment_mes_invitations, container, false);
        loading = rootView.findViewById(R.id.invits_loading);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewInvitAmis);
        adapterInvits();

        return rootView;
    }

    private void adapterInvits() {
        friends = new ArrayList<>();
        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "received").limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                friends.add(document.getId());
                            }
                            //query
                            Query query = mStoreBase.collection("users").whereIn("uid", friends);
                            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                            adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
                                @NonNull
                                @Override
                                public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invit_amis, parent, false);
                                    return new UserViewHolder(view);
                                }

                                @Override
                                protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                                    userViewHolder.villePers.setText(u.getCity());
                                    userViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());

                                    if (!Cache.fileExists(u.getUid())) {
                                        StorageReference imgRef = mStore.getReference("users/" + u.getUid());
                                        if (imgRef != null) {
                                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Uri img = task.getResult();
                                                        if (img != null) {
                                                            Log.w("image", "save image from cache");
                                                            Cache.saveUriFile(u.getUid(), img);
                                                            Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.w("image", "get image from cache");
                                            Glide.with(getContext()).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                        }
                                    }

                                        userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPopUpProfil(u);
                                            }
                                        });

                                    userViewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //addFriend(u.getUid());
                                        }
                                    });

                                        userViewHolder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //removeFriend(u.getUid());
                                            }
                                        });
                                        loading.setVisibility(View.GONE);

                                }
                            };
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            mRecyclerView.setAdapter(adapter);
                            adapter.startListening();
                        }
                        ;
                    }
                });
    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
       /* if(USER.get_friends_requests_send().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_hourglass_top_24;
       }else if(USER.get_friends_requests_received().contains(mStoreBase.collection("users").document(u.getUid()))){
            PopupProfilFragment.button = R.drawable.ic_round_mail_24;
        }else{*/
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;
        // }

        navController.navigate(R.id.action_navigation_invit_amis_to_navigation_popup_profil);
    }


    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private TextView villePers;
        private ImageView buttonAdd, buttonRemove;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomAmisInvit);
            villePers = itemView.findViewById(R.id.villeAmisInvit);
            layoutProfil = itemView.findViewById(R.id.layoutInvitAmi);
            imagePerson = itemView.findViewById(R.id.imageInvitAmis);
            buttonAdd = itemView.findViewById(R.id.addInvitAmis);
            buttonRemove = itemView.findViewById(R.id.removeInvitAmis);
        }

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

        /*mAdapter.setOnItemClickListener(new ItemInvitAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                acceptFriendRequest(invits.get(position).getUid());
            }

            @Override
            public void onRemoveClick(int position) {
                declineFriendRequest(invits.get(position).getUid());
            }
        });*/

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

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}