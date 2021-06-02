package com.nolonely.mobile.enumeration.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.nolonely.mobile.R;

import java.util.ArrayList;

public enum FiltrePrice {
    FREE(R.string.free, R.drawable.ic_baseline_attach_money_24),
    LOW(R.string.price_low, R.drawable.ic_baseline_attach_money_24),
    MEDIUM(R.string.price_medium, R.drawable.ic_baseline_attach_money_24),
    HIGH(R.string.price_high, R.drawable.ic_baseline_attach_money_24);

    int name, image;

    FiltrePrice(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public static FiltrePrice valueOfName(int name) {
        for (FiltrePrice fs : values()) {
            if (fs.name == name) {
                return fs;
            }
        }
        return null;
    }

    public static FiltrePrice valueOfImage(int image) {
        for (FiltrePrice fs : values()) {
            if (fs.image == image) {
                return fs;
            }
        }
        return null;
    }

    public static int nameOfValue(FiltrePrice filtreOwner) {
        for (FiltrePrice fs : values()) {
            if (fs.equals(filtreOwner)) {
                return fs.name;
            }
        }
        return 0;
    }

    public static String nameOfValue(FiltrePrice filtreOwner, Context context) {
        for (FiltrePrice fs : values()) {
            if (fs == filtreOwner) {
                return context.getString(fs.name);
            }
        }
        return "";
    }

    public static int indexOfValue(FiltrePrice ft) {
        for (int i = 0; i < values().length; i++) {
            if (ft.equals(values()[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int imageOfValue(FiltrePrice filtreOwner) {
        for (FiltrePrice fs : values()) {
            if (fs.equals(filtreOwner)) {
                return fs.image;
            }
        }
        return 0;
    }

    public static ArrayList<Drawable> listOfImages(Context context) {
        ArrayList<Drawable> images = new ArrayList<>();
        for (FiltrePrice fs : values()) {
            images.add(context.getResources().getDrawable(fs.image));
        }
        return images;
    }

    public static ArrayList<String> listOfNames(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (FiltrePrice fs : values()) {
            names.add(context.getString(fs.name));
        }
        return names;
    }

    public static ArrayList<Integer> listOfImages() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltrePrice fs : values()) {
            integerArrayList.add(fs.image);
        }

        return integerArrayList;
    }

    public static ArrayList<Integer> listOfNames() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltrePrice fs : values()) {
            integerArrayList.add(fs.image);
        }

        return integerArrayList;
    }
}
