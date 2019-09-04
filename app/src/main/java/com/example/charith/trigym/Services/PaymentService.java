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
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentService {

    private static final String TAG="TriGym";

    Gson gson = new Gson();


    private static PaymentService paymentService=new PaymentService();

   public  PaymentService(){

    }

    public static PaymentService getInstance(){
       return paymentService;
    }


    public void getPayments(Context context, final VolleyCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, Config.get_all_payments_url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Config.get_all_payments_url, null, new Response.Listener<JSONArray>() {
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

//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonArrayRequest);
    }

    public void getPaymentsByMemberId(Context context, String member_id,final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, Config.get_payments_by_member_id_url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Config.get_payments_by_member_id_url+"/"+member_id, null, new Response.Listener<JSONArray>() {
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

    public void updatePayments(Context context, String toJson, int size, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, toJson);
        Log.d(TAG, Config.update_payments_url);

        JSONArray object = null;

        try {
            object = new JSONArray(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT, Config.update_payments_url+"/"+size, object, new Response.Listener<JSONArray>() {
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

    public void addPayments(Context context, String toJson, int size, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, toJson);
        Log.d(TAG, Config.add_payments_url);

        JSONArray object = null;

        try {
            object = new JSONArray(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, Config.add_payments_url+"/"+size, object, new Response.Listener<JSONArray>() {
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

    public void savePayment(Context context, Payment payment, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String jsonString = gson.toJson(payment);
        Log.d(TAG, jsonString);
        Log.d(TAG, Config.save_payment_url);

        JSONObject object = null;

        try {
            object = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.save_payment_url, object, new Response.Listener<JSONObject>() {
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



}
