package com.example.nalone.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.nalone.R;
import com.example.nalone.dialog.LoadFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

import static com.example.nalone.ui.evenements.creation.MainCreationEventActivity.ACTION_RECEIVE_NEXT_CLICK;

public class AdressEventFragment extends EventFragment {

    private TextInputEditText event_adresse, event_city;
    private DialogFragment load;

    public AdressEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_adress_event, container, false);
        event_adresse = root.findViewById(R.id.eventAdress);
        event_city = root.findViewById(R.id.eventCity);

        if (MainCreationEventActivity.currentEvent.getAddress() != null) {
            event_adresse.setText(MainCreationEventActivity.currentEvent.getAddress());
        }
        if (MainCreationEventActivity.currentEvent.getCity() != null) {
            event_city.setText(MainCreationEventActivity.currentEvent.getCity());
        }

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


    private LatLng getLocationFromAddress(String strAddress) {
        showLoadDialog();
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 10);
            if (address == null) {
                return null;
            }

            if (address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                dismissLoadDialog();
                return null;
            }

        } catch (IOException ex) {
            dismissLoadDialog();
            ex.printStackTrace();
        }

        dismissLoadDialog();
        return p1;
    }

    private void showLoadDialog() {
        load = new LoadFragment();
        load.show(getActivity().getSupportFragmentManager(), "LOAD");
        load.setCancelable(false);
        Log.w("Dialog", "Show");
    }

    private void dismissLoadDialog() {
        load.dismiss();
        Log.w("Dialog", "Dismiss");
    }

    private final BroadcastReceiver receiverNextClick = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LatLng pos = getLocationFromAddress(event_adresse.getText().toString() + " " + event_city.getText().toString());
                if (pos == null) {
                    event_adresse.setError("Adresse introuvable");
                } else if (event_adresse.getText().toString().matches("")) {
                    event_adresse.setError("Champs obligatoire");
                } else if (event_city.getText().toString().matches("")) {
                    event_city.setError("Champs obligatoire");
                } else {
                    MainCreationEventActivity.currentEvent.setAddress(event_adresse.getText().toString());
                    MainCreationEventActivity.currentEvent.setCity(event_city.getText().toString());
                    MainCreationEventActivity.currentEvent.setLatitude(pos.latitude);
                    MainCreationEventActivity.currentEvent.setLongitude(pos.longitude);
                    sendFragmentBroadcast(MainCreationEventActivity.CurrentFragment.ADRESS);
                }

            } catch (Exception e) {
                Log.w("Response", e.getMessage());
                Toast.makeText(getContext(), getResources().getString(R.string.error_address), Toast.LENGTH_SHORT).show();
            }
        }
    };

}