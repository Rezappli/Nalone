package com.example.nalone.ui.recherche;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.Group;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheGroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private ImageView addGroup;
    private SwipeRefreshLayout swipeContainer;
    private View root;
    private List<String> myGroups;
    private LinearLayout linearSansRechercheGroupe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_recherche_groupe, container, false);

        createFragment();
        return root;
    }

    private void createFragment(){
        myGroups = new ArrayList<>();

        linearSansRechercheGroupe = root.findViewById(R.id.linearSansRechercheGroupe);
        swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        this.configureSwipeRefreshLayout();

        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);
        addGroup = root.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_groupe_to_navigation_creat_group);
            }
        });

        mStoreBase.collection("users").document(USER.getUid()).collection("groups").whereEqualTo("status","add")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                myGroups.add(document.getId());
                            }
                            adapterGroups();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(adapter != null){
            if(adapter.getItemCount() == 0){
                linearSansRechercheGroupe.setVisibility(View.VISIBLE);
            }
        }

    }

    private void configureSwipeRefreshLayout(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });
    }


    private void adapterGroups() {

        Query query;
        if(myGroups.isEmpty()){
            DocumentReference ref = mStoreBase.document("users/"+USER_ID);
            query = mStoreBase.collection("groups").whereNotEqualTo("ownerDoc",ref);
        }else{
            query = mStoreBase.collection("groups").whereNotIn("uid", myGroups);
        }

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

                    if(g.getImage_url() != null) {
                        if(!Cache.fileExists(g.getUid())) {
                            StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                            if (imgRef != null) {
                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri img = task.getResult();
                                            if (img != null) {
                                                Cache.saveUriFile(g.getUid(), img);
                                                g.setImage_url(Cache.getImageDate(g.getUid()));
                                                mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                            }
                                        }
                                    }
                                });
                            }
                        }else{
                            Uri imgCache = Cache.getUriFromUid(g.getUid());
                            if(Cache.getImageDate(g.getUid()).equalsIgnoreCase(g.getImage_url())) {
                                Glide.with(getContext()).load(imgCache).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                            }else{
                                StorageReference imgRef = mStore.getReference("groups/" + g.getUid());
                                if (imgRef != null) {
                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri img = task.getResult();
                                                if (img != null) {
                                                    Cache.saveUriFile(g.getUid(), img);
                                                    g.setImage_url(Cache.getImageDate(g.getUid()));
                                                    mStoreBase.collection("groups").document(g.getUid()).set(g);
                                                    Glide.with(getContext()).load(img).fitCenter().centerCrop().into(userViewHolder.imageGroup);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }

                }
            };
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(adapter);
            adapter.startListening();
            swipeContainer.setRefreshing(false);
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
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }

    }


}