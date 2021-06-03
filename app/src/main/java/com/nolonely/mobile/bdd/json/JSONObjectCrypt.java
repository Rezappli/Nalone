package com.nolonely.mobile.bdd.json;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nolonely.mobile.util.CryptoUtils;

import org.json.JSONObject;

public class JSONObjectCrypt extends JSONObject {

    public JSONObjectCrypt() {
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putCryptParameter(String key, Object value) {
        try {
            value = CryptoUtils.encrypt(value.toString());
            this.put(key, value);
        } catch (Exception e) {
            Log.w("Response", e.getMessage());
        }
    }
}
