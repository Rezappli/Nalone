package com.example.nalone.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.example.nalone.ui.evenements.creation.CreateEventFragment;
import com.example.nalone.ui.evenements.creation.DateEventFragment;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    public static boolean isStart;
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
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        String Sday = day + "";
        String Smonth = month + "";
        if(Sday.length() == 1){
            Sday = "0"+day;
        }

        if(Smonth.length() == 1){
            Smonth = "0"+month;
        }

        if(isStart){
            DateEventFragment.eventStartDate.setText(Sday+"/"+Smonth+"/"+year);
        }else{
            DateEventFragment.eventEndDate.setText(Sday+"/"+Smonth+"/"+year);
        }
    }
}
