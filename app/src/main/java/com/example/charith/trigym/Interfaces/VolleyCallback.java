package com.example.charith.trigym.Interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyCallback {

    void onSuccess(JSONObject response);

    void onSuccess(JSONArray response);

    void onError(String error);
}
