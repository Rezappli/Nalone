package com.example.nalone.json;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.google.gson.Gson;

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
                                 @Override
                                 public void onResponse(JSONArray jsonArray) {
                                     if (listener != null) {
                                         listener.onJSONReceived(jsonArray);
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
                             Map<String, String> params = new HashMap<String, String>();
                             params.put("uid", "09cbc003-078b-4950-b0d8-c311e069cacf");

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
                );
    }

    public static void getJsonObjectFromUrl(@NonNull String url, @NonNull Context context,
                                            @NonNull JSONObject parameters, final JSONObjectListener listener){
        Volley.newRequestQueue(context)
                .add(new JsonRequest<JSONObject>(Request.Method.POST,
                             url,
                             parameters.toString(),
                             new Response.Listener<JSONObject>() {
                                 @Override
                                 public void onResponse(JSONObject jsonObject) {
                                     if (listener != null) {
                                         listener.onJSONReceived(jsonObject);
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
                             Map<String, String> params = new HashMap<String, String>();
                             params.put("uid", "09cbc003-078b-4950-b0d8-c311e069cacf");

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

    public static Object convertJSONToObject(JSONObject s, Class c){
        return gson.fromJson(s.toString(), c);
    }
}
