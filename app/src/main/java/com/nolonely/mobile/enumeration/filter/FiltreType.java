package com.nolonely.mobile.enumeration.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nolonely.mobile.R;

import java.util.ArrayList;

public enum FiltreType {
    NULL(R.string.location_all, R.drawable.ic_baseline_all_inclusive_24),

    GAME(R.string.event_game, R.drawable.event_game_grey), GATHER(R.string.event_gather, R.drawable.event_gather_grey),

    MULTIMEDIA(R.string.event_movie, R.drawable.event_movie_grey), MUSIC(R.string.event_music, R.drawable.event_music_grey),

    PARTY(R.string.event_party, R.drawable.event_party_grey), SCIENCE(R.string.event_science, R.drawable.event_science_grey),

    SHOP(R.string.event_shop, R.drawable.event_shop_grey), SHOW(R.string.event_show, R.drawable.event_show_grey),

    SPORT(R.string.event_sport, R.drawable.event_sport_grey), ART(R.string.event_art, R.drawable.event_art_grey),

    ANIMAL(R.string.event_animal, R.drawable.event_animal_grey), BOOK(R.string.event_book, R.drawable.event_book_grey),

    ENVIRONMENT(R.string.event_environment, R.drawable.event_environment_grey), TECHNOLOGY(R.string.event_technology, R.drawable.event_technology_grey),

    WELL_BEING(R.string.event_well_being, R.drawable.event_well_being_grey), WORK(R.string.event_work, R.drawable.event_work_grey),

    CAR(R.string.event_car, R.drawable.event_car_grey), CONTEST(R.string.event_contest, R.drawable.event_contest_grey),

    CONFERENCE(R.string.event_conference, R.drawable.event_conference_grey), HEALTH(R.string.event_health, R.drawable.event_health_grey);

    public int name, image;

    FiltreType(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public static int indexOfValue(FiltreType ft) {
        for (int i = 0; i < values().length; i++) {
            if (ft.equals(values()[i])) {
                return i;
            }
        }
        return -1;
    }

    public static FiltreType valueOfName(int name) {
        for (FiltreType te : values()) {
            if (te.name == name) {
                return te;
            }
        }
        return null;
    }

    public static FiltreType valueOfImage(int image) {
        for (FiltreType te : values()) {
            if (te.image == image) {
                return te;
            }
        }
        return null;
    }

    public static int nameOfValue(FiltreType typeEvent) {
        for (FiltreType te : values()) {
            if (te.equals(typeEvent)) {
                return te.name;
            }
        }
        return 0;
    }

    public static String nameOfValue(FiltreType typeEvent, Context context) {
        for (FiltreType te : values()) {
            Log.w("TYPEVENTT", te.toString());
            if (te == typeEvent) {
                return context.getString(te.name);
            }
        }
        return "";
    }

    public static int imageOfValue(FiltreType typeEvent) {
        for (FiltreType te : values()) {
            if (te.equals(typeEvent)) {
                return te.image;
            }
        }
        return 0;
    }

    public static ArrayList<Drawable> listOfImages(Context context) {
        ArrayList<Drawable> images = new ArrayList<>();
        for (FiltreType te : values()) {
            images.add(context.getResources().getDrawable(te.image));
        }
        return images;
    }

    public static ArrayList<String> listOfNames(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (FiltreType te : values()) {
            names.add(context.getString(te.name));
        }
        return names;
    }

    public static ArrayList<Integer> listOfImages() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreType te : values()) {
            integerArrayList.add(te.image);
        }

        return integerArrayList;
    }

    public static ArrayList<Integer> listOfNames() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (FiltreType te : values()) {
            integerArrayList.add(te.image);
        }

        return integerArrayList;
    }
}
