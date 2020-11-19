package com.example.nalone.ui.amis.display;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.UserFriendData;
import com.example.nalone.User;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class MesInvitationsFragment extends Fragment {

    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private View rootView;
    private ArrayList<ItemPerson> invits = new ArrayList<>();
    private ProgressBar loading;
    List<String> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                            if (!friends.isEmpty()) {
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
                                                acceptFriendRequest(u.getUid());
                                            }
                                        });

                                        userViewHolder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                declineFriendRequest(u.getUid());
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
                        }
                    }
                });
    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = 0;

        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
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

    private void acceptFriendRequest(final String uid) {
        UserFriendData data1 = new UserFriendData("add", mStoreBase.collection("users").document(USER.getUid()));
        UserFriendData data2 = new UserFriendData("add", mStoreBase.collection("users").document(uid));
        mStoreBase.collection("users").document(USER.getUid()).collection("friends").document(uid).set(data2);
        mStoreBase.collection("users").document(uid).collection("friends").document(USER.getUid()).set(data1);
        Toast.makeText(getContext(), "Vous avez ajouté(e) cet utilisateur", Toast.LENGTH_SHORT).show();
    }

    private void declineFriendRequest(final String uid) {

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").document(uid).delete();

        mStoreBase.collection("users").document(uid).collection("friends").document(USER.getUid()).delete();

        Toast.makeText(getContext(), "Vous n'avez pas accepté(e) cet utilisateur", Toast.LENGTH_SHORT).show();
        adapter.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}