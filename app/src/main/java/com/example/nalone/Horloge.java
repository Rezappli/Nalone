package com.example.nalone;

import android.util.Log;

import java.util.Date;

public class Horloge {

    public boolean eventTermine(Date startDate, Date endDate) {
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
    }

    public StatusEvent verifStatut(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date","startDate : " + startDate);
        Log.w("date","endDate : "+ endDate);
        Log.w("date","different : " + different);


        if(different < 0 && different > -7200000){
            return StatusEvent.EN_COURS;
        }

        if(different > 7200000){
            return StatusEvent.BIENTOT;
        }
        if(different < -7200000){
            return StatusEvent.FINI;
        }else{
            return null;
        }
    }

}
