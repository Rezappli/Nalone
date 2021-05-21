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

import static com.example.nalone.ui.UserListActivity.ACTION_RECEIVE_USERS_LIST;
import static com.example.nalone.ui.UserListActivity.EXTRA_BROADCAST_USERS_LIST;
import static com.example.nalone.ui.UserListActivity.EXTRA_TYPE_LIST;
import static com.example.nalone.ui.UserListActivity.EXTRA_USERS_LIST;
import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;
import static com.example.nalone.util.Constants.USER;

public class MembersEventPrivateFragment extends EventFragment {

    private List<User> adds;
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private Button buttonMoreInvit;
    private ImageView imageViewMemberFriend, imageViewMemberCustom;
    private LinearLayout linearCustomInvit;

    public MembersEventPrivateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.w("MEMBER", "REGISTER");
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ACTION_RECEIVE_NEXT_CLICK);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverNextClick, intentFilter1);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverNextClick);
        //getActivity().unregisterReceiver(receiverListMembers);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_members_private_event, container, false);
        getActivity().registerReceiver(receiverListMembers, new IntentFilter(ACTION_RECEIVE_USERS_LIST));

        linearCustomInvit = root.findViewById(R.id.linearCustomInvit);
        mRecyclerView = root.findViewById(R.id.recyclerView1);
        buttonMoreInvit = root.findViewById(R.id.buttonMoreInvit);
        imageViewMemberFriend = root.findViewById(R.id.imageViewMemberFriend);
        imageViewMemberCustom = root.findViewById(R.id.imageViewMemberCustom);

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
                            User user = (User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class);
                            if (adds == null || adds.isEmpty() || !adds.get(i).getUid().equals(user.getUid())) {
                                friends.add(user);
                            }
                        }
                        if (!friends.isEmpty()) {
                            Intent intent = new Intent(getContext(), UserListActivity.class);
                            intent.putExtra(EXTRA_USERS_LIST, friends);
                            intent.putExtra(EXTRA_TYPE_LIST, UserList.INVIT_EVENT.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.no_more_friends), Toast.LENGTH_SHORT).show();
                        }

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

    private final BroadcastReceiver receiverListMembers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adds = new ArrayList<>();

            Bundle bundle = intent.getExtras();
            if (bundle != null)
                adds = (ArrayList<User>) bundle.getSerializable(EXTRA_BROADCAST_USERS_LIST);
            mAdapter = new UserListAdapter(adds, UserList.MEMBERS);
            mAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                @Override
                public void onDisplayClick(int position) {

                }

                @Override
                public void onAddClick(int position) {

                }

                @Override
                public void onRemoveClick(int position) {
                    adds.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //mAdapter.notifyDataSetChanged();*/
        }
    };

}