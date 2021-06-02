package com.nolonely.mobile.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.nolonely.mobile.R;
import com.google.android.material.textfield.TextInputEditText;

import static com.nolonely.mobile.ui.evenements.creation.MainCreationEventActivity.currentEvent;


public class MembersEventPublicFragment extends EventFragment {

    private TextInputEditText textInputEditNbMember;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members_event_public, container, false);

        textInputEditNbMember = view.findViewById(R.id.textInputEditNbMember);

        if (currentEvent.getLimitMembers() != -1) {
            textInputEditNbMember.setText(currentEvent.getLimitMembers());
        }

        receiverNextClick = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context context, Intent intent) {
                if (textInputEditNbMember.getText().toString().matches("")) {
                    textInputEditNbMember.setError(getString(R.string.error_limit_member_empty));
                } else {
                    currentEvent.setLimitMembers(Integer.parseInt(textInputEditNbMember.getText().toString()));
                    sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.MEMBERS);
                }
            }
        };
        return view;
    }


    ;
}