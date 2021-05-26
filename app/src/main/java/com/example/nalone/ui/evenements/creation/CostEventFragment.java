package com.example.nalone.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.currentEvent;

public class CostEventFragment extends EventFragment {
    private CardView cardViewPaying;
    private CardView cardViewFree;
    private ImageView imageViewPaying, imageViewFree;
    private boolean isTaping, isFree;
    private TextInputEditText textInputPrice;
    private TextInputLayout textInputLayoutPrice;


    public CostEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cost_event, container, false);
        cardViewFree = root.findViewById(R.id.cardViewFree);
        cardViewPaying = root.findViewById(R.id.cardViewPaying);
        imageViewPaying = root.findViewById(R.id.imageViewPaying);
        imageViewFree = root.findViewById(R.id.imageViewFree);
        textInputPrice = root.findViewById(R.id.textInputPrice);
        textInputLayoutPrice = root.findViewById(R.id.textInputLayoutPrice);


        if (currentEvent.getPrice() == 0) {
            changeToFree();
            isTaping = true;
            isFree = false;
        } else if (currentEvent.getPrice() != -1) {
            changeToPaying();
            isTaping = true;
            isFree = false;
            textInputPrice.setText(String.valueOf(currentEvent.getPrice()));

        }


        cardViewFree.setOnClickListener(v -> {
            changeToFree();
            isTaping = true;
            isFree = true;
        });

        cardViewPaying.setOnClickListener(v -> {
            changeToPaying();
            isTaping = true;
            isFree = false;
        });
        receiverNextClick = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int finalValue;
                String value = textInputPrice.getText().toString();
                if (!value.matches("")) {
                    finalValue = Integer.parseInt(value);
                } else {
                    finalValue = 0;
                }
                if (!isTaping) {
                    imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24_error));
                    imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24_error));
                } else {
                    if (!isFree) {
                        if (textInputPrice.getText().toString().matches("")) {
                            textInputPrice.setError(getResources().getString(R.string.required_field));
                            return;
                        } else if (finalValue <= 0) {
                            textInputPrice.setError(getResources().getString(R.string.int_over_zero));
                            return;
                        } else {
                            MainCreationEventActivity.currentEvent.setPrice(finalValue);
                        }
                    } else {
                        MainCreationEventActivity.currentEvent.setPrice(0);
                    }
                    sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.COST);
                }
            }
        };
        return root;
    }

    private void changeToPaying() {
        imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24));
        imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24_white));
        textInputLayoutPrice.setVisibility(View.VISIBLE);
    }

    private void changeToFree() {
        imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24_white));
        imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24));
        textInputLayoutPrice.setVisibility(View.INVISIBLE);
    }
}