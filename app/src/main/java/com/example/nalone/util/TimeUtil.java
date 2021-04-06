package com.example.nalone.util;

import android.util.Log;
import android.widget.TextView;

import com.example.nalone.enumeration.StatusEvent;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static SimpleDateFormat sdfToday = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    private static String currentDay = sdf.format(new Date(System.currentTimeMillis()));

   /* public boolean eventTermine(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date","startDate : " + startDate);
        Log.w("date","endDate : "+ endDate);
        Log.w("date","different : " + different);


        if(different < 0){
            return true;
        }else{
            return false;
        }
    }*/

    public static StatusEvent verifStatut(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date","startDate : " + startDate);
        Log.w("date","endDate : "+ endDate);
        Log.w("date","different : " + different);


        if(different <= 0 && different >= -7200000){
            return StatusEvent.ENCOURS;
        }
        if(different > 0){
            return StatusEvent.BIENTOT;
        }
        if(different < -7200000 && different > -86400000){
            return StatusEvent.FINI;
        }if(different <= -86400000){
            return StatusEvent.EXPIRE;
        }

        return null;
    }

    public static void differenceDate(Date startDate, Date endDate, TextView textView) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        Log.w("date",
                elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes +"m "+ elapsedSeconds+"secondes ");

        if(elapsedDays < 0 || elapsedHours < 0 || elapsedMinutes < 0 || elapsedSeconds < 0){
            if(elapsedDays < 0)
                elapsedDays = elapsedDays*-1;
            if(elapsedHours < 0 )
                elapsedHours = elapsedHours*-1;
            if(elapsedMinutes < 0)
                elapsedMinutes = elapsedMinutes*-1;
            if(elapsedSeconds < 0)
                elapsedSeconds = elapsedSeconds*-1;


        }
        textView.setText(elapsedDays + "j " + elapsedHours + "h " + elapsedMinutes +"m "+ elapsedSeconds+"s ");
    }

    public static String verifDay(String date){
        if(date.equalsIgnoreCase(currentDay)){
            return sdfToday.format(date);
        }else {
            return date;
        }
    }

    public static String cutString(String s, int length, int start){
        if(length > s.length()){
            return null;
        }

        String temp = "";

        int i = 0;
        if(start != -1){
            for(i=start; i<length+start; i++){
                temp += s.charAt(i);
            }
        }else{
            for(i=0; i<length; i++){
                temp += s.charAt(i);
            }
        }
        return temp;
    }

}
