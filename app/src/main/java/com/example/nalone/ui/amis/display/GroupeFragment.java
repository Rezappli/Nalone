package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nalone.Group;
import com.example.nalone.User;
import com.example.nalone.adapter.ItemGroupeAdapter;
import com.example.nalone.adapter.ItemProfilAdapter;
import com.example.nalone.items.ItemGroupe;
import com.example.nalone.listeners.CoreListener;
import com.example.nalone.R;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.example.nalone.ui.recherche.RechercheFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.mStoreBase;

public class GroupeFragment extends Fragment implements CoreListener {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;

    public GroupeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.groupe_fragment, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);

        adapterGroups();

        return root;
    }

    private void adapterGroups() {
        Query query = mStoreBase.collection("groups");
        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();

        adapter = new FirestoreRecyclerAdapter<Group, UserViewHolder>(options) {
                @NonNull
                @Override
                public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupe,parent,false);
                    return new UserViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final Group g) {
                    final Group group = g;
                    userViewHolder.nomGroup.setText(g.getName());

                    userViewHolder.layoutGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // showPopUpProfil(user);
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
        }

        private class UserViewHolder extends RecyclerView.ViewHolder {

            private TextView nomGroup;
            private LinearLayout layoutGroup;
            private ImageView imageGroup;
            private ImageView button;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);

                nomGroup = itemView.findViewById(R.id.nomGroupe);
                layoutGroup = itemView.findViewById(R.id.layoutGroupe);
                imageGroup = itemView.findViewById(R.id.imageGroupe);

            }

        }


    @Override
    public void onDataChangeListener() {
        //updateItems();
    }

    @Override
    public void onStart(){
        super.onStart();
        //updateItems();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}