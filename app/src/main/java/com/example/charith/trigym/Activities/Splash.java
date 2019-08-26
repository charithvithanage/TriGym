package com.example.charith.trigym.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.charith.trigym.ApiService;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Splash extends AppCompatActivity {
    private static final String TAG = "TriGym";
    Gson gson;

    AtomicInteger counter=new AtomicInteger(0);
    private int counterValue=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

        new GetAllMembersAsync().execute();

        new GetAllPaymentsAsync().execute();


    }

    private class GetAllMembersAsync extends AsyncTask<Void, Void, Void> {
        List<Member> updateServerObjects;

        public GetAllMembersAsync() {
            this.updateServerObjects = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ApiService.getInstance().getAllMembers(Splash.this, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {

                }

                @Override
                public void onSuccess(JSONArray response) {
                    try {

                        for (int i = 0; i < response.length(); i++) {

                            Member member = gson.fromJson(response.getString(i), Member.class);
                            Log.d(TAG + " SRVR MMBRS", gson.toJson(member));

                            DatabaseHandler databaseHandler = new DatabaseHandler(Splash.this);
                            Member excistingMember = databaseHandler.getMemberById(String.valueOf(member.getMember_id()));
                            if (excistingMember.getMember_id() != null) {

                                if (!Utils.stringToDateTime(member.getModified_at()).isEqual(Utils.stringToDateTime(excistingMember.getModified_at()))) {
                                    if (Utils.stringToDateTime(member.getModified_at()).isAfter(Utils.stringToDateTime(excistingMember.getModified_at()))) {

                                        databaseHandler.updateMember(member);

                                        Log.d(TAG + " UP MBRS TO LOCAL", gson.toJson(databaseHandler.getMemberById(String.valueOf(member.getMember_id()))));

                                    } else {

                                        updateServerObjects.add(excistingMember);

                                        Log.d(TAG + " UP MBRS TO SRVR", gson.toJson(excistingMember));
//                                    new UpdateMemberAsync(excistingMember).execute();
                                    }
                                }

                            } else {
                                Long id = databaseHandler.addMember(member);
                                Log.d(TAG + " ADD TO LOCAL", gson.toJson(databaseHandler.getMemberById(String.valueOf(id))));

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DatabaseHandler databaseHandler = new DatabaseHandler(Splash.this);

                    List<Member> list = databaseHandler.getAllMembers();

                    for (Member member : list) {
                        Log.d(TAG + " LOCAL DATABASE ", gson.toJson(member));
                    }

                    if(updateServerObjects.size()>0){
                        new UpdateMembersToServerAsync(updateServerObjects).execute();
                    }

                    counterValue=counter.incrementAndGet();

                    if(counterValue==2){
                        Intent intent=new Intent(Splash.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                }
            });
            return null;
        }
    }

    private class GetAllPaymentsAsync extends AsyncTask<Void, Void, Void> {

        List<Payment> updateServerObjects;

        public GetAllPaymentsAsync() {
            updateServerObjects = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ApiService.getInstance().getPayments(Splash.this, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {

                }

                @Override
                public void onSuccess(JSONArray response) {
                    try {

                        for (int i = 0; i < response.length(); i++) {

                            Payment payment = gson.fromJson(response.getString(i), Payment.class);
                            Log.d(TAG + " SRVR PAYs", gson.toJson(payment));

                            DatabaseHandler databaseHandler = new DatabaseHandler(Splash.this);
                            Payment excistingPayment = databaseHandler.getPaymentById(String.valueOf(payment.getId()), String.valueOf(payment.getMember_id()));
                            if (excistingPayment.getMember_id() != null) {

                                if (!Utils.stringToDateTime(payment.getModified_at()).isEqual(Utils.stringToDateTime(excistingPayment.getModified_at()))) {
                                    if (Utils.stringToDateTime(payment.getModified_at()).isAfter(Utils.stringToDateTime(excistingPayment.getModified_at()))) {

                                        databaseHandler.updatePayment(payment);

                                        Log.d(TAG + " UP PAY TO LOCAL", gson.toJson(databaseHandler.getPaymentById(String.valueOf(payment.getId()), String.valueOf(payment.getMember_id()))));

                                    } else {

                                        updateServerObjects.add(excistingPayment);
                                        Log.d(TAG + " UP PAY TO SRVR", gson.toJson(excistingPayment));
//                                    new UpdateMemberAsync(excistingMember).execute();
                                    }
                                }

                            } else {
                                Long id = databaseHandler.addPayment(payment);
                                Log.d(TAG + " ADD TO LOCAL", gson.toJson(databaseHandler.getPaymentById(String.valueOf(id), String.valueOf(payment.getMember_id()))));

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DatabaseHandler databaseHandler = new DatabaseHandler(Splash.this);

                    List<Payment> list = databaseHandler.getAllPayments();

                    if(updateServerObjects.size()>0){
                        new UpdatePaymentsToServer(updateServerObjects).execute();
                    }

                    counter.incrementAndGet();

                    if(counterValue==2){
                        Intent intent=new Intent(Splash.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                }
            });
            return null;
        }
    }


    private class UpdateMembersToServerAsync extends AsyncTask<Void, Void, Void> {

        List<Member> updateServerMembers;


        public UpdateMembersToServerAsync(List<Member> updateServerMembers) {
            this.updateServerMembers = updateServerMembers;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ApiService.getInstance().updateMembers(Splash.this, gson.toJson(updateServerMembers),updateServerMembers.size(), new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());
                }

                @Override
                public void onSuccess(JSONArray response) {

                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                }
            });
            return null;
        }
    }

    private class UpdatePaymentsToServer extends AsyncTask<Void, Void, Void> {
        List<Payment> updateServerObjects;

        public UpdatePaymentsToServer(List<Payment> updateServerObjects) {
            this.updateServerObjects = updateServerObjects;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ApiService.getInstance().updatePayments(Splash.this, gson.toJson(updateServerObjects),updateServerObjects.size(), new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());
                }

                @Override
                public void onSuccess(JSONArray response) {
                    Log.d(TAG, response.toString());
                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error.toString());

                }
            });

            return null;
        }
    }
}
