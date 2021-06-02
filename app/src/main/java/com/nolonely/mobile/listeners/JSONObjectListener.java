package com.nolonely.mobile.listeners;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface JSONObjectListener {
    void onJSONReceived(JSONObject jsonObject);
    void onJSONReceivedError(VolleyError volleyError);
}
