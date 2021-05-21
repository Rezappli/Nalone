package com.example.nalone.listeners;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

public interface JSONArrayListener {
    void onJSONReceived(JSONArray jsonArray) throws JSONException;

    void onJSONReceivedError(VolleyError volleyError);
}
