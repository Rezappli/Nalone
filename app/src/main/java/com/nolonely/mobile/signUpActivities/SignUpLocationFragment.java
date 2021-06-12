package com.nolonely.mobile.signUpActivities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.nolonely.mobile.R;
import com.nolonely.mobile.listeners.CreationFragmentListener;
import com.nolonely.mobile.objects.AddressSearch;
import com.nolonely.mobile.thread.SearchThread;


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
                if (t != null) {
                    if (t.isAlive()) {
                        t.interrupt();
                    }
                }
                t = new SearchThread(listView, s + "", getActivity());
                t.start();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AddressSearch ad = (AddressSearch) listView.getAdapter().getItem(position);
            pos = new LatLng(ad.getLatitude(), ad.getLongitude());
            city = ad.getCity();
            Log.w("Position", "Pos : " + pos.toString());
            Log.w("Position", "City : " + city.toString());
            inputCity.setText(ad.getAddress());
            listView.setVisibility(View.GONE);
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