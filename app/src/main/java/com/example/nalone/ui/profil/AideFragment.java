package com.example.nalone.ui.profil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.nalone.QrCode;
import com.example.nalone.R;

import static com.example.nalone.util.Constants.USER;

public class AideFragment extends Fragment {

    private ImageView qr_code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_aide, container, false);


        return root;
    }
}