package com.example.nalone.ui.recherche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nalone.R;

public class RechercheFragment extends Fragment {

    private RechercheViewModel rechercheViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.logo);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.logo);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("NoLonely");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        rechercheViewModel =
                ViewModelProviders.of(this).get(RechercheViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recherche, container, false);

        rechercheViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        return root;

    }
}