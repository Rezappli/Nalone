package com.example.nalone.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.nalone.R;
import com.example.nalone.enumeration.TypeEvent;

public class TypeEventObject {

    private Drawable mDrawable;
    private String mName;
    private TypeEvent mType;
    private Context context;

    public TypeEventObject(Drawable mDrawable, String mName, TypeEvent mType) {
        this.mDrawable = mDrawable;
        this.mName = mName;
        this.mType = mType;
    }

    public TypeEventObject(Context context) {
        this.context = context;
    }

    public TypeEventObject() {
    }

    public int getDrawableType(com.example.nalone.enumeration.TypeEvent te) {
        switch (te) {
            case ANIMAL:
                return R.drawable.event_animal;
            case WORK:
                return R.drawable.event_work;
            case WELL_BEING:
                return R.drawable.event_well_being;
            case HEALTH:
                return R.drawable.event_health;
            case BOOK:
                return R.drawable.event_book;
            case ENVIRONMENT:
                return R.drawable.event_environment;
            case ART:
                return R.drawable.event_art;
            case CAR:
                return R.drawable.event_car;
            case GAME:
                return R.drawable.event_game;
            case SHOP:
                return R.drawable.event_shop;
            case SHOW:
                return R.drawable.event_show;
            case MULTIMEDIA:
                return R.drawable.event_movie;
            case MUSIC:
                return R.drawable.event_music;
            case PARTY:
                return R.drawable.event_party;
            case SPORT:
                return R.drawable.event_sport;
            case GATHER:
                return R.drawable.event_gather;
            case SCIENCE:
                return R.drawable.event_science;
            case CONFERENCE:
                return R.drawable.event_conference;
            default:
                return 0;
        }
    }

    public String getTextType(com.example.nalone.enumeration.TypeEvent te) {
        switch (te) {
            case ANIMAL:
                return context.getResources().getString(R.string.event_animal);
            case WORK:
                return context.getResources().getString(R.string.event_work);
            case WELL_BEING:
                return context.getResources().getString(R.string.event_well_being);
            case HEALTH:
                return context.getResources().getString(R.string.event_health);
            case BOOK:
                return context.getResources().getString(R.string.event_book);
            case ENVIRONMENT:
                return context.getResources().getString(R.string.event_environment);
            case ART:
                return context.getResources().getString(R.string.event_art);
            case CAR:
                return context.getResources().getString(R.string.event_car);
            case GAME:
                return context.getResources().getString(R.string.event_game);
            case SHOP:
                return context.getResources().getString(R.string.event_shop);
            case SHOW:
                return context.getResources().getString(R.string.event_show);
            case MULTIMEDIA:
                return context.getResources().getString(R.string.event_movie);
            case MUSIC:
                return context.getResources().getString(R.string.event_music);
            case PARTY:
                return context.getResources().getString(R.string.event_party);
            case SPORT:
                return context.getResources().getString(R.string.event_sport);
            case GATHER:
                return context.getResources().getString(R.string.event_gather);
            case SCIENCE:
                return context.getResources().getString(R.string.event_science);
            case CONFERENCE:
                return context.getResources().getString(R.string.event_conference);
            default:
                return "Error";
        }
    }

    public String[] getListActivitiesName() {
        return new String[]{context.getResources().getString(R.string.event_animal),
                context.getResources().getString(R.string.event_animal),
                context.getResources().getString(R.string.event_well_being),
                context.getResources().getString(R.string.event_health),
                context.getResources().getString(R.string.event_book),
                context.getResources().getString(R.string.event_environment),
                context.getResources().getString(R.string.event_art),
                context.getResources().getString(R.string.event_car),
                context.getResources().getString(R.string.event_game),
                context.getResources().getString(R.string.event_technology),
                context.getResources().getString(R.string.event_shop),
                context.getResources().getString(R.string.event_show),
                context.getResources().getString(R.string.event_movie),
                context.getResources().getString(R.string.event_music),
                context.getResources().getString(R.string.event_party),
                context.getResources().getString(R.string.event_sport),
                context.getResources().getString(R.string.event_gather),
                context.getResources().getString(R.string.event_science),
                context.getResources().getString(R.string.event_conference)};
    }

    public int[] getListActivitiesImage() {
        return new int[]{R.drawable.event_animal,
                R.drawable.event_work,
                R.drawable.event_well_being,
                R.drawable.event_health,
                R.drawable.event_book,
                R.drawable.event_environment,
                R.drawable.event_art,
                R.drawable.event_car,
                R.drawable.event_game,
                R.drawable.event_technology,
                R.drawable.event_shop,
                R.drawable.event_show,
                R.drawable.event_movie,
                R.drawable.event_music,
                R.drawable.event_party,
                R.drawable.event_sport,
                R.drawable.event_gather,
                R.drawable.event_science,
                R.drawable.event_conference};
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public TypeEvent getmType() {
        return mType;
    }

    public void setmType(TypeEvent mType) {
        this.mType = mType;
    }


}
