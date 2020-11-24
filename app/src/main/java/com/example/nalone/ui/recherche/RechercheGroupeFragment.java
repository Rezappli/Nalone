package com.example.nalone.ui.recherche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.Group;
import com.example.nalone.R;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheGroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private ImageView addGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_recherche_groupe, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);
        addGroup = root.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_groupe_to_navigation_creat_group);
            }
        });

        adapterGroups();

        return root;
    }

    private void adapterGroups() {
        DocumentReference ref = mStoreBase.document("users/"+USER_ID);
        Query query = mStoreBase.collection("groups").whereNotEqualTo("ownerDoc", ref);
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
                           showPopUpGroup(group);
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

    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_recherche_groupe_to_navigation_popup_group);
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