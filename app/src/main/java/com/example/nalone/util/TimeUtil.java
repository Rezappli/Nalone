package com.example.nalone.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TextView;

import com.example.nalone.R;
import com.example.nalone.enumeration.StatusEvent;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static SimpleDateFormat sdfHoursMinutes = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sdfDayMonthYear = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String currentDay = sdfDayMonthYear.format(new Date(System.currentTimeMillis()));

    public static StatusEvent verifStatut(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.w("date", "startDate : " + startDate);
        Log.w("date", "endDate : " + endDate);
        Log.w("date", "different : " + different);


        if (different <= 0 && different >= -7200000) {
            return StatusEvent.ENCOURS;
        }
        if (different > 0) {
            return StatusEvent.BIENTOT;
        }
        if (different < -7200000 && different > -86400000) {
            return StatusEvent.FINI;
        }
        if (different <= -86400000) {
            return StatusEvent.EXPIRE;
        }

        return null;
    }

    @SuppressLint("SetTextI18n")
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
                elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + "secondes ");

        if (elapsedDays < 0 || elapsedHours < 0 || elapsedMinutes < 0 || elapsedSeconds < 0) {
            if (elapsedDays < 0)
                elapsedDays = elapsedDays * -1;
            if (elapsedHours < 0)
                elapsedHours = elapsedHours * -1;
            if (elapsedMinutes < 0)
                elapsedMinutes = elapsedMinutes * -1;
            if (elapsedSeconds < 0)
                elapsedSeconds = elapsedSeconds * -1;


        }
        textView.setText(elapsedDays + "j " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + "s ");
    }

    /**
     * Methode permettant d'obtenir la meilleur annotation possible pour la date
     * Soit l'heure si la notification date d'aujourd'hui
     * Soit Hier à + heure de la notification
     * Soit la date sous le forme dd-MM-yyyy
     *
     * @param date
     * @return
     */
    public static String getBestDateAnnotation(String date) {
        try {
            Date dateEnterFull = fullSdf.parse(date);
            Date dateEnter = sdfDayMonthYear.parse(date);
            Date dateYestearday = new DateTime(new Date(System.currentTimeMillis())).minusDays(1).toDate();

            String dateEnterFormated = sdfDayMonthYear.format(dateEnter);
            String dateYeasterdayFormated = sdfDayMonthYear.format(dateYestearday);

            if (dateEnterFormated.equalsIgnoreCase(currentDay)) {
                return sdfHoursMinutes.format(dateEnterFull);
            } else if (dateEnterFormated.equalsIgnoreCase(dateYeasterdayFormated)) {
                return Constants.application.getResources().getString(R.string.notifications_yesterday) + " " + sdfHoursMinutes.format(dateEnterFull);
            } else {
                return dateEnterFormated;
            }
        } catch (ParseException e) {
            Log.w("Response", "Une erreur est survenue");
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Methode permettant de couper un String pour obtenir un resultat
     *
     * @param s
     * @param length
     * @param start
     * @return
     */
    public static String cutString(String s, int length, int start) {
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
