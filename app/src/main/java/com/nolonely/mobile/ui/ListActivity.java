package com.nolonely.mobile.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.UserListAdapter;
import com.nolonely.mobile.enumeration.UserList;
import com.nolonely.mobile.objects.User;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    protected RecyclerView mRecyclerView;
    protected UserListAdapter userListAdapter;
    protected ArrayList<User> users;
    protected UserList typeList;
    public static String EXTRA_TYPE_LIST = "EXTRA_TYPE_LIST";
    public static String EXTRA_USERS_LIST = "EXTRA_USERS_LIST";
    public static String EXTRA_BROADCAST_USERS_LIST = "EXTRA_BROADCAST_USERS_LIST";
    public static String ACTION_RECEIVE_USERS_LIST = "ACTION_RECEIVE_USERS_LIST";
    protected TextView title;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mRecyclerView = findViewById(R.id.recyclerViewInvitAmis);
        this.users = new ArrayList<>();
        title = findViewById(R.id.title_user_list);
    }

    protected void removeItem(int position) {
        users.remove(users.get(position));
        if (users.isEmpty()) {
            onBackPressed();
        }
        userListAdapter.notifyDataSetChanged();
    }

}