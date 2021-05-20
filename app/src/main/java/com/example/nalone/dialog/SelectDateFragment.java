package com.example.nalone.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static boolean isStart;
    public static String ACTION_RECEIVE_DATE = "ACTION_RECEIVE_DATE";
    public static String EXTRA_START_DATE = "EXTRA_START_DATE";
    public static String EXTRA_END_DATE = "EXTRA_END_DATE";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);
        datePicker.getDatePicker().setMinDate(now);
        return datePicker;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
        String Sday = day + "";
        String Smonth = month + "";
        if (Sday.length() == 1) {
            Sday = "0" + day;
        }

        if (Smonth.length() == 1) {
            Smonth = "0" + month;
        }


        String date = Sday + "/" + Smonth + "/" + year;
        if (isStart) {
            sendDateBroadcast(EXTRA_START_DATE, date);
        } else {
            sendDateBroadcast(EXTRA_END_DATE, date);
        }
    }

    private void sendDateBroadcast(String extra, String value) {
        Intent intent = new Intent(ACTION_RECEIVE_DATE);
        intent.putExtra(extra, value);
        LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(getContext());
        localBctMgr.sendBroadcast(intent);
    }
}
