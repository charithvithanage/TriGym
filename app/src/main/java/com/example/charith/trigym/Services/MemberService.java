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
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberService {
    private static final String TAG="TriGym";

    Gson gson = new Gson();

    private static MemberService memberService = new MemberService();

    public MemberService() {

    }

    public static MemberService getInstance() {
        return memberService;
    }

    public void saveMember(Context context, Member member, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String jsonString = gson.toJson(member);
        Log.d(TAG, jsonString);
        Log.d(TAG, Config.save_member_url);

        JSONObject object = null;

        try {
            object = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.save_member_url, object, new Response.Listener<JSONObject>() {
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

    public void updateMember(Context context, Member member, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String jsonString = gson.toJson(member);
        Log.d(TAG, jsonString);
        Log.d(TAG, Config.update_member_url+"/"+ member.getMember_id());

        JSONObject object = null;

        try {
            object = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Config.update_member_url+"/"+ member.getMember_id(), object, new Response.Listener<JSONObject>() {
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

    public void getAllMembers(Context context, final VolleyCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, Config.get_all_members_url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Config.get_all_members_url, null, new Response.Listener<JSONArray>() {
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

    public void updateMembers(Context context, String toJson, int size, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, toJson);
        Log.d(TAG, Config.update_members_url);

        JSONArray object = null;

        try {
            object = new JSONArray(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT, Config.update_members_url+"/"+size, object, new Response.Listener<JSONArray>() {
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

    public void addMembers(Context context, String toJson, int size, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, toJson);
        Log.d(TAG, Config.add_members_url);

        JSONArray object = null;

        try {
            object = new JSONArray(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, Config.add_members_url+"/"+size, object, new Response.Listener<JSONArray>() {
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
