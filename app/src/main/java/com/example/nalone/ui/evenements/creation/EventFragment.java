package com.example.nalone.ui.evenements.creation;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_FRAGMENT;

public class EventFragment extends Fragment {

    protected void sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment currentFragment) {
        Intent intent = new Intent(ACTION_RECEIVE_FRAGMENT);
        intent.putExtra("fragment", currentFragment.toString());
        LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(getContext());
        localBctMgr.sendBroadcast(intent);
    }
}