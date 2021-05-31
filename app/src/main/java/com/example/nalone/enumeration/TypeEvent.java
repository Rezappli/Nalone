package com.example.nalone.enumeration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.nalone.R;

import java.util.ArrayList;

public enum TypeEvent {
    GAME(R.string.event_game, R.drawable.event_game), GATHER(R.string.event_gather, R.drawable.event_gather),
    MULTIMEDIA(R.string.event_movie, R.drawable.event_movie), MUSIC(R.string.event_music, R.drawable.event_music),
    PARTY(R.string.event_party, R.drawable.event_party), SCIENCE(R.string.event_science, R.drawable.event_science),
    SHOP(R.string.event_shop, R.drawable.event_shop), SHOW(R.string.event_show, R.drawable.event_show),
    SPORT(R.string.event_sport, R.drawable.event_sport), ART(R.string.event_art, R.drawable.event_art),
    ANIMAL(R.string.event_animal, R.drawable.event_animal), BOOK(R.string.event_book, R.drawable.event_book),
    ENVIRONMENT(R.string.event_environment, R.drawable.event_environment), TECHNOLOGY(R.string.event_technology, R.drawable.event_technology),
    WELL_BEING(R.string.event_well_being, R.drawable.event_well_being), WORK(R.string.event_work, R.drawable.event_work),
    CAR(R.string.event_car, R.drawable.event_car), CONTEST(R.string.event_contest, R.drawable.event_contest),
    CONFERENCE(R.string.event_conference, R.drawable.event_conference), HEALTH(R.string.event_health, R.drawable.event_health);

    public int name, image;

    TypeEvent(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public static TypeEvent valueOfName(int name) {
        for (TypeEvent te : values()) {
            if (te.name == name) {
                return te;
            }
        }
        return null;
    }

    public static TypeEvent valueOfImage(int image) {
        for (TypeEvent te : values()) {
            if (te.image == image) {
                return te;
            }
        }
        return null;
    }

    public static int nameOfValue(TypeEvent typeEvent) {
        for (TypeEvent te : values()) {
            if (te.equals(typeEvent)) {
                return te.name;
            }
        }
        return 0;
    }

    public static String nameOfValue(TypeEvent typeEvent, Context context) {
        for (TypeEvent te : values()) {
            Log.w("TYPEVENTT", te.toString());
            if (te == typeEvent) {
                return context.getString(te.name);
            }
        }
        return "";
    }

    public static int imageOfValue(TypeEvent typeEvent) {
        for (TypeEvent te : values()) {
            if (te.equals(typeEvent)) {
                return te.image;
            }
        }
        return 0;
    }

    public static ArrayList<Drawable> listOfImages(Context context) {
        ArrayList<Drawable> images = new ArrayList<>();
        for (TypeEvent te : values()) {
            images.add(context.getResources().getDrawable(te.image));
        }
        return images;
    }

    public static ArrayList<String> listOfNames(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (TypeEvent te : values()) {
            names.add(context.getString(te.name));
        }
        return names;
    }

    public static ArrayList<Integer> listOfImages() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (TypeEvent te : values()) {
            integerArrayList.add(te.image);
        }

        return integerArrayList;
    }

    public static ArrayList<Integer> listOfNames() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (TypeEvent te : values()) {
            integerArrayList.add(te.image);
        }

        return integerArrayList;
    }
}
