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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.UserFriendData;
import com.example.nalone.adapter.ItemListAmisAdapter;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.example.nalone.ui.recherche.RechercheFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_REFERENCE;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.listeners;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.nolonelyBundle;
import static com.example.nalone.util.Constants.updateUserData;

public class AmisFragment extends Fragment implements CoreListener{

    private SearchView search_bar;

    private NavController navController;
    private TextView resultat;
    private ArrayList<ItemPerson> items = new ArrayList<>();
    private static String message = "null";
    private View rootView;
    private ProgressBar loading;

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;



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
        loading = rootView.findViewById(R.id.amis_loading);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);

        //query
        Query query = firebaseFirestore.collection("users").document(USER.getUid()).collection("friends");
        //RecyclerOption
        FirestoreRecyclerOptions<UserFriendData> options = new FirestoreRecyclerOptions.Builder<UserFriendData>().setQuery(query, UserFriendData.class).build();
        adapterUsers(options);

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

    private void adapterUsers(FirestoreRecyclerOptions<UserFriendData> options) {
        adapter = new FirestoreRecyclerAdapter<UserFriendData, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final UserFriendData data) {
                getUserData(data.getUser().getId(), new FireStoreUsersListeners() {
                    @Override
                    public void onDataUpdate(final User u) {
                        userViewHolder.villePers.setText(u.getCity());
                        userViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());
                        userViewHolder.button.setImageResource(0);

                        if(!Cache.fileExists(u.getUid())) {
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
                            }
                        }else{
                            Log.w("image", "get image from cache");
                            Glide.with(getContext()).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                        }


                        userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPopUpProfil(u);
                            }
                        });
                    }
                });


                /*getUserData(u.getUid(), new FireStoreUsersListeners() {
                    @Override
                    public void onDataUpdate(final User u) {
                        if (u.getImage_url() != null) {
                            if(!Cache.fileExists(u.getUid())) {
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
                                                    Glide.with(context).load(img).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                                                }
                                            }
                                        }
                                    });
                                }
                            }else{
                                Log.w("image", "get image from cache");
                                Glide.with(context).load(Cache.getUriFromUid(u.getUid())).fitCenter().centerCrop().into(userViewHolder.imagePerson);
                            }
                        }
                    }
                });*/

            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        loading.setVisibility(View.GONE);
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.imageView19);

        }

    }
    private void removeFriend(final String uid, final int position) {
        /*getUserData(uid, new FireStoreUsersListeners() {
            @Override
            public void onDataUpdate(User u) {
                USER.get_friends().remove(mStoreBase.collection("users").document(uid));
                u.get_friends().remove(USER_REFERENCE);

                updateUserData(u);
                updateUserData(USER);
            }
        });*/

        Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();
    }



    private void updateItems() {
        /*final boolean[] duplicate = {false};
        items.clear();
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
                        for(ItemPerson i : items) {
                            if (i.getUid().equalsIgnoreCase(it.getUid())) {
                                duplicate[0] = true;
                                break;
                            }
                        }

                        if(!duplicate[0]) {
                            items.add(it);
                            onUpdateAdapter();
                        }
                    }
                });
            }
        }*/
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

        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_profil);
    }

    @Override
    public void onDataChangeListener() {
        Log.w("Amis", "Update data on firestore");
        updateItems();
    }

    //@Override
    public void onUpdateAdapter() {
        loading.setVisibility(View.GONE);
        Log.w("Amis", "Update data on adapter");


        if(nolonelyBundle.getSerializable("invits") != null) {
            if((ArrayList<ItemPerson>)nolonelyBundle.getSerializable("invits") != items){
                nolonelyBundle.putSerializable("friends", items);
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
       /* if(nolonelyBundle.getSerializable("friends") != null){
            Log.w("Amis", "Chargement du bundle");
            items = (ArrayList<ItemPerson>) nolonelyBundle.getSerializable("friends");
            onUpdateAdapter();
        }else{
            updateItems();
        }*/
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}