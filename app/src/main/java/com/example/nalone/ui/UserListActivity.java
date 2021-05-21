package com.example.nalone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalone.R;
import com.example.nalone.adapter.user.UserListAdapter;
import com.example.nalone.enumeration.UserList;
import com.example.nalone.objects.User;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    protected UserListAdapter userListAdapter;
    protected ArrayList<User> users;
    protected UserList typeList;
    public static String EXTRA_TYPE_LIST = "EXTRA_TYPE_LIST";
    public static String EXTRA_USERS_LIST = "EXTRA_USERS_LIST";
    public static String EXTRA_BROADCAST_USERS_LIST = "EXTRA_BROADCAST_USERS_LIST";
    public static String ACTION_RECEIVE_USERS_LIST = "ACTION_RECEIVE_USERS_LIST";
    private ArrayList<User> usersAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mRecyclerView = findViewById(R.id.recyclerViewInvitAmis);
        this.users = new ArrayList<>();

        if (getIntent() != null) {
            this.typeList = UserList.valueOf(getIntent().getStringExtra(EXTRA_TYPE_LIST).toUpperCase());
            this.users = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_USERS_LIST);

            this.userListAdapter = new UserListAdapter(this.users, typeList);
            this.mRecyclerView.setAdapter(this.userListAdapter);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            usersAdd = new ArrayList<>();
            userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                @Override
                public void onDisplayClick(int position) {

                }

                @Override
                public void onAddClick(int position) {
                    usersAdd.add(users.get(position));
                    Toast.makeText(UserListActivity.this, users.get(position).getName() + getString(R.string.adding_participant), Toast.LENGTH_SHORT).show();
                    users.remove(users.get(position));
                    if (users.isEmpty()) {
                        onBackPressed();
                    }
                    userListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRemoveClick(int position) {
                    usersAdd.remove(users.get(position));
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BROADCAST_USERS_LIST, users);
        setResult(RESULT_OK, intent);
        finish();
    }

}