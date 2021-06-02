package com.nolonely.mobile.ui.evenements.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.nolonely.mobile.R;
import com.nolonely.mobile.dialog.LoadFragment;
import com.nolonely.mobile.objects.AddressSearch;
import com.nolonely.mobile.thread.SearchThread;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

public class AddressEventFragment extends EventFragment {

    private TextInputEditText event_adresse;
    private DialogFragment load;
    private LatLng pos = null;
    private SearchThread t;
    private String city;
    private ListView listView;

    public AddressEventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_adress_event, container, false);
        event_adresse = root.findViewById(R.id.eventAdress);
        listView = root.findViewById(R.id.listViewEvent);

        if (MainCreationEventActivity.currentEvent.getAddress() != null) {
            event_adresse.setText(MainCreationEventActivity.currentEvent.getAddress());
        }

        event_adresse.setOnClickListener(v -> {
            if (listView != null) {
                if (listView.getVisibility() == View.GONE) {
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });

        event_adresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (t != null) {
                    if (t.isAlive()) {
                        t.interrupt();
                    }
                }
                t = new SearchThread(listView, s + "", getActivity());
                t.start();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AddressSearch ad = (AddressSearch) listView.getAdapter().getItem(position);
            pos = new LatLng(ad.getLatitude(), ad.getLongitude());
            city = ad.getCity();
            event_adresse.setText(ad.getAddress());
            listView.setVisibility(View.GONE);
        });
        receiverNextClick = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context context, Intent intent) {
                try {

                    if (pos == null || city == null) {
                        event_adresse.setError(getActivity().getResources().getString(R.string.location_not_found));
                    } else if (event_adresse.getText().toString().matches("")) {
                        event_adresse.setError(getActivity().getResources().getString(R.string.required_field));
                    } else {
                        MainCreationEventActivity.currentEvent.setAddress(event_adresse.getText().toString());
                        MainCreationEventActivity.currentEvent.setCity(city);
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
        return root;
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
}