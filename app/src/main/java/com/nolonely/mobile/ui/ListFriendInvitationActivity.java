package com.nolonely.mobile.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.UserListAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.UserList;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.nolonely.mobile.util.Constants.USER;

public class ListFriendInvitationActivity extends ListActivity {


    private ArrayList<User> usersAdd;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            this.typeList = UserList.valueOf(getIntent().getStringExtra(EXTRA_TYPE_LIST).toUpperCase());
            this.users = (ArrayList<User>) getIntent().getSerializableExtra(EXTRA_USERS_LIST);

            title.setText(getString(R.string.title_invitation_friend));

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
                    addInvitationFriend(position);
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onRemoveClick(int position) {
                    removeInvitationFriend(position);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void removeInvitationFriend(int position) {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_friend", users.get(position));

        JSONController.getJsonArrayFromUrl(Constants.URL_DELETE_FIREND_INVITATION, this, params, new JSONArrayListener() {
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

}