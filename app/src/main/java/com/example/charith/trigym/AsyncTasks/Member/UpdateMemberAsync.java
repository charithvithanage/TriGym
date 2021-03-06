package com.example.charith.trigym.AsyncTasks.Member;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.MemberService;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateMemberAsync extends AsyncTask<Void,Void,Void> {

    AsyncListner listner;
    Context context;
    Member member;

    public UpdateMemberAsync( Context context, Member member,AsyncListner listner) {
        this.listner = listner;
        this.context = context;
        this.member = member;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MemberService.getInstance().updateMember(context, member, new VolleyCallback() {
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
