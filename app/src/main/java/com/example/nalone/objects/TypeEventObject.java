package com.example.nalone.objects;

import android.graphics.drawable.Drawable;

import com.example.nalone.enumeration.TypeEvent;

public class TypeEventObject {

    private Drawable mDrawable;
    private String mName;
    private TypeEvent mType;

    public TypeEventObject(Drawable mDrawable, String mName, TypeEvent mType) {
        this.mDrawable = mDrawable;
        this.mName = mName;
        this.mType = mType;
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
