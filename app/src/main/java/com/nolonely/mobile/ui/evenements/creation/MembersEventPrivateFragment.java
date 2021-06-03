package com.nolonely.mobile.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.user.UserListAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.UserList;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.ui.UserListActivity;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.nolonely.mobile.ui.UserListActivity.ACTION_RECEIVE_USERS_LIST;
import static com.nolonely.mobile.ui.UserListActivity.EXTRA_BROADCAST_USERS_LIST;
import static com.nolonely.mobile.ui.UserListActivity.EXTRA_TYPE_LIST;
import static com.nolonely.mobile.ui.UserListActivity.EXTRA_USERS_LIST;
import static com.nolonely.mobile.ui.evenements.creation.MainCreationEventActivity.currentEvent;
import static com.nolonely.mobile.util.Constants.USER;

public class MembersEventPrivateFragment extends EventFragment {

    private ArrayList<User> adds;
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private Button buttonMoreInvit;
    private ImageView imageViewMemberFriend, imageViewMemberCustom;
    private LinearLayout linearCustomInvit;
    private boolean isClicked;
    private boolean isCustom;

    public MembersEventPrivateFragment() {
        // Required empty public constructor
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

        imageViewMemberCustom.setOnClickListener(v -> customClicked());

        imageViewMemberFriend.setOnClickListener(v -> friendsClicked());


        if (currentEvent.isFriendMembers() && currentEvent.getNbMembers() > 0) {
            friendsClicked();
        } else if (!currentEvent.isFriendMembers() && currentEvent.getNbMembers() != -1) {
            customClicked();
        }

        if (!currentEvent.getMembers().isEmpty() && !currentEvent.isFriendMembers()) {
            initRecylerView(currentEvent.getMembers());
        }

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

        receiverNextClick = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context context, Intent intent) {

                if (!isClicked) {
                    imageViewMemberCustom.setColorFilter(Color.RED);
                    imageViewMemberFriend.setColorFilter(Color.RED);
                    Toast.makeText(context, getString(R.string.error_lise_member_not_selected), Toast.LENGTH_SHORT).show();
                } else {
                    if (isCustom) {
                        if (adds != null && !adds.isEmpty()) {
                            currentEvent.setNbMembers(adds.size() + 1);
                            currentEvent.setLimitMembers(0);
                            currentEvent.setMembers(adds);
                            currentEvent.setFriendMembers(false);
                            sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.MEMBERS);
                        } else {
                            Toast.makeText(context, getString(R.string.error_lise_member_empty), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        JSONObjectCrypt params = new JSONObjectCrypt();
                        params.putCryptParameter("uid", USER.getUid());

                        JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS, getContext(), params, new JSONArrayListener() {
                            @Override
                            public void onJSONReceived(JSONArray jsonArray) throws JSONException {
                                if (jsonArray.length() > 0) {
                                    ArrayList<User> friends = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        User user = (User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class);
                                        friends.add(user);
                                    }
                                    currentEvent.setNbMembers(friends.size() + 1);
                                    currentEvent.setMembers(friends);
                                    currentEvent.setFriendMembers(true);
                                    sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.MEMBERS);
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
                    }
                }
            }
        };

        return root;
    }

    private void friendsClicked() {
        isClicked = true;
        isCustom = false;
        imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.grey));
        imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.colorPrimary));
        linearCustomInvit.setVisibility(View.INVISIBLE);
    }

    private void customClicked() {
        isClicked = true;
        isCustom = true;
        imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.colorPrimary));
        imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.grey));
        linearCustomInvit.setVisibility(View.VISIBLE);
    }


    private final BroadcastReceiver receiverListMembers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adds = new ArrayList<>();

            Bundle bundle = intent.getExtras();
            if (bundle != null)
                adds = (ArrayList<User>) bundle.getSerializable(EXTRA_BROADCAST_USERS_LIST);
            initRecylerView(adds);
            //mAdapter.notifyDataSetChanged();*/
        }
    };

    private void initRecylerView(ArrayList<User> list) {
        mAdapter = new UserListAdapter(list, UserList.MEMBERS);
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
    }

}