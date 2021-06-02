package com.nolonely.mobile.dialog;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static boolean isStart;
    public static String ACTION_RECEIVE_TIME = "ACTION_RECEIVE_TIME";
    public static String EXTRA_START_TIME = "EXTRA_START_TIME";
    public static String EXTRA_END_TIME = "EXTRA_END_TIME";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String Shour = hourOfDay + "";
        String Sminute = minute + "";

        if (Shour.length() == 1) {
            Shour = "0" + hourOfDay;
        }

        if (Sminute.length() == 1) {
            Sminute = "0" + minute;
        }
        
        String time = Shour + ":" + Sminute;
        if (isStart) {
            sendDateBroadcast(EXTRA_START_TIME, time);
        } else {
            sendDateBroadcast(EXTRA_END_TIME, time);
        }
    }

    private void sendDateBroadcast(String extra, String value) {
        Intent intent = new Intent(ACTION_RECEIVE_TIME);
        intent.putExtra(extra, value);
        LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(getContext());
        localBctMgr.sendBroadcast(intent);
    }

}