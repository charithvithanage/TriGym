package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.MemberService;

import org.json.JSONArray;
import org.json.JSONObject;

public class SaveMemberAsync extends AsyncTask<Void, Void, Void> {

    AsyncListner listner;
    Context context;
    Member member;

    public SaveMemberAsync(Context context,Member member,AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.member=member;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        MemberService.getInstance().saveMember(context, member, new VolleyCallback() {
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