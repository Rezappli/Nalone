package com.example.nalone.ui.message.display;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nalone.R;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.objects.Group;
import com.example.nalone.ui.amis.display.PopUpMesGroupesFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class MessagesGroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private ImageView addGroup;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mes_groupe, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment() {
        mRecyclerView = rootView.findViewById(R.id.recyclerViewGroupe);
        addGroup = rootView.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mSwipeRefreshLayout = rootView.findViewById(R.id.messageGroupSwipeRefreshLayout);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAmisFragment.type = "message_groupe";
                navController.navigate(R.id.action_navigation_messages_to_navigation_list_amis);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });

        adapterGroups();
    }

    private void adapterGroups() {

        Query query = mStoreBase.collection("groups").whereEqualTo("ownerDoc", "users/" + USER.getUid());
        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();

        adapter = new FirestoreRecyclerAdapter<Group, GroupViewHolder>(options) {
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

                Constants.setGroupImage(g, userViewHolder.imageGroup);

            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
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
        PopUpMesGroupesFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_mes_groupes_to_navigation_popup_mes_groupes);
    }

    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }


    @Override
    public void onStart() {
        super.onStart();
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