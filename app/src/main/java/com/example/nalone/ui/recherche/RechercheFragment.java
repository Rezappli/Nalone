package com.example.nalone.ui.recherche;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheFragment extends Fragment {

    private RecyclerView recyclerAmis;
    private RecyclerView recyclerGroupes;
    private CardView cardViewRechercheAmis;
    private CardView cardViewRechercheGroupes;
    private View rootView;
    private NavController navController;
    private List<String> friends;
    private List<String> mesGroupes;
    private FirestoreRecyclerAdapter adapterG;
    private FirestoreRecyclerAdapter adapterU;
    private TextView textViewRechercheAmis;
    private TextView textViewRechercheGroupes;
    private ProgressBar loading;
    private List<String> myGroups;
    private RelativeLayout relativeAmis, relativeGroup;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);

        return rootView;


    }

    public void createFragment(){
        myGroups = new ArrayList<>();
        recyclerAmis = rootView.findViewById(R.id.recyclerViewRechercheAmis);
        recyclerGroupes = rootView.findViewById(R.id.recyclerViewRechercheGroupes);
        cardViewRechercheAmis = rootView.findViewById(R.id.cardViewRechercheAmis);
        cardViewRechercheGroupes = rootView.findViewById(R.id.cardViewRechercheGroupes);
        textViewRechercheAmis = rootView.findViewById(R.id.textViewRechercheAmis);
        textViewRechercheGroupes = rootView.findViewById(R.id.textViewRechercheGroupes);
        loading = rootView.findViewById(R.id.search_loading);
        cardViewRechercheAmis.setVisibility(View.INVISIBLE);
        cardViewRechercheGroupes.setVisibility(View.INVISIBLE);
        relativeAmis = rootView.findViewById(R.id.relativeSansAmis);
        relativeGroup = rootView.findViewById(R.id.relativeSansGroup);


        textViewRechercheAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_recherche_amis);
            }
        });

        textViewRechercheGroupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_rcherche_groupe);
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

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

        friends = new ArrayList<>();

        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status","add")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                friends.add(document.getId());
                                Log.w("Friend", document.getId());
                            }
                            friends.add(USER.getUid());
                            //query
                            Query query = mStoreBase.collection("users").whereNotIn("uid", friends).limit(3);
                            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                            adapterUsers(options);


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



        if(adapterU != null){
            if(adapterU.getItemCount() == 0){
                relativeAmis.setVisibility(View.VISIBLE);
                textViewRechercheAmis.setVisibility(View.GONE);
            }
        }

        if(adapterG != null){
            if(adapterG.getItemCount() == 0){
                relativeGroup.setVisibility(View.VISIBLE);
                textViewRechercheGroupes.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        createFragment();
        super.onResume();
    }

    private void adapterGroups() {
        Query query;
        if(!myGroups.isEmpty()) {
            query = mStoreBase.collection("groups").whereNotIn("uid", myGroups).limit(3);

            FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();

            adapterG = new FirestoreRecyclerAdapter<Group, GroupViewHolder>(options) {
                @NonNull
                @Override
                public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupe, parent, false);
                    return new GroupViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull final GroupViewHolder userViewHolder, int i, @NonNull final Group g) {
                    final Group group = g;
                    userViewHolder.nomGroup.setText(g.getName());

                    userViewHolder.layoutGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopUpGroup(group);
                        }
                    });

                    userViewHolder.imageGroup.post(new Runnable() {
                        @Override
                        public void run() {
                            Constants.setGroupImage(g, getContext(), userViewHolder.imageGroup);
                        }
                    });
                }
            };
            recyclerGroupes.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerGroupes.setAdapter(adapterG);
            adapterG.startListening();
        }else{
            relativeGroup.setVisibility(View.VISIBLE);
            textViewRechercheGroupes.setVisibility(View.GONE);
        }
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView nomGroup;
        private LinearLayout layoutGroup;
        private ImageView imageGroup;
        private ImageView button;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            nomGroup = itemView.findViewById(R.id.nomGroupe);
            layoutGroup = itemView.findViewById(R.id.layoutGroupe);
            imageGroup = itemView.findViewById(R.id.imageGroupe);

        }
    }


    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_group);
    }

    private void adapterUsers(FirestoreRecyclerOptions<User> options) {
        adapterU = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.w("Add", "ViewHolder Recherche");
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
                return new UserViewHolder(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                userViewHolder.villePers.setText(u.getCity());
                userViewHolder.nomInvit.setText(u.getFirst_name() + " "+ u.getLast_name());

                if(u.getCursus().equalsIgnoreCase("Informatique")){
                    userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.RED);
                }

                if(u.getCursus().equalsIgnoreCase("TC")){
                    userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#00E9FD"));
                }

                if(u.getCursus().equalsIgnoreCase("MMI")){
                    userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#FF1EED"));
                }

                if(u.getCursus().equalsIgnoreCase("GB")){
                    userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#41EC57"));
                }

                if(u.getCursus().equalsIgnoreCase("LP")){
                    userViewHolder.cardViewPhotoPerson.setCardBackgroundColor((Color.parseColor("#EC9538")));
                }

                userViewHolder.button.setImageResource(0);
                Log.w("Add", "BienHolder Recherche");

                Constants.setUserImage(u, getContext(), userViewHolder.imagePerson);

                userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUpProfil(u);
                    }
                });
                loading.setVisibility(View.GONE);
                cardViewRechercheAmis.setVisibility(View.VISIBLE);
                cardViewRechercheGroupes.setVisibility(View.VISIBLE);
            }
        };
        Log.w("Add", "Set Adapter Recherche");
        recyclerAmis.setHasFixedSize(true);
        recyclerAmis.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerAmis.setAdapter(adapterU);
        adapterU.startListening();

    }
    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);

        }

    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;

        PopupProfilFragment.type = "recherche";
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapterG != null){
            adapterG.stopListening();
            adapterG = null;
        }
        if(adapterU != null){
            adapterU.stopListening();
            adapterU = null;
        }

    }
}