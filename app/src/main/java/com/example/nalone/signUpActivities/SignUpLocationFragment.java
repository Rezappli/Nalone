package com.example.nalone.signUpActivities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.nalone.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class SignUpLocationFragment extends SignUpFragment implements SignUpListener {

    private TextInputEditText inputCity;
    private TextInputEditText inputAdress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_sign_up_location;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Places.initialize(getContext(), "APKSdhsjsgdkKH1872");
        PlacesClient placesClient = Places.createClient(getContext());
        // Inflate the layout for this fragment
        inputCity = view.findViewById(R.id.signupCountry);

       /* inputCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutocompleteActivity();
            }
        });*/
    }

    public void startAutocompleteActivity() {
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID, Place.Field.NAME))
                .setTypeFilter(TypeFilter.ADDRESS)
                .setCountries(Arrays.asList("FR"))
                .build(getContext());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
        } else if (resultCode == RESULT_CANCELED) {
            Status status = Autocomplete.getStatusFromIntent(data);
        }


    }

    private LatLng getLocationFromAddress(String strAddress) {

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
                return null;
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    @Override
    public void onNextClicked() {
        String fieldCity = inputCity.getText().toString();

        LatLng pos = null;

        try {
            pos = getLocationFromAddress(fieldCity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pos == null) {
            inputCity.setError("Impossible de trouver ce lieu", customErrorDrawable);
            return;
        }
        user.setCity(fieldCity);
        user.setLatitude(pos.latitude);
        user.setLongitude(pos.longitude);

        SignUpMainActivity.listenerMain.onFragmentValidate(this);
    }
}