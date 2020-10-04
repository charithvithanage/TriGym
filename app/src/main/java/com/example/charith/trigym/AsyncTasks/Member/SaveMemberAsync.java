package com.example.charith.trigym.AsyncTasks.Member;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.NewServices.MemberService;

public class SaveMemberAsync extends AsyncTask<Void, Void, Void> {

    ResponseListener listner;
    Context context;
    Member member;

    public SaveMemberAsync(Context context, Member member, ResponseListener listner) {
        this.listner = listner;
        this.context = context;
        this.member = member;


    }

    @Override
    protected Void doInBackground(Void... voids) {

        MemberService.getInstance().saveMember(context, member, new ResponseListener() {
            @Override
            public void onSuccess(Context activity, String data) {
                listner.onSuccess(activity, data);
            }

            @Override
            public void onError(Context activity, String error) {
                listner.onError(activity, error);
            }
        });
        return null;
    }
}