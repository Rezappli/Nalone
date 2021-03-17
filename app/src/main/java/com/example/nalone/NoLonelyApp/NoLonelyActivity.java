package com.example.nalone.NoLonelyApp;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.listeners.NoLonelyInterface;

public class NoLonelyActivity extends AppCompatActivity implements NoLonelyInterface {
    @Override
    public void createFragment() {}

    @Override
    public void onResume(){
        super.onResume();
        createFragment();
    }
}
