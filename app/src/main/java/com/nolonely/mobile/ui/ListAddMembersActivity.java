package com.nolonely.mobile.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.UserListAdapter;
import com.nolonely.mobile.enumeration.UserList;
import com.nolonely.mobile.objects.User;

import java.util.ArrayList;

public class ListAddMembersActivity extends ListActivity {

    private ArrayList<User> usersAdd;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            this.typeList = UserList.valueOf(getIntent().getStringExtra(EXTRA_TYPE_LIST).toUpperCase());
            this.users = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_USERS_LIST);

            title.setText(getString(R.string.title_add_participant));

            this.userListAdapter = new UserListAdapter(this.users, typeList);
            this.mRecyclerView.setAdapter(this.userListAdapter);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            usersAdd = new ArrayList<>();
            userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                @Override
                public void onDisplayClick(int position) {

                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onAddClick(int position) {

                    addMember(position);
                }

                @Override
                public void onRemoveClick(int position) {

                    addMember(position);
                    usersAdd.remove(users.get(position));
                }
            });

        }


    }

    private void addMember(int position) {
        usersAdd.add(users.get(position));
        Toast.makeText(ListAddMembersActivity.this, users.get(position).getName() + getString(R.string.adding_participant), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ACTION_RECEIVE_USERS_LIST);
        intent.putExtra(EXTRA_BROADCAST_USERS_LIST, usersAdd);
        sendBroadcast(intent);

        removeItem(position);
    }

}