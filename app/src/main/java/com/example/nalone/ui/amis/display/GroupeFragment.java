package com.example.nalone.ui.amis.display;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.Group;
import com.example.nalone.R;
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
import static com.example.nalone.util.Constants.setGroupImage;

public class GroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private ImageView addGroup;
    private CardView mesGroupes;
    private List<String> myGroups;
    private LinearLayout linearSansMesGroupes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_groupe, container, false);

        myGroups = new ArrayList<>();
        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);
        linearSansMesGroupes = root.findViewById(R.id.linearSansMesGroupes);
        addGroup = root.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mesGroupes = root.findViewById(R.id.mesGroupes);

        mesGroupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_mes_groupes);
            }
        });

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_creat_group);
            }
        });

        DocumentReference ref = mStoreBase.collection("users").document(USER_ID);
        mStoreBase.collection("users").document(USER.getUid()).collection("groups").whereEqualTo("status","add")
                .whereNotEqualTo("user", ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    myGroups.add(document.getId());
                                }
                                adapterGroups();
                            }else{
                                linearSansMesGroupes.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return root;
    }

    private void adapterGroups() {

        if(!myGroups.isEmpty()){
            Query query = mStoreBase.collection("groups").whereIn("uid", myGroups);
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

                    setGroupImage(g,getContext(),userViewHolder.imageGroup);
                    //linearSansMesGroupes.setVisibility(View.GONE);
                }
            };
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mRecyclerView.setAdapter(adapter);
            adapter.startListening();
        }

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
        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_group);


    }


    @Override
    public void onStart(){
        super.onStart();
        //updateItems();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }


}