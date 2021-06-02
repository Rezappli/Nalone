package com.nolonely.mobile.enumeration.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nolonely.mobile.R;

import java.util.ArrayList;

public enum FiltreDate {

    NULL(R.string.title_nothing, R.drawable.ic_baseline_event_24),
    TODAY(R.string.filter_date_today, R.drawable.ic_baseline_event_24),
    TOMORROW(R.string.filter_date_tomorrow, R.drawable.ic_baseline_event_24),
    WEEK(R.string.filter_date_week, R.drawable.ic_baseline_event_24),
    MONTH(R.string.filter_date_month, R.drawable.ic_baseline_event_24),
    OTHER(R.string.filter_date_choose, R.drawable.ic_baseline_event_24);

    int name, image;

    FiltreDate(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public static FiltreDate valueOfName(int name) {
        for (FiltreDate fd : values()) {
            if (fd.name == name) {
                return fd;
            }
        }
        return null;
    }

    public static int indexOfValue(FiltreDate ft) {
        for (int i = 0; i < values().length; i++) {
            if (ft.equals(values()[i])) {
                return i;
            }
        }
        return -1;
    }

    public static FiltreDate valueOfImage(int image) {
        for (FiltreDate fd : values()) {
            if (fd.image == image) {
                return fd;
            }
        }
        return null;
    }

    public static int nameOfValue(FiltreDate filtreDate) {
        for (FiltreDate fd : values()) {
            if (fd.equals(filtreDate)) {
                return fd.name;
            }
        }
        return 0;
    }

    public static String nameOfValue(FiltreDate filtreDate, Context context) {
        for (FiltreDate fd : values()) {
            Log.w("TYPEVENTT", fd.toString());
            if (fd == filtreDate) {
                return context.getString(fd.name);
            }
        }
        return "";
    }

    public static int imageOfValue(FiltreDate filtreDate) {
        for (FiltreDate fd : values()) {
            if (fd.equals(filtreDate)) {
                return fd.image;
            }
        }
        return 0;
    }

    public static ArrayList<Drawable> listOfImages(Context context) {
        ArrayList<Drawable> images = new ArrayList<>();
        for (FiltreDate fd : values()) {
            images.add(context.getResources().getDrawable(fd.image));
        }
        return images;
    }

    public static ArrayList<String> listOfNames(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (FiltreDate fd : values()) {
            names.add(context.getString(fd.name));
        }
        return names;
    }

    public static ArrayList<Integer> listOfImages() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreDate fd : values()) {
            integerArrayList.add(fd.image);
        }

        return integerArrayList;
    }

    public static ArrayList<Integer> listOfNames() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreDate fd : values()) {
            integerArrayList.add(fd.image);
        }

        return integerArrayList;
    }
}
