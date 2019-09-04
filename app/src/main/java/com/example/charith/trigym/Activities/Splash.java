package com.example.charith.trigym.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.charith.trigym.AsyncTasks.AddMembersToServerAsync;
import com.example.charith.trigym.AsyncTasks.GetAllAddressesAsync;
import com.example.charith.trigym.AsyncTasks.GetAllHealthConditionsAsync;
import com.example.charith.trigym.AsyncTasks.GetAllMembersAsync;
import com.example.charith.trigym.AsyncTasks.GetAllPaymentsAsync;
import com.example.charith.trigym.AsyncTasks.UpdateAddresssToServerAsync;
import com.example.charith.trigym.AsyncTasks.UpdateHealthConditionsToServer;
import com.example.charith.trigym.AsyncTasks.UpdateMembersToServerAsync;
import com.example.charith.trigym.AsyncTasks.UpdatePaymentsToServer;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.HealthCondition;
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

            new GetAllHealthConditionsAsync(Splash.this, new AsyncJsonArrayListner() {
                @Override
                public void onSuccess(Context context, JSONArray response) {
                    List<HealthCondition> updateServerObjects = new ArrayList<>();
                    boolean shouldUpdate = false;

                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    List<HealthCondition> list = databaseHandler.getAllHealthConditions();

                    if (response.length() > 0) {
                        try {


                            for (int i = 0; i < response.length(); i++) {
                                shouldUpdate = true;
                                HealthCondition healthCondition = gson.fromJson(response.getString(i), HealthCondition.class);
                                Log.d(TAG + " SRVR HCS", gson.toJson(healthCondition));

                                HealthCondition existingHealthCondition = databaseHandler.getHealthConditionById(String.valueOf(healthCondition.getHealth_condition_id()));

                                if (existingHealthCondition.getHealth_condition_id() != null) {

                                    if (!Utils.stringToDateTime(healthCondition.getModified_at()).isEqual(Utils.stringToDateTime(existingHealthCondition.getModified_at()))) {
                                        if (Utils.stringToDateTime(healthCondition.getModified_at()).isAfter(Utils.stringToDateTime(existingHealthCondition.getModified_at()))) {

                                            databaseHandler.updateHealthCondition(healthCondition);

                                            Log.d(TAG + " UP HCS TO LOCAL", gson.toJson(databaseHandler.getHealthConditionById(String.valueOf(healthCondition.getHealth_condition_id()))));

                                        } else {

                                            updateServerObjects.add(existingHealthCondition);
                                            Log.d(TAG + " UP HCS TO SRVR", gson.toJson(existingHealthCondition));
//                                    new UpdateMemberAsync(excistingMember).execute();
                                        }
                                    }

                                } else {

                                    if (healthCondition.getHealth_condition_id() != null) {
                                        Long id = databaseHandler.addHealthCondition(healthCondition);
                                        Log.d(TAG + " ADD HCS TO LOCAL", gson.toJson(databaseHandler.getHealthConditionById(String.valueOf(healthCondition.getHealth_condition_id()))));
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
                            new UpdateHealthConditionsToServer(context, updateServerObjects, new AsyncJsonArrayListner() {
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
                            new AddHealthConditionsToServer(context, updateServerObjects, new AsyncJsonArrayListner() {
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

            new GetAllAddressesAsync(Splash.this, new AsyncJsonArrayListner() {
                @Override
                public void onSuccess(Context context, JSONArray response) {
                    boolean shouldUpdate = false;

                    List<Address> updateServerObjects = new ArrayList<>();

                    DatabaseHandler databaseHandler = new DatabaseHandler(Splash.this);

                    List<Address> list = databaseHandler.getAllAddresses();

                    Log.d(TAG+"L DB ADDRESS",gson.toJson(list));

                    if (response.length() > 0) {
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                Address address = gson.fromJson(response.getString(i), Address.class);
                                Log.d(TAG + " SRVR ADRS", gson.toJson(address));
                                shouldUpdate = true;

                                Address excistingAddress = databaseHandler.getAddressById(String.valueOf(address.getAddress_id()));
                                if (excistingAddress.getAddress_id() != null) {

                                    if (!Utils.stringToDateTime(address.getModified_at()).isEqual(Utils.stringToDateTime(excistingAddress.getModified_at()))) {
                                        if (Utils.stringToDateTime(address.getModified_at()).isAfter(Utils.stringToDateTime(excistingAddress.getModified_at()))) {

                                            databaseHandler.updateAddress(address);

                                            Log.d(TAG + " UP ADRS TO LOCAL", gson.toJson(databaseHandler.getAddressById(String.valueOf(address.getAddress_id()))));

                                        } else {

                                            updateServerObjects.add(excistingAddress);
                                            Log.d(TAG + " UP ADRS TO SRVR", gson.toJson(excistingAddress));
//                                    new UpdateMemberAsync(excistingMember).execute();
                                        }
                                    }

                                } else {

                                    if (address.getAddress_id() != null) {
                                        Long id = databaseHandler.addAddress(address);
                                        Log.d(TAG + " ADD ADR TO LOCAL", gson.toJson(databaseHandler.getAddressById(String.valueOf(address.getAddress_id()))));
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

                        if(shouldUpdate){
                            new UpdateAddresssToServerAsync(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
                                @Override
                                public void onSuccess(Context context, JSONArray jsonArray) {
                                    goToMainActivity(context);
                                }

                                @Override
                                public void onError(Context context, String error) {
                                    goToMainActivity(context);
                                }
                            }).execute();
                        }else {
                            new AddAddresssToServerAsync(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
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
                                }

                                @Override
                                public void onError(Context context, String error) {
                                    goToMainActivity(context);

                                }
                            }).execute();
                        } else {
                            new AddMembersToServerAsync(Splash.this, updateServerObjects, new AsyncJsonArrayListner() {
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

        } else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


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
