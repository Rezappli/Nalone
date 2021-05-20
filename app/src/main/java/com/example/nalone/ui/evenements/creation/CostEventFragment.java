package com.example.nalone.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.nalone.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;

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

        cardViewFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTaping) {
                    changeToFree();
                }
                isTaping = true;
                isFree = true;
            }
        });

        cardViewPaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTaping) {
                    changeToPaying();
                }
                isTaping = true;
                isFree = false;
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE_NEXT_CLICK);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverNextClick, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverNextClick);

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

    private final BroadcastReceiver receiverNextClick = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = textInputPrice.getText().toString();
            int finalValue = Integer.parseInt(value);
            if (!isTaping) {
                imageViewPaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_attach_money_24_error));
                imageViewFree.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_money_off_24_error));
            } else {
                if (!isFree) {
                    if (textInputPrice.getText().toString().matches("")) {
                        textInputPrice.setError(getResources().getString(R.string.required_field));
                    } else if (finalValue <= 0) {
                        textInputPrice.setError(getResources().getString(R.string.int_over_zero));
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

}