package com.example.nalone.enumeration.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.nalone.R;

import java.util.ArrayList;

public enum FiltreOwner {
    PUBLIC(R.string.location_public, R.drawable.ic_baseline_groups_icon),
    FRIEND(R.string.title_amis, R.drawable.ic_baseline_groups_icon);

    int name, image;

    FiltreOwner(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public static FiltreOwner valueOfName(int name) {
        for (FiltreOwner fs : values()) {
            if (fs.name == name) {
                return fs;
            }
        }
        return null;
    }

    public static FiltreOwner valueOfImage(int image) {
        for (FiltreOwner fs : values()) {
            if (fs.image == image) {
                return fs;
            }
        }
        return null;
    }

    public static int nameOfValue(FiltreOwner filtreOwner) {
        for (FiltreOwner fs : values()) {
            if (fs.equals(filtreOwner)) {
                return fs.name;
            }
        }
        return 0;
    }

    public static String nameOfValue(FiltreOwner filtreOwner, Context context) {
        for (FiltreOwner fs : values()) {
            if (fs == filtreOwner) {
                return context.getString(fs.name);
            }
        }
        return "";
    }

    public static int indexOfValue(FiltreOwner ft) {
        for (int i = 0; i < values().length; i++) {
            if (ft.equals(values()[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int imageOfValue(FiltreOwner filtreOwner) {
        for (FiltreOwner fs : values()) {
            if (fs.equals(filtreOwner)) {
                return fs.image;
            }
        }
        return 0;
    }

    public static ArrayList<Drawable> listOfImages(Context context) {
        ArrayList<Drawable> images = new ArrayList<>();
        for (FiltreOwner fs : values()) {
            images.add(context.getResources().getDrawable(fs.image));
        }
        return images;
    }

    public static ArrayList<String> listOfNames(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (FiltreOwner fs : values()) {
            names.add(context.getString(fs.name));
        }
        return names;
    }

    public static ArrayList<Integer> listOfImages() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreOwner fs : values()) {
            integerArrayList.add(fs.image);
        }

        return integerArrayList;
    }

    public static ArrayList<Integer> listOfNames() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreOwner fs : values()) {
            integerArrayList.add(fs.image);
        }

        return integerArrayList;
    }
}
