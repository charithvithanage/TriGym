package com.example.charith.trigym.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.charith.trigym.Config;
import com.example.charith.trigym.Entities.HealthCondition;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HealthConditionService {
    private static final String TAG="TriGym";

    Gson gson = new Gson();

    private static HealthConditionService healthConditionService=new HealthConditionService();

    public HealthConditionService() {
    }

    public static HealthConditionService getInstance(){
        return healthConditionService;
    }

    public void saveHealthCondition(Context context, HealthCondition healthCondition, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String jsonString = gson.toJson(healthCondition);
        Log.d(TAG, jsonString);
        Log.d(TAG, Config.save_health_condition_url);

        JSONObject object = null;

        try {
            object = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.save_health_condition_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }


    public void updateHealthCondition(Context context, HealthCondition healthCondition, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String jsonString = gson.toJson(healthCondition);
        Log.d(TAG, jsonString);
        Log.d(TAG, Config.update_health_condition_url+"/"+ healthCondition.getHealth_condition_id());

        JSONObject object = null;

        try {
            object = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Config.update_health_condition_url+"/"+ healthCondition.getHealth_condition_id(), object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }
    public void updateHealthConditions(Context context, String toJson, int size, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, toJson);
        Log.d(TAG, Config.update_health_conditions_url);

        JSONArray object = null;

        try {
            object = new JSONArray(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT, Config.update_health_conditions_url+"/"+size, object, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonArrayRequest);
    }

    public void addHealthConditions(Context context, String toJson, int size, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, toJson);
        Log.d(TAG, Config.add_health_conditions_url);

        JSONArray object = null;

        try {
            object = new JSONArray(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, Config.add_health_conditions_url+"/"+size, object, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonArrayRequest);
    }

    public void getAllHealthConditions(Context context, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, Config.get_all_healthconditions_url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Config.get_all_healthconditions_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonArrayRequest);
    }
}
