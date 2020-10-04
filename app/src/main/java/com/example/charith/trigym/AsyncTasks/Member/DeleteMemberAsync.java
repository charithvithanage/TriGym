package com.example.charith.trigym.AsyncTasks.Member;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Constants;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.SuccessErrorListener;
import com.example.charith.trigym.Interfaces.SuccessListner;
import com.example.charith.trigym.NewServices.MemberService;
import com.example.charith.trigym.NewServices.PaymentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.charith.trigym.Utils.createEntityDirectory;
import static com.example.charith.trigym.Utils.createMembersDirectory;
import static com.example.charith.trigym.Utils.saveJSONFile;
import static com.example.charith.trigym.Utils.saveMemberJSONFile;

public class DeleteMemberAsync extends AsyncTask<Void, Void, Void> {

    SuccessListner listner;
    Context context;
    Member member;
    AtomicInteger counter = new AtomicInteger(0);
    private int counterValue = 0;
    ProgressDialog progressDialog;
    Gson gson;

    public DeleteMemberAsync(Context context, Member member, SuccessListner listner) {
        this.listner = listner;
        this.context = context;
        this.member = member;

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MemberService.getInstance().deleteMember(context, member, new ResponseListener() {
            @Override
            public void onSuccess(Context context, String data) {

                if (data != null && !data.equals("")) {
                    File mediaStorageDir = createMembersDirectory(context, Constants.MEMBERS);

                    saveMemberJSONFile(data, mediaStorageDir, new SuccessErrorListener() {
                        @Override
                        public void onSuccess() {
                            startCounter();
                        }

                        @Override
                        public void onError(String error) {
                            startCounter();
                        }
                    });

                } else {
                    startCounter();
                }

            }

            @Override
            public void onError(Context context, String error) {

                startCounter();

            }
        });

        PaymentService.getInstance().deletePayments(context, member.getMember_id(), new ResponseListener() {
            @Override
            public void onSuccess(Context context, String data) {

                if (data != null && !data.equals("")) {
                    File mediaStorageDir = createEntityDirectory(context, Constants.PAYMENTS);

                    saveJSONFile(data, mediaStorageDir, new SuccessErrorListener() {
                        @Override
                        public void onSuccess() {
                            startCounter();
                        }

                        @Override
                        public void onError(String error) {
                            startCounter();
                        }
                    });

                } else {
                    startCounter();
                }

            }

            @Override
            public void onError(Context context, String error) {

                startCounter();

            }
        });
        return null;
    }

    private void startCounter() {
        counterValue = counter.incrementAndGet();

        if (counterValue == 2) {
            progressDialog.dismiss();
            listner.onFinished();
        }
    }
}
