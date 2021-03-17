package com.example.nalone.NoLonelyApp;

import androidx.fragment.app.Fragment;

import com.example.nalone.listeners.NoLonelyInterface;

public class NoLonelyFragment extends Fragment implements NoLonelyInterface {
    @Override
    public void createFragment() {}

    @Override
    public void onResume(){
        super.onResume();
        createFragment();
    }
}
