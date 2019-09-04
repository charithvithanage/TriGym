package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.MemberService;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetAllMembersAsync extends AsyncTask<Void, Void, Void> {
    Context context;
    AsyncJsonArrayListner listner;

    public GetAllMembersAsync(Context context, AsyncJsonArrayListner listner) {
        this.context = context;
        this.listner = listner;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        MemberService.getInstance().getAllMembers(context, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onSuccess(JSONArray response) {
                listner.onSuccess(context, response);
            }

            @Override
            public void onError(String error) {
                listner.onError(context, error);
            }
        });
        return null;
    }
}

