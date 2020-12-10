package com.example.nalone.ui.message.display;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.nalone.Cache;
import com.example.nalone.R;
import com.example.nalone.User;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.items.ItemPerson;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.ui.message.ChatActivity;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class MessagesAmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private View rootView;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private String search = "";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView addMessage;
    private List<String> uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_messages_amis, container, false);

        createFragment();

        return rootView;
    }

    private void createFragment() {
        search_bar = rootView.findViewById(R.id.search_bar_amis);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        buttonBack.setVisibility(View.GONE);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMessagesAmis);
        mSwipeRefreshLayout = rootView.findViewById(R.id.messageFriendSwipeRefreshLayout);
        addMessage = rootView.findViewById(R.id.create_event_button);

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
                    search = newText;
                    Log.w("Search", "Search : "+search);
                    adapterUsers();
                return false;
            }
        });

        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAmisFragment.type = "message";
                navController.navigate(R.id.action_navigation_messages_to_navigation_list_amis);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });

    }

    private void adapterUsers() {
        uid = new ArrayList<>();
        mStoreBase.collection("users").document(USER.getUid()).collection("chat_friends").limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User u = document.toObject(User.class);

                                uid.add(u.getUid());

                                uid.add(document.toObject(User.class).getUid());
                            }
                            if(!uid.isEmpty()){
                                Log.w("liste uid", uid.toString());
                                Query query = mStoreBase.collection("users").whereIn("uid", uid);
                                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                                adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
                                    @NonNull
                                    @Override
                                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
                                        return new UserViewHolder(view);
                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                                        userViewHolder.villePers.setText(u.getCity());
                                        userViewHolder.nomInvit.setText(u.getFirst_name() + " " + u.getLast_name());
                                        userViewHolder.button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);

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

                                        Constants.setUserImage(u, getContext(), userViewHolder.imagePerson);

                                        userViewHolder.layoutProfil.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPopUpProfil(u);
                                            }
                                        });

                                        userViewHolder.button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPopUpProfil(u);
                                            }
                                        });
                                    }
                                };
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                mRecyclerView.setAdapter(adapter);
                                adapter.startListening();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                        }}});
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
    private void removeFriend(final String uid) {
        if(mStoreBase.collection("users").document(USER.getUid()).
                collection("friends").document(uid) != null){
            mStoreBase.collection("users").document(USER.getUid()).
                    collection("friends").document(uid).delete();

            mStoreBase.collection("users").document(uid).
                    collection("friends").document(USER.getUid()).delete();

            Toast.makeText(getContext(), "Vous avez supprimé un amis !", Toast.LENGTH_SHORT).show();
        }

    }


    public void showPopUpProfil(final User u) {
        ChatActivity.USER_LOAD = u;
        startActivity(new Intent(getContext(), ChatActivity.class));
    }

    @Override
    public void onResume(){
        super.onResume();
        createFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

}