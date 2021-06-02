package com.example.nalone.enumeration.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.nalone.R;

import java.util.ArrayList;

public enum FiltreSort {
    PERTINENCE(R.string.filter_sort_pertinence, R.drawable.filtre_sort),
    DATE(R.string.filter_sort_date, R.drawable.filtre_sort),
    POPULAR(R.string.filter_sort_popular, R.drawable.filtre_sort),
    LOCATION(R.string.filter_sort_location, R.drawable.filtre_sort),
    PRICEASC(R.string.filter_sort_price_asc, R.drawable.filtre_sort),
    PRICEDESC(R.string.filter_sort_price_desc, R.drawable.filtre_sort);

    int name, image;

    FiltreSort(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public static FiltreSort valueOfName(int name) {
        for (FiltreSort fs : values()) {
            if (fs.name == name) {
                return fs;
            }
        }
        return null;
    }

    public static FiltreSort valueOfImage(int image) {
        for (FiltreSort fs : values()) {
            if (fs.image == image) {
                return fs;
            }
        }
        return null;
    }

    public static int nameOfValue(FiltreSort filtreSort) {
        for (FiltreSort fs : values()) {
            if (fs.equals(filtreSort)) {
                return fs.name;
            }
        }
        return 0;
    }

    public static String nameOfValue(FiltreSort filtreSort, Context context) {
        for (FiltreSort fs : values()) {
            if (fs == filtreSort) {
                return context.getString(fs.name);
            }
        }
        return "";
    }

    public static int indexOfValue(FiltreSort ft) {
        for (int i = 0; i < values().length; i++) {
            if (ft.equals(values()[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int imageOfValue(FiltreSort filtreSort) {
        for (FiltreSort fs : values()) {
            if (fs.equals(filtreSort)) {
                return fs.image;
            }
        }
        return 0;
    }

    public static ArrayList<Drawable> listOfImages(Context context) {
        ArrayList<Drawable> images = new ArrayList<>();
        for (FiltreSort fs : values()) {
            images.add(context.getResources().getDrawable(fs.image));
        }
        return images;
    }

    public static ArrayList<String> listOfNames(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (FiltreSort fs : values()) {
            names.add(context.getString(fs.name));
        }
        return names;
    }

    public static ArrayList<Integer> listOfImages() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreSort fs : values()) {
            integerArrayList.add(fs.image);
        }

        return integerArrayList;
    }

    public static ArrayList<Integer> listOfNames() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreSort fs : values()) {
            integerArrayList.add(fs.image);
        }

        return integerArrayList;
    }
}
