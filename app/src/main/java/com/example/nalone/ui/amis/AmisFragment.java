package com.example.nalone.ui.amis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nalone.R;

public class AmisFragment extends Fragment {

    private AmisViewModel amisViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        amisViewModel =
                ViewModelProviders.of(this).get(AmisViewModel.class);
        View root = inflater.inflate(R.layout.fragment_amis, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        amisViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}