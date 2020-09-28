package com.example.nalone.ui.evenements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nalone.R;
import com.google.android.gms.maps.OnMapReadyCallback;

public class EvenementsFragment extends Fragment {

    private EvenementsViewModel evenementsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("NoLonely");
        evenementsViewModel =
                ViewModelProviders.of(this).get(EvenementsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_evenements, container, false);

        evenementsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}