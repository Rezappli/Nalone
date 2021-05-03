package com.example.nalone.listeners;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface JSONArrayListener {
    void onJSONReceived(JSONArray jsonArray);
    void onJSONReceivedError(VolleyError volleyError);
}
