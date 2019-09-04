package com.example.charith.trigym.AsyncTasks.Member;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.MemberService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UpdateMembersToServerAsync extends AsyncTask<Void, Void, Void> {

    List<Member> updateServerMembers;

    Context context;
    AsyncJsonArrayListner listner;
    Gson gson;

    public UpdateMembersToServerAsync( Context context,List<Member> updateServerMembers, AsyncJsonArrayListner listner) {
        this.updateServerMembers = updateServerMembers;
        this.context = context;
        this.listner = listner;
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        MemberService.getInstance().updateMembers(context, gson.toJson(updateServerMembers), updateServerMembers.size(), new VolleyCallback() {
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