package com.nolonely.mobile.bdd.json;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.util.CryptoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JSONController {

    private static Gson gson = new Gson();

    public static void getJsonArrayFromUrl(@NonNull String url, @NonNull Context context,
                                           @NonNull JSONObject parameters, final JSONArrayListener listener) {
        Volley.newRequestQueue(context)
                .add(new JsonRequest<JSONArray>(Request.Method.POST,
                             url,
                             parameters.toString(),
                             new Response.Listener<JSONArray>() {
                                 @RequiresApi(api = Build.VERSION_CODES.O)
                                 @Override
                                 public void onResponse(JSONArray jsonArray) {
                                     if (listener != null) {
                                         try {
                                             listener.onJSONReceived(CryptoUtils.decryptArray(jsonArray));
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 }
                             }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError volleyError) {
                             if (listener != null) {
                                 Log.w("Response", "Error call from " + listener.getClass());
                                 listener.onJSONReceivedError(volleyError);
                             }
                         }
                     }) {
                         @Override
                         protected Map<String, String> getParams() throws AuthFailureError {
                             Map<String, String> params = new HashMap<String, String>();

                             return super.getParams();
                         }

                         @Override
                         protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
                             try {
                                 String jsonString = new String(networkResponse.data,
                                         HttpHeaderParser
                                                 .parseCharset(networkResponse.headers));
                                 return Response.success(new JSONArray(jsonString),
                                         HttpHeaderParser
                                                 .parseCacheHeaders(networkResponse));
                             } catch (UnsupportedEncodingException e) {
                                 return Response.error(new ParseError(e));
                             } catch (JSONException je) {
                                 return Response.error(new ParseError(je));
                             }
                         }
                     }
                ).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static void getJsonObjectFromUrl(@NonNull String url, @NonNull Context context,
                                            @NonNull JSONObject parameters, final JSONObjectListener listener) {
        Volley.newRequestQueue(context)
                .add(new JsonRequest<JSONObject>(Request.Method.POST,
                             url,
                             parameters.toString(),
                             new Response.Listener<JSONObject>() {
                                 @RequiresApi(api = Build.VERSION_CODES.O)
                                 @Override
                                 public void onResponse(JSONObject jsonObject) {
                                     if (listener != null) {
                                         listener.onJSONReceived(CryptoUtils.decryptObject(jsonObject));
                                     }
                                 }
                             }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError volleyError) {
                             if (listener != null) {
                                 listener.onJSONReceivedError(volleyError);
                             }
                         }
                     }) {
                         @Override
                         protected Map<String, String> getParams() throws AuthFailureError {
                             return super.getParams();
                         }

                         @Override
                         protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                             try {
                                 String jsonString = new String(networkResponse.data,
                                         HttpHeaderParser
                                                 .parseCharset(networkResponse.headers));
                                 return Response.success(new JSONObject(jsonString),
                                         HttpHeaderParser
                                                 .parseCacheHeaders(networkResponse));
                             } catch (UnsupportedEncodingException e) {
                                 return Response.error(new ParseError(e));
                             } catch (JSONException je) {
                                 return Response.error(new ParseError(je));
                             }
                         }
                     }
                );
    }

    public static Object convertJSONToObject(JSONObject s, Class c) {
        return gson.fromJson(s.toString(), c);
    }
}
