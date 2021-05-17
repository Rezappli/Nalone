package com.example.nalone.signUpActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nalone.R;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    int activitiesImages[];
    String[] activitiesNames;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.activitiesImages = flags;
        this.activitiesNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return activitiesImages.length;
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
        icon.setImageResource(activitiesImages[i]);
        names.setText(activitiesNames[i]);
        return view;
    }
}
