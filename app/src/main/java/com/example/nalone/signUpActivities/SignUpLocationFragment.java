package com.example.nalone.signUpActivities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.nalone.R;
import com.example.nalone.listeners.CreationFragmentListener;
import com.example.nalone.objects.AddressSearch;
import com.example.nalone.thread.SearchThread;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;


public class SignUpLocationFragment extends SignUpFragment implements CreationFragmentListener {

    private TextInputEditText inputCity;
    private ListView listView;
    private LatLng pos = null;
    private String city = null;
    private SearchThread t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_sign_up_location;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputCity = view.findViewById(R.id.signupCountry);
        listView = view.findViewById(R.id.listView);

        inputCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView != null) {
                    if (listView.getVisibility() == View.GONE) {
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        inputCity.addTextChangedListener(new TextWatcher() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressSearch ad = (AddressSearch) listView.getAdapter().getItem(position);
                pos = new LatLng(ad.getLatitude(), ad.getLongitude());
                city = ad.getCity();
                inputCity.setText(ad.getAddress());
                listView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNextClicked() {
        if (pos == null || city == null) {
            inputCity.setError(getActivity().getResources().getString(R.string.location_not_found), customErrorDrawable);
            return;
        }
        Log.w("City", "City enter : " + city);
        user.setCity(city);
        user.setLatitude(pos.latitude);
        user.setLongitude(pos.longitude);

        SignUpMainActivity.listenerMain.onFragmentValidate(this);
    }
}