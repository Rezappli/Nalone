package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nalone.listeners.CoreListener;
import com.example.nalone.R;

public class GroupeFragment extends Fragment implements CoreListener {


    public GroupeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.groupe_fragment, container, false);

        return root;
    }

    @Override
    public void onDataChangeListener() {
        //updateItems();
    }

    @Override
    public void onUpdateAdapter() {

    }
}