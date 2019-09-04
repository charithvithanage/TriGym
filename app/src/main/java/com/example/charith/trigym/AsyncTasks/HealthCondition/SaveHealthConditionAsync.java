package com.example.charith.trigym.AsyncTasks.HealthCondition;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.HealthCondition;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.HealthConditionService;

import org.json.JSONArray;
import org.json.JSONObject;

public class SaveHealthConditionAsync extends AsyncTask<Void,Void,Void> {
    AsyncListner listner;
    Context context;
    HealthCondition healthCondition;

    public SaveHealthConditionAsync(Context context, HealthCondition healthCondition, AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.healthCondition=healthCondition;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        HealthConditionService.getInstance().saveHealthCondition(context, healthCondition, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                listner.onSuccess(context,response);
            }

            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onError(String error) {
                listner.onError(context,error);
            }
        });
        return null;
    }
}
