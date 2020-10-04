package com.example.charith.trigym.AsyncTasks.Payment;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.NewServices.PaymentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddPaymentsToServer extends AsyncTask<Void, Void, Void> {
    List<Payment> addServerObjects;
    Context context;
    AsyncJsonArrayListner listner;
    Gson gson;

    public AddPaymentsToServer(Context context, List<Payment> addServerObjects, AsyncJsonArrayListner listner) {
        this.context = context;
        this.addServerObjects = addServerObjects;
        this.listner = listner;
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        PaymentService.getInstance().addPayments(context, gson.toJson(addServerObjects), new ResponseListener() {
            @Override
            public void onSuccess(Context context, String data) {
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    listner.onSuccess(context, jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Context context, String error) {
                listner.onError(context, error);
            }
        });

        return null;
    }
}

