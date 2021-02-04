package com.example.nalone.dialog;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.nalone.objects.Evenement;
import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.amis.display.CreateGroupFragment;
import com.example.nalone.ui.evenements.creation.CreateEventFragment;
import com.example.nalone.ui.message.ChatActivityFriend;
import com.example.nalone.ui.message.ChatActivityGroup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setGroupImage;
import static com.example.nalone.util.Constants.setUserImage;

public class ListAmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private TextView resultat;
    private ArrayList<ItemPerson> items = new ArrayList<>();
    private static String message = "null";
    private View rootView;
    private ProgressBar loading;

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<String> friends;
    private List<String> groups;
    private List<String> adds;
    private List<String> adds_group;

    private Button valider;
    private int remove, add;

    public static String type;
    public static Evenement EVENT_LOAD;
    public static Group GROUP_LOAD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_amis, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment(){
        adds = new ArrayList<>();
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        resultat = rootView.findViewById(R.id.resultatText_amis);
        resultat.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loading = rootView.findViewById(R.id.amis_loading);
        valider = rootView.findViewById(R.id.validerListAmi);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewAmisAdd);
        remove = R.drawable.ic_baseline_remove_24;
        add = R.drawable.ic_baseline_add_24;
        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == "group") {
                    navController.navigate(R.id.action_navigation_list_amis_to_navigation_creat_group);
                }
                if (type == "event") {
                    //navController.navigate(R.id.action_navigation_list_amis_to_navigation_create_event);
                    CreateEventFragment.EVENT_LOAD = EVENT_LOAD;
                }
                if (type == "message_ami" || type == "message_groupe") {
                    navController.navigate(R.id.action_navigation_list_amis_to_navigation_messages);
                }
            }
        });


        if (type == "message_ami" || type == "message_groupe") {
            valider.setVisibility(View.GONE);
        }
        ajoutMembres();

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Type", type);
                for (String a : adds) {
                    Log.w("Adds", a);
                }
                if (type == "group") {
                    CreateGroupFragment.adds = adds;
                    navController.navigate(R.id.action_navigation_list_amis_to_navigation_creat_group);
                }
                if (type == "event") {
                    CreateEventFragment.adds = adds;
                    //navController.navigate(R.id.action_navigation_list_amis_to_navigation_create_event);
                }

            }
        });

        if (type == "message_groupe") {
            adapterGroups();
        }else
            adapterUsers();
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

    }

    private void adapterGroups() {
        groups = new ArrayList<>();
        DocumentReference ref = mStoreBase.collection("users").document(USER_ID);


        Query query = mStoreBase.collection("groups").whereEqualTo("ownerDoc", ref).limit(10);
        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();

        adapter = new FirestoreRecyclerAdapter<Group, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final Group g) {
                userViewHolder.nomInvit.setText(g.getName());
                userViewHolder.villePers.setVisibility(View.GONE);
                userViewHolder.button.setImageResource(R.drawable.ic_baseline_add_24);

                                       /* if(adds_group.contains(g.getUid())){
                                            userViewHolder.button.setImageDrawable(getResources().getDrawable(remove));
                                        }else{
                                            userViewHolder.button.setImageDrawable(getResources().getDrawable(add));
                                        }*/

                userViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == "message_groupe") {
                            mStoreBase.collection("users").document(USER_ID).collection("chat_groups")
                                    .document(g.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ChatActivityGroup.nouveau = false;
                                        } else {
                                            ChatActivityGroup.nouveau = true;
                                        }
                                    }
                                    ChatActivityGroup.GROUP_LOAD =g;
                                    startActivity(new Intent(getContext(), ChatActivityGroup.class));
                                }
                            });
                        }else{
                            /*else{
                                                    if (!adds.contains(u.getUid()) || adds.isEmpty()) {
                                                        userViewHolder.button.setImageDrawable(getResources().getDrawable(remove));
                                                        adds.add(u.getUid());
                                                    } else {
                                                        userViewHolder.button.setImageDrawable(getResources().getDrawable(add));
                                                        adds.remove(u.getUid());
                                                    }
                                                }*/
                        }
                    }
                });


                setGroupImage(g, getContext(), userViewHolder.imagePerson);

                loading.setVisibility(View.GONE);


            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);

        adapter.startListening();

    }

    private void ajoutMembres() {
        if(type == "group"){
            for(String s : CreateGroupFragment.adds ){
                adds.add(s);
            }
        }
        if(type == "event"){
            for(String s : CreateEventFragment.adds){
                adds.add(s);
            }
        }

    }

    private void adapterUsers() {
        friends = new ArrayList<>();
        mStoreBase.collection("users").document(USER.getUid()).collection("friends").whereEqualTo("status", "add").limit(10)
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
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
                                        return new UserViewHolder(view);
                                    }

                                    @Override
                                    protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                                        userViewHolder.villePers.setText(u.getCity());
                                        userViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());

                                        if(type.equalsIgnoreCase("message_ami")){
                                            userViewHolder.button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                                        }else{
                                            userViewHolder.button.setImageResource(R.drawable.ic_baseline_add_24);
                                            if(adds.contains(u.getUid())){
                                                userViewHolder.button.setImageDrawable(getResources().getDrawable(remove));
                                            }else{
                                                userViewHolder.button.setImageDrawable(getResources().getDrawable(add));
                                            }
                                        }

                                        userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (type == "message_ami"){
                                                    mStoreBase.collection("users").document(USER_ID).collection("chat_friends")
                                                            .document(u.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {
                                                                    ChatActivityFriend.nouveau = false;
                                                                } else {
                                                                    ChatActivityFriend.nouveau = true;
                                                                }
                                                            }
                                                            ChatActivityFriend.USER_LOAD = u;
                                                            startActivity(new Intent(getContext(), ChatActivityFriend.class));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        userViewHolder.button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (type == "message_ami"){
                                                    mStoreBase.collection("users").document(USER_ID).collection("chat_friends")
                                                            .document(u.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {
                                                                    ChatActivityFriend.nouveau = false;
                                                                } else {
                                                                    ChatActivityFriend.nouveau = true;
                                                                }
                                                            }
                                                            ChatActivityFriend.USER_LOAD = u;
                                                            startActivity(new Intent(getContext(), ChatActivityFriend.class));
                                                        }
                                                    });
                                                }else{
                                                    if (!adds.contains(u.getUid()) || adds.isEmpty()) {
                                                        userViewHolder.button.setImageDrawable(getResources().getDrawable(remove));
                                                        adds.add(u.getUid());
                                                    } else {
                                                        userViewHolder.button.setImageDrawable(getResources().getDrawable(add));
                                                        adds.remove(u.getUid());
                                                    }
                                                }
                                            }
                                        });


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
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor(Color.parseColor("#41EC57")); }

                                        if(u.getCursus().equalsIgnoreCase("LP")){
                                            userViewHolder.cardViewPhotoPerson.setCardBackgroundColor((Color.parseColor("#EC9538")));
                                        }


                                        setUserImage(u,getContext(),userViewHolder.imagePerson);

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

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        createFragment();
        super.onResume();
    }
}