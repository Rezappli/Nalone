package com.example.nalone.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.user.UserListAdapter;
import com.example.nalone.enumeration.UserList;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.User;
import com.example.nalone.util.Constants;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.example.nalone.util.Constants.USER;

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

        TextView title = findViewById(R.id.title_user_list);
        if (getIntent() != null) {
            this.typeList = UserList.valueOf(getIntent().getStringExtra(EXTRA_TYPE_LIST).toUpperCase());
            this.users = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_USERS_LIST);

            switch (typeList) {
                case INVIT_FRIEND:
                    title.setText(getString(R.string.title_invitation_friend));
                    break;
                case MEMBERS:
                case INVIT_EVENT:
                    title.setText(getString(R.string.title_add_participant));
                    break;
            }
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
                    switch (typeList) {
                        case INVIT_FRIEND:
                            addInvitationFriend(position);
                            break;
                        case INVIT_EVENT:
                            addMember(position);
                            break;
                    }
                }

                @Override
                public void onRemoveClick(int position) {
                    usersAdd.remove(users.get(position));
                }
            });

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addInvitationFriend(int position) {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", users.get(position));

        JSONController.getJsonArrayFromUrl(Constants.URL_ADD_FRIEND, this, params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                removeItem(position);
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMember(int position) {
        usersAdd.add(users.get(position));
        Toast.makeText(UserListActivity.this, users.get(position).getName() + getString(R.string.adding_participant), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ACTION_RECEIVE_USERS_LIST);
        intent.putExtra(EXTRA_BROADCAST_USERS_LIST, usersAdd);
        sendBroadcast(intent);

        removeItem(position);
    }

    private void removeItem(int position) {
        users.remove(users.get(position));
        if (users.isEmpty()) {
            onBackPressed();
        }
        userListAdapter.notifyDataSetChanged();
    }

}