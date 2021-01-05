package com.example.nalone.util;

import android.util.Log;

import com.example.nalone.enumeration.StatusEvent;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Horloge {

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

    public static String verifDay(Timestamp tp){
        String date = sdf.format(tp.toDate());
        if(date.equalsIgnoreCase(currentDay)){
            return sdfToday.format(tp.toDate());
        }else {
            return date;
        }
    }

}
