package com.example.nalone.json;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.nalone.util.Constants;
import com.example.nalone.util.CryptoUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectCrypt extends JSONObject{

    public JSONObjectCrypt(){
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addParameter(String key, Object value) {
        try {
            value = CryptoUtils.encrypt(value.toString());
            this.put(key, value);
        }catch (Exception e){
            Log.w("Response", e.getMessage());
        }
    }
}
