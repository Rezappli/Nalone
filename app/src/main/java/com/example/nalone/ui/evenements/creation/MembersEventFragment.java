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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.util.Constants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;
import static com.example.nalone.util.Constants.USER;

public class MembersEventFragment extends EventFragment {

    private List<String> adds = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Button buttonMoreInvit;
    private ImageView imageViewMemberFriend, imageViewMemberCustom;

    public MembersEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE_NEXT_CLICK);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverNextClick, intentFilter);
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
        mRecyclerView = root.findViewById(R.id.recyclerView1);
        buttonMoreInvit = root.findViewById(R.id.buttonMoreInvit);
        imageViewMemberFriend = root.findViewById(R.id.imageViewMemberFriend);
        imageViewMemberCustom = root.findViewById(R.id.imageViewMemberCustom);

        imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.grey));
        imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.grey));

        imageViewMemberCustom.setOnClickListener(v -> {
            imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.colorPrimary));
            imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.grey));
        });

        imageViewMemberFriend.setOnClickListener(v -> {
            imageViewMemberCustom.setColorFilter(getResources().getColor(R.color.grey));
            imageViewMemberFriend.setColorFilter(getResources().getColor(R.color.colorPrimary));
        });
        buttonMoreInvit.setOnClickListener(v -> {

            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());

            JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS, getContext(), params, new JSONArrayListener() {
                @Override
                public void onJSONReceived(JSONArray jsonArray) {
                    if (jsonArray.length() > 0) {
                        ListAmisFragment.type = "event";
                        ListAmisFragment.EVENT_LOAD = MainCreationEventActivity.currentEvent;
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


}