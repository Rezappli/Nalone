package com.example.nalone.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_FRAGMENT;
import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;

public class EventFragment extends Fragment {

    protected BroadcastReceiver receiverNextClick;

    protected void sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment currentFragment) {
        Intent intent = new Intent(ACTION_RECEIVE_FRAGMENT);
        intent.putExtra("fragment", currentFragment.toString());
        LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(getContext());
        localBctMgr.sendBroadcast(intent);
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
}