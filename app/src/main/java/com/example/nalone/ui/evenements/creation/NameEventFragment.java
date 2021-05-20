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

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;

public class NameEventFragment extends EventFragment {
    private TextInputEditText event_name;
    private TextInputEditText event_resume;
    private final BroadcastReceiver receiverNextClick = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (event_name.getText().toString().matches("")) {
                    event_name.setError("Champs obligatoire");
                } else {
                    Log.w("Response", "Value :" + event_name.getText().toString());
                    MainCreationEventActivity.currentEvent.setName(event_name.getText().toString());
                    if (!event_resume.getText().toString().matches("")) {
                        MainCreationEventActivity.currentEvent.setDescription(event_resume.getText().toString());
                    }

                    sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.NAME);

                }
            }
        }
    };

    public NameEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_name_event, container, false);
        event_name = root.findViewById(R.id.eventName);
        event_resume = root.findViewById(R.id.eventResume);

        if (MainCreationEventActivity.currentEvent.getName() != null) {
            event_name.setText(MainCreationEventActivity.currentEvent.getName());
        }
        if (MainCreationEventActivity.currentEvent.getDescription() != null) {
            event_resume.setText(MainCreationEventActivity.currentEvent.getDescription());
        }

        return root;
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