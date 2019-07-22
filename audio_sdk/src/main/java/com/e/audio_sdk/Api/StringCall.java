package com.e.audio_sdk.Api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class StringCall {
    private API mApi;
    private Context context;

    public StringCall(Context context) {
        this.mApi = API.getInstance(context);
        this.context = context;
    }

    public void get(String url, Map params, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        String tag = mApi.getTag(url, "get");
        if (null != params) {
            url = mApi.buildParams(url, params);
        }

        Response.Listener<String> respListener = handleAuth(listener);

        StringRequest request = new StringRequest(Request.Method.GET, url, respListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map map = new HashMap();
                map.put("Accept", "application/json");
                if (API.isLoggedIn(mApi.getContext())) {
                    Map<String, String> credentials = API.getCredentials(mApi.getContext());
                    String token = credentials.get(API.SESSION_ID);
                    map.put("sessionid", token);
                    //map.put("Authorization", token);
                    Log.d("SESSION_ID", token);
                }

                //map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
        };
        request.setTag(tag);
        mApi.getRequestQueue().add(request);
    }

    public void post(String url, final Map params, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        Response.Listener<String> respListener = handleAuth(listener);

        StringRequest request = new StringRequest(Request.Method.POST, url, respListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map map = new HashMap();
                map.put("Accept", "application/json");
                if (API.isLoggedIn(mApi.getContext())) {
                    Map<String, String> credentials = API.getCredentials(mApi.getContext());
                    String token = credentials.get(API.SESSION_ID);
                    map.put("sessionid", token);
                    //map.put("Authorization", token);
                    Log.d("SESSION_ID", token);
                }
                //map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        String tag = mApi.getTag(url, "post");
        request.setTag(tag);
        mApi.getRequestQueue().add(request);
    }

    public void put(String url, final Map params, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        Response.Listener<String> respListener = handleAuth(listener);
        StringRequest request = new StringRequest(Request.Method.PUT, url, respListener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        String tag = mApi.getTag(url, "put");
        request.setTag(tag);
        mApi.getRequestQueue().add(request);
    }

    public void delete(String url, Map params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String tag = mApi.getTag(url, "delete");
        if (null != params) {
            url = mApi.buildParams(url, params);
        }

        Response.Listener<String> respListener = handleAuth(listener);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, respListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map map = new HashMap();
                map.put("Accept", "application/json");
                if (API.isLoggedIn(mApi.getContext())) {
                    Map<String, String> credentials = API.getCredentials(mApi.getContext());
                    String token = credentials.get(API.SESSION_ID);
                    map.put("sessionid", token);
                    //map.put("Authorization", token);
                    Log.d("SESSION_ID", token);
                }
                //map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag(tag);
        mApi.getRequestQueue().add(request);
    }

    private Response.Listener<String> handleAuth(Response.Listener listener) {
        return response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.has("code") && obj.getInt("code") == 40) {
//                    ToastUtil.showLong(context, "Sorry you need to sign in again");
//                    API.logout(context);
                }else {
                    listener.onResponse(response);
                }
            }catch (JSONException e) {
                log(e.getMessage());
            }
        };
    }

    private void log(String log) {
        Log.d("StringCall ", log);
    }
}
