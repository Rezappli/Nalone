package com.example.nalone.ui.recherche;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nalone.R;
import com.google.android.gms.common.internal.SimpleClientAdapter;

import org.xmlpull.v1.XmlPullParser;

public class RechercheFragment extends Fragment {

    private RechercheViewModel rechercheViewModel;
    private LinearLayout layout_fond;
    private LinearLayout recherche_layout;

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

        layout_fond = root.findViewById(R.id.fond_layout);
        recherche_layout = root.findViewById(R.id.recherche_layout);

        recherche_layout.setVisibility(View.INVISIBLE);

        LinearLayout duplicate_layout = new LinearLayout(getContext());
        duplicate_layout.setLayoutParams(recherche_layout.getLayoutParams());

        CardView firstCardView = new CardView(getContext());
        firstCardView.setLayoutParams(recherche_layout.getChildAt(0).getLayoutParams());
        duplicate_layout.addView(firstCardView);

        LinearLayout linearCardView = new LinearLayout(getContext());
        linearCardView.setLayoutParams(recherche_layout.getChildAt(1).getLayoutParams());
        firstCardView.addView(linearCardView);

        CardView secondCardView = new CardView(getContext());
        secondCardView.setLayoutParams(recherche_layout.getChildAt(2).getLayoutParams());
        linearCardView.addView(secondCardView);

        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.logo);
        secondCardView.addView(image);

        duplicate_layout.addView(firstCardView);

        layout_fond.addView(duplicate_layout);





        rechercheViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        return root;

    }
}