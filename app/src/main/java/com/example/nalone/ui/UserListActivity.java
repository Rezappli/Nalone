package com.example.nalone.ui;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.adapter.UserListAdapter;
import com.example.nalone.objects.User;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProgressBar loading;
    private UserListAdapter userListAdapter;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if (getIntent() != null) {
            users = (ArrayList<User>) getIntent().getSerializableExtra("users");
        }

        loading = findViewById(R.id.invits_loading);
        mRecyclerView = findViewById(R.id.recyclerViewInvitAmis);
        
        this.userListAdapter = new UserListAdapter(this.users);
        this.mRecyclerView.setAdapter(this.userListAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}