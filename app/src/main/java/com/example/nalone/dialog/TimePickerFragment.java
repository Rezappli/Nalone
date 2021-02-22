package com.example.nalone.dialog;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.nalone.ui.evenements.creation.CreateEventFragment;
import com.example.nalone.ui.evenements.creation.DateEventFragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static boolean isStart;

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
        String Shour = hourOfDay+"";
        String Sminute = minute+"";

        if(Shour.length() == 1){
            Shour = "0"+hourOfDay;
        }

        if(Sminute.length() == 1){
            Sminute = "0"+minute;
        }

        if (isStart){
            DateEventFragment.eventStartHoraire.setText(Shour + ":"+Sminute);
        }else{
            DateEventFragment.eventEndHoraire.setText(Shour + ":"+Sminute);
        }
    }

}