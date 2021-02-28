package com.example.nalone.listeners;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface JSONArrayListener {
    void onJSONReceived(JSONArray jsonArray);
    void onJSONReceivedError(VolleyError volleyError);
}
