package com.example.nalone.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.UserFriendData;
import com.example.nalone.User;
import com.example.nalone.R;
import com.example.nalone.adapter.FirestoreRecyclerAdapterUserFriendData;
import com.example.nalone.listeners.FireStoreUsersListeners;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.getUserData;
import static com.example.nalone.util.Constants.mStoreBase;

public class MessagesFragment extends Fragment {


    private List<User> users = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapterUserFriendData adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = root.findViewById(R.id.recycler);

        CollectionReference usersRef = mStoreBase.collection("users").document(USER.getUid())
                .collection("friends");

        Query query = usersRef.orderBy("add_time");

        FirestoreRecyclerOptions<UserFriendData> options = new FirestoreRecyclerOptions.Builder<UserFriendData>().setQuery(query, UserFriendData.class).build();
        adapter = new FirestoreRecyclerAdapterUserFriendData(options);
        Log.w("Doc", "adapter : " + adapter.getItemCount());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FirestoreRecyclerAdapterUserFriendData.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {
                //ChatActivity.USER_LOAD =
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}


