package com.example.nalone.thread;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.widget.ListView;

import com.example.nalone.adapter.AddressSearchAdapter;
import com.example.nalone.objects.AddressSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchThread extends Thread {

    private String search;
    private ListView listView;
    private Activity activity;

    private Geocoder coder;

    public SearchThread(ListView listView, String searchString, Activity activity) {
        this.search = searchString;
        this.activity = activity;
        this.listView = listView;
    }


    @Override
    public void run() {
        if (listView != null) {
            List<AddressSearch> list = getLocationFromAddress(search);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (listView != null) {
                        listView.setAdapter(null);
                        if (list != null) {
                            listView.setAdapter(new AddressSearchAdapter(activity, list));
                        }
                    }
                }
            });
        }
    }

    private List<AddressSearch> getLocationFromAddress(String strAddress) {
        if (coder == null) {
            coder = new Geocoder(activity);
        }
        List<Address> address = null;
        List<AddressSearch> addressSearch = null;

        try {
            address = coder.getFromLocationName(strAddress, 10);
            if (address == null) {
                return null;
            }

            if (address.size() > 0) {
                addressSearch = new ArrayList<>();
                for (Address a : address) {
                    addressSearch.add(new AddressSearch(a.getAddressLine(0), a.getLatitude(), a.getLongitude(), a.getLocality()));
                }
            } else {
                return null;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return addressSearch;
    }
}
