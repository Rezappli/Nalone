package com.example.nalone.signUpActivities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nalone.R;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Drawable> activitiesImages;
    ArrayList<String> activitiesNames;

    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, ArrayList<Drawable> images, ArrayList<String> names) {
        this.context = applicationContext;
        this.activitiesImages = images;
        this.activitiesNames = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return activitiesImages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item_activity, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageViewSpinner);
        TextView names = (TextView) view.findViewById(R.id.textViewSpinner);
        icon.setImageDrawable(activitiesImages.get(i));
        names.setText(activitiesNames.get(i));
        return view;
    }
}
