package com.nolonely.mobile.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TextView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.enumeration.StatusEvent;

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

    public static Date dateOfString(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }

    public static String dateOfDateLetter(String mDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        String final_date_text = "";
        Date date = sdf.parse(mDate);
        String date_text = Constants.formatD.format(date);
        for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }

        return final_date_text;
    }

    public static String dateOfDateNumber(String mDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");

        Date date = sdf.parse(mDate);
        return sdfDate.format(date);
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

    public static String timeOfDate(String s) {
        if (5 > s.length()) {
            return null;
        }

        StringBuilder temp = new StringBuilder();

        int i = 0;
        if (11 != -1) {
            for (i = 11; i < 5 + 11; i++) {
                temp.append(s.charAt(i));
            }
        } else {
            for (i = 0; i < 5; i++) {
                temp.append(s.charAt(i));
            }
        }
        return temp.toString();
    }

}
