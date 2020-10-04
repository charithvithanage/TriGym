package com.example.charith.trigym.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charith.trigym.AsyncTasks.Member.AddMembersToServerAsync;
import com.example.charith.trigym.AsyncTasks.Member.GetAllMembersAsync;
import com.example.charith.trigym.AsyncTasks.Member.UpdateMembersToServerAsync;
import com.example.charith.trigym.AsyncTasks.Payment.AddPaymentsToServer;
import com.example.charith.trigym.AsyncTasks.Payment.GetAllPaymentsAsync;
import com.example.charith.trigym.AsyncTasks.Payment.UpdatePaymentsToServer;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Splash extends AppCompatActivity {
    private static final String TAG = "TriGym";
    Gson gson;

    AtomicInteger counter = new AtomicInteger(0);
    private int counterValue = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

        if (Utils.isDeviceOnline(Splash.this)) {
            new GetAllMembersAsync(Splash.this, new AsyncJsonArrayListner() {
                @Override
                public void onSuccess(Context context, JSONArray response) {
                    List<Member> updateServerObjects = new ArrayList<>();

                    boolean shouldUpdate = false;

                    DatabaseHandler databaseHandler = new DatabaseHandler(Splash.this);
                    List<Member> list = databaseHandler.getAllMembers();


                    if (response.length() > 0) {
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                shouldUpdate = true;

                                Member member = gson.fromJson(response.getString(i), Member.class);
                                Log.d(TAG + " SRVR MMBRS", gson.toJson(member));

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

                                    if (member.getMember_id() != null) {
                                        Long id = databaseHandler.addMember(member);
                                        Log.d(TAG + " ADD MMR TO LOCAL", gson.toJson(databaseHandler.getMemberById(String.valueOf(id))));
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        updateServerObjects = list;
                    }

                    for (Member member : list) {
                        Log.d(TAG + " LOCAL DATABASE ", gson.toJson(member));
                    }

                    if (updateServerObjects.size() > 0) {

                        if (shouldUpdate) {
                            new UpdateMembersToServerAsync(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
                                @Override
                                public void onSuccess(Context context, JSONArray jsonArray) {
                                    goToMainActivity(context);
                                    getAllPayments();

                                }

                                @Override
                                public void onError(Context context, String error) {
                                    goToMainActivity(context);
                                    getAllPayments();

                                }
                            }).execute();
                        } else {
                            new AddMembersToServerAsync(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
                                @Override
                                public void onSuccess(Context context, JSONArray jsonArray) {
                                    goToMainActivity(context);
                                    getAllPayments();

                                }

                                @Override
                                public void onError(Context context, String error) {
                                    goToMainActivity(context);
                                    getAllPayments();
                                }
                            }).execute();
                        }

                    } else {
                        goToMainActivity(context);
                        getAllPayments();
                    }

                }

                @Override
                public void onError(Context context, String error) {
                    goToMainActivity(context);
                    getAllPayments();
                }
            }).execute();


        } else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }

    private void getAllPayments() {


        new GetAllPaymentsAsync(Splash.this, new AsyncJsonArrayListner() {
            @Override
            public void onSuccess(Context context, JSONArray response) {
                List<Payment> updateServerObjects = new ArrayList<>();

                boolean shouldUpdate = false;

                DatabaseHandler databaseHandler = new DatabaseHandler(context);
                List<Payment> list = databaseHandler.getAllPayments();

                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length(); i++) {

                            shouldUpdate = true;
                            Payment payment = gson.fromJson(response.getString(i), Payment.class);
                            Log.d(TAG + " SRVR PAYs", gson.toJson(payment));

                            Payment excistingPayment = databaseHandler.getPaymentById(String.valueOf(payment.getPayment_id()), String.valueOf(payment.getMember_id()));
                            Log.d(TAG + " LOCAL PAYMENT", gson.toJson(excistingPayment));

                            if (excistingPayment.getMember_id() != null) {

                                if (!Utils.stringToDateTime(payment.getModified_at()).isEqual(Utils.stringToDateTime(excistingPayment.getModified_at()))) {
                                    if (Utils.stringToDateTime(payment.getModified_at()).isAfter(Utils.stringToDateTime(excistingPayment.getModified_at()))) {

                                        databaseHandler.updatePayment(payment);

                                        Log.d(TAG + " UP PAY TO LOCAL", gson.toJson(databaseHandler.getPaymentById(String.valueOf(payment.getPayment_id()), String.valueOf(payment.getMember_id()))));

                                    } else {

                                        updateServerObjects.add(excistingPayment);
                                        Log.d(TAG + " UP PAY TO SRVR", gson.toJson(excistingPayment));
//                                    new UpdateMemberAsync(excistingMember).execute();
                                    }
                                }

                            } else {

                                if (payment.getMember_id() != null) {
                                    Long id = databaseHandler.addPayment(payment);
                                    Log.d(TAG + " ADD PAY TO LOCAL", gson.toJson(databaseHandler.getPaymentById(String.valueOf(id), String.valueOf(payment.getMember_id()))));
                                }

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    updateServerObjects = list;
                }


                if (updateServerObjects.size() > 0) {

                    if (shouldUpdate) {
                        new UpdatePaymentsToServer(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
                            @Override
                            public void onSuccess(Context context, JSONArray jsonArray) {
                                goToMainActivity(context);
                            }

                            @Override
                            public void onError(Context context, String error) {
                                goToMainActivity(context);

                            }
                        }).execute();
                    } else {
                        new AddPaymentsToServer(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
                            @Override
                            public void onSuccess(Context context, JSONArray jsonArray) {
                                goToMainActivity(context);
                            }

                            @Override
                            public void onError(Context context, String error) {
                                goToMainActivity(context);

                            }
                        }).execute();

                    }
                } else {
                    goToMainActivity(context);
                }


            }

            @Override
            public void onError(Context context, String error) {
                goToMainActivity(context);
            }
        }).execute();
    }

    private void goToMainActivity(Context context) {
        counterValue = counter.incrementAndGet();

        if (counterValue == 4) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

}
