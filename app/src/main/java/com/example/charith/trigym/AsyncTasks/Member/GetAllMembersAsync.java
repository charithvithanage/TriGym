package com.example.charith.trigym.AsyncTasks.Member;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.NewServices.MemberService;

import org.json.JSONArray;
import org.json.JSONException;
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

        MemberService.getInstance().getAllMembers(context, new ResponseListener() {
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

