package com.wwc.jajing.component;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.wwc.R;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.CustomRequest;
import com.wwc.jajing.interfaces.VolleyInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SAJEN on 15/11/16.
 */

public class GetJSONResponse {


    Context context;

    public GetJSONResponse (Context context){
        this.context =context;
    }

    public static void RequestJsonToServer(Context ctx,String url ,Map<String, String> params, final VolleyInterface callback){

        MySingleton.getInstance(ctx).getRequestQueue();

        System.out.println("params = " + new JSONObject(params));

        System.out.println("Constants.url+url = " + Constants.url+url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,Constants.url+url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("REQUEST_JSON_TO_SERVER", "response: " + response);
                            callback.onSuccessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("REQUEST_JSON_TO_SERVER", "Error: " + error);
                        callback.onErroeResponse(error);
                    }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.e("REQUEST_JSON_TO_SERVER", "jsObjRequest====: " + jsObjRequest);
        MySingleton.getInstance(ctx).addToRequestQueue(jsObjRequest);

    }


    public static void RequestJsonToServer(Context ctx,String url ,JSONObject params, final VolleyInterface callback){

        MySingleton.getInstance(ctx).getRequestQueue();

        System.out.println("params = " + params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,Constants.url+url,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("REQUEST_JSON_TO_SERVER", "response: " + response);
                        callback.onSuccessResponse(response);

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("REQUEST_JSON_TO_SERVER", "Error: " + error);

                callback.onErroeResponse(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(ctx).addToRequestQueue(jsObjRequest);

    }


    public static void RequestJsonToServerArray(Context ctx,String url ,JSONObject params,final VolleyInterface callback){

        MySingleton.getInstance(ctx).getRequestQueue();

        System.out.println("params = " + params);

        CustomRequest jsonArrayRequest = new CustomRequest(Request.Method.POST,Constants.url+url,params,new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {

                callback.onSuccessResponse(response);

                System.out.println("response = " + response);

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onErroeResponse(error);

                System.out.println("error = " + error);
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(ctx).addToRequestQueue(jsonArrayRequest);

    }

}
