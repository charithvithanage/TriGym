package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.PaymentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UpdatePaymentsToServer extends AsyncTask<Void, Void, Void> {
    List<Payment> updateServerObjects;
    Context context;
    AsyncJsonArrayListner listner;
    Gson gson;

    public UpdatePaymentsToServer(Context context, List<Payment> updateServerObjects, AsyncJsonArrayListner listner) {
        this.context = context;
        this.updateServerObjects = updateServerObjects;
        this.listner = listner;
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        PaymentService.getInstance().updatePayments(context, gson.toJson(updateServerObjects), updateServerObjects.size(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onSuccess(JSONArray response) {
                listner.onSuccess(context,response);
            }

            @Override
            public void onError(String error) {
                listner.onError(context,error);

            }
        });

        return null;
    }
}

