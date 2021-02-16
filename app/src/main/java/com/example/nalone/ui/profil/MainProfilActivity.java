package com.example.nalone.ui.profil;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.ImageView;

import com.example.nalone.R;

public class MainProfilActivity extends AppCompatActivity {
    public static ImageView buttonBack;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profil);
        buttonBack = findViewById(R.id.buttonBack);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_profil);
    }
}