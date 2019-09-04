package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.HealthCondition;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.HealthConditionService;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateHealthConditionAsync extends AsyncTask<Void,Void,Void> {
    AsyncListner listner;
    Context context;
    HealthCondition healthCondition;

    public UpdateHealthConditionAsync(Context context, HealthCondition healthCondition, AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.healthCondition=healthCondition;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        HealthConditionService.getInstance().updateHealthCondition(context, healthCondition, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                listner.onSuccess(response);
            }

            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onError(String error) {
                listner.onError(error);
            }
        });
        return null;
    }
}
