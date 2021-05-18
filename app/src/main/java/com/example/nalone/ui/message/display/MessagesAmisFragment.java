package com.example.nalone.ui.message.display;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.R;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.objects.User;
import com.example.nalone.ui.message.ChatActivityFriend;
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

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class MessagesAmisFragment extends Fragment {

    private SearchView search_bar;

    private NavController navController;
    private View rootView;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private String search = null;
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
                search = newText.toLowerCase();
                adapterUsers();
                return false;
            }
        });

        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAmisFragment.type = "message_ami";
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
                            Log.w("Message", "Task sucessful");
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                uid.add(document.getId());
                            }

                            if (!uid.isEmpty()) {
                                Log.w("Message", uid.toString());
                                Query query = mStoreBase.collection("users").whereIn("uid", uid);
                                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                                adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
                                    @NonNull
                                    @Override
                                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
                                        return new UserViewHolder(view);
                                    }

                                    @SuppressLint("SetTextI18n")
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull final User u) {
                                        userViewHolder.villePers.setText(u.getCity());
                                        userViewHolder.nomInvit.setText(u.getName());
                                        userViewHolder.button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);

                                        Constants.setUserImage(u, userViewHolder.imagePerson);

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
                            } else {
                                mRecyclerView.setAdapter(null);
                            }
                        }
                    }
                });
    }

    private boolean matchToUser(User u) {
        Log.w("Recherche", "Utilisateur : " + u.getName());
        int max_lenght = 0;
        max_lenght = u.getName().length();
        /*if (u.getFirst_name().length() < u.getLast_name().length()) {
            max_lenght = u.getLast_name().length();
        }
        for (int i = 0; i < search.length(); i++) {

            String u_first = u.getFirst_name().toLowerCase();
            String u_last = u.getLast_name().toLowerCase();

            Log.w("Recherche", "Valeur de recherche :" + (u_first.charAt(i) == search.charAt(i) || u_last.charAt(i) == search.charAt(i)));
            return (u_first.charAt(i) == search.charAt(i) || u_last.charAt(i) == search.charAt(i));
        }*/

        return false;
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

    public void showPopUpProfil(final User u) {
        ChatActivityFriend.USER_LOAD = u;
        startActivity(new Intent(getContext(), ChatActivityFriend.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}