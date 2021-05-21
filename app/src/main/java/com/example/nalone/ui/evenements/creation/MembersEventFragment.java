package com.example.nalone.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.example.nalone.ui.UserListActivity;
import com.example.nalone.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.nalone.ui.UserListActivity.EXTRA_BROADCAST_USERS_LIST;
import static com.example.nalone.ui.UserListActivity.EXTRA_TYPE_LIST;
import static com.example.nalone.ui.UserListActivity.EXTRA_USERS_LIST;
import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;
import static com.example.nalone.util.Constants.USER;

public class MembersEventFragment extends EventFragment {

    private List<User> adds;
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private Button buttonMoreInvit;
    private ImageView imageViewMemberFriend, imageViewMemberCustom;
    private LinearLayout linearCustomInvit;

    public MembersEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ACTION_RECEIVE_NEXT_CLICK);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverNextClick, intentFilter1);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverNextClick);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_members_event, container, false);

        linearCustomInvit = root.findViewById(R.id.linearCustomInvit);
        mRecyclerView = root.findViewById(R.id.recyclerView1);
        buttonMoreInvit = root.findViewById(R.id.buttonMoreInvit);
        imageViewMemberFriend = root.findViewById(R.id.imageViewMemberFriend);
        imageViewMemberCustom = root.findViewById(R.id.imageViewMemberCustom);

        if (adds != null) {
            mAdapter = new UserListAdapter(this.adds, UserList.MEMBERS);
            mAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                @Override
                public void onDisplayClick(int position) {

                }

                @Override
                public void onAddClick(int position) {

                }

                @Override
                public void onRemoveClick(int position) {

                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }


        imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.grey));
        imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.grey));

        imageViewMemberCustom.setOnClickListener(v -> {
            imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.colorPrimary));
            imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.grey));
            linearCustomInvit.setVisibility(View.VISIBLE);
        });

        imageViewMemberFriend.setOnClickListener(v -> {
            imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.grey));
            imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.colorPrimary));
            linearCustomInvit.setVisibility(View.INVISIBLE);
        });
        buttonMoreInvit.setOnClickListener(v -> {

            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());

            JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS, getContext(), params, new JSONArrayListener() {
                @Override
                public void onJSONReceived(JSONArray jsonArray) throws JSONException {
                    if (jsonArray.length() > 0) {
                        ArrayList<User> friends = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            friends.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                        }
                        Intent intent = new Intent(getContext(), UserListActivity.class);
                        intent.putExtra(EXTRA_USERS_LIST, friends);
                        intent.putExtra(EXTRA_TYPE_LIST, UserList.INVIT_EVENT.toString());
                        startActivityForResult(intent, 1);
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_friends), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Log.w("Response", "Erreur:" + volleyError.toString());
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });
        });
        return root;
    }


    private final BroadcastReceiver receiverNextClick = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            MainCreationEventActivity.currentEvent.setNbMembers(adds.size() + 1);
            sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.MEMBERS);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("MEMBER", "ACTIVITY RESULT");
        if (resultCode == RESULT_OK) {
            adds = new ArrayList<>();
            adds = (ArrayList<User>) data.getSerializableExtra(EXTRA_BROADCAST_USERS_LIST);
            mAdapter.notifyDataSetChanged();
            Log.w("MEMBER", "" + adds.size());
        }
    }

}