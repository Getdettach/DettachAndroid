package com.wwc.jajing.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by infmac1 on 16/11/16.
 */

public interface VolleyInterface {

    void onSuccessResponse(JSONObject result);
    void onErroeResponse(VolleyError result);
    void onSuccessResponse(JSONArray result);
}
