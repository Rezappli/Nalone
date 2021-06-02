package com.nolonely.mobile.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nolonely.mobile.R;
import com.nolonely.mobile.dialog.SelectDateFragment;
import com.nolonely.mobile.dialog.TimePickerFragment;
import com.nolonely.mobile.enumeration.StatusEvent;
import com.nolonely.mobile.util.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nolonely.mobile.dialog.SelectDateFragment.ACTION_RECEIVE_DATE;
import static com.nolonely.mobile.dialog.SelectDateFragment.EXTRA_END_DATE;
import static com.nolonely.mobile.dialog.SelectDateFragment.EXTRA_START_DATE;
import static com.nolonely.mobile.dialog.TimePickerFragment.ACTION_RECEIVE_TIME;
import static com.nolonely.mobile.dialog.TimePickerFragment.EXTRA_END_TIME;
import static com.nolonely.mobile.dialog.TimePickerFragment.EXTRA_START_TIME;
import static com.nolonely.mobile.ui.evenements.creation.MainCreationEventActivity.currentEvent;

public class DateEventFragment extends EventFragment {
    private TextView eventStartDate, eventStartHoraire, eventEndDate, eventEndHoraire;

    private final BroadcastReceiver receiverDate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.hasExtra(EXTRA_START_DATE)) {
                    eventStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
                } else {
                    eventEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
                }
            }
        }
    };

    private final BroadcastReceiver receiverTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.hasExtra(EXTRA_START_TIME)) {
                    eventStartHoraire.setText(intent.getStringExtra(EXTRA_START_TIME));
                } else {
                    eventEndHoraire.setText(intent.getStringExtra(EXTRA_END_TIME));
                }
            }
        }
    };

    public DateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_date_event, container, false);
        eventStartDate = root.findViewById(R.id.eventDate);
        eventEndDate = root.findViewById(R.id.eventEndDate);
        eventStartHoraire = root.findViewById(R.id.eventHoraire);
        eventEndHoraire = root.findViewById(R.id.eventEndHoraire);

        if (currentEvent.getStartDate() != null) {
            String tsStart = currentEvent.getStartDate().replaceAll("-", "-"); //convertion de date pour sql
            eventStartDate.setText(tsStart.substring(0, 10));
            eventStartHoraire.setText(TimeUtil.timeOfDate(currentEvent.getStartDate()));
        }
        if (currentEvent.getEndDate() != null) {
            String tsEnd = currentEvent.getEndDate().replaceAll("-", "/"); //convertion de date pour sql
            eventEndDate.setText(tsEnd.substring(0, 10));
            eventEndHoraire.setText(TimeUtil.timeOfDate(currentEvent.getStartDate()));
        }

        eventStartHoraire.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerFragment();
            TimePickerFragment.isStart = true;
            newFragment.show(getActivity().getSupportFragmentManager(), "DIALOG_TIME");
        });

        eventStartDate.setOnClickListener(v -> {
            DialogFragment newFragment = new SelectDateFragment();
            SelectDateFragment.isStart = true;
            newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
        });

        eventEndDate.setOnClickListener(v -> {
            DialogFragment newFragment = new SelectDateFragment();
            SelectDateFragment.isStart = false;
            newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
        });

        eventEndHoraire.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerFragment();
            TimePickerFragment.isStart = false;
            newFragment.show(getActivity().getSupportFragmentManager(), "DIALOG_TIME");
        });

        receiverNextClick = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context context, Intent intent) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                if (eventStartDate.getText().toString().equalsIgnoreCase("date")) {
                    eventStartDate.setError(getResources().getString(R.string.required_field));
                    return;
                } else {
                    eventStartDate.setError(null);
                }

                if (eventEndDate.getText().toString().equalsIgnoreCase("date")) {
                    eventEndDate.setError(getResources().getString(R.string.required_field));
                    return;
                } else {
                    eventEndDate.setError(null);
                }

                if (eventStartHoraire.getText().toString().equalsIgnoreCase("horaire")) {
                    eventStartHoraire.setError(getResources().getString(R.string.required_field));
                    return;
                } else {
                    eventStartHoraire.setError(null);
                }

                if (eventEndHoraire.getText().toString().equalsIgnoreCase("horaire")) {
                    eventEndHoraire.setError(getResources().getString(R.string.required_field));
                    return;
                } else {
                    eventEndHoraire.setError(null);
                }


                String tsStart = eventStartDate.getText().toString() + " " + eventStartHoraire.getText().toString();
                String tsEnd = eventEndDate.getText().toString() + " " + eventEndHoraire.getText().toString();
                StatusEvent seStart = null;
                try {
                    seStart = TimeUtil.verifStatut(new Date(), sdf.parse(tsStart));
                    StatusEvent seEnd = TimeUtil.verifStatut(new Date(), sdf.parse(tsEnd));
                    if (seStart == StatusEvent.FINI || seStart == StatusEvent.EXPIRE || seEnd == StatusEvent.FINI || seEnd == StatusEvent.EXPIRE) {
                        Toast.makeText(getContext(), getResources().getString(R.string.date_in_futur), Toast.LENGTH_SHORT).show();
                    } else {

                        tsStart = tsStart.replaceAll("/", "-"); //convertion de date pour sql
                        tsEnd = tsEnd.replaceAll("/", "-");

                        MainCreationEventActivity.currentEvent.setStartDate(tsStart);
                        MainCreationEventActivity.currentEvent.setEndDate(tsEnd);

                        sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.DATE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE_DATE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverDate, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ACTION_RECEIVE_TIME);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverTime, intentFilter1);
    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverDate);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverTime);

    }

    private String cutString(String s, int length, int start) {
        if (length > s.length()) {
            return null;
        }

        String temp = "";

        int i = 0;
        if (start != -1) {
            for (i = start; i < length + start; i++) {
                temp += s.charAt(i);
            }
        } else {
            for (i = 0; i < length; i++) {
                temp += s.charAt(i);
            }
        }
        return temp;

    }
}