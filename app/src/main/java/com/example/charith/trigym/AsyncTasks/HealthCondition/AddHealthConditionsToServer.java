package com.example.charith.trigym.AsyncTasks.HealthCondition;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.HealthCondition;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.HealthConditionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class AddHealthConditionsToServer extends AsyncTask<Void, Void, Void> {
    List<HealthCondition> addServerObjects;
    Context context;
    AsyncJsonArrayListner listner;
    Gson gson;

    public AddHealthConditionsToServer(Context context, List<HealthCondition> addServerObjects, AsyncJsonArrayListner listner) {
        this.addServerObjects = addServerObjects;
        this.context = context;
        this.listner = listner;
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        HealthConditionService.getInstance().addHealthConditions(context, gson.toJson(addServerObjects), addServerObjects.size(), new VolleyCallback() {
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
