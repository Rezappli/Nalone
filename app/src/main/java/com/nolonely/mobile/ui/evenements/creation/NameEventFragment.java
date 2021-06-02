package com.nolonely.mobile.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.nolonely.mobile.R;
import com.google.android.material.textfield.TextInputEditText;

public class NameEventFragment extends EventFragment {
    private TextInputEditText event_name;
    private TextInputEditText event_resume;

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
        receiverNextClick = new BroadcastReceiver() {
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

        return root;
    }
}