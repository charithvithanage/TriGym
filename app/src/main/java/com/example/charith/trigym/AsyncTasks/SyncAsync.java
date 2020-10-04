package com.example.charith.trigym.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.example.charith.trigym.Activities.Member.MemberBioActivity;
import com.example.charith.trigym.Activities.NewSplash;
import com.example.charith.trigym.AsyncTasks.Member.AddMembersToServerAsync;
import com.example.charith.trigym.AsyncTasks.Payment.AddPaymentsToServer;
import com.example.charith.trigym.Constants;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.SuccessErrorListener;
import com.example.charith.trigym.Interfaces.SuccessListner;
import com.example.charith.trigym.NewServices.MemberService;
import com.example.charith.trigym.NewServices.PaymentService;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.charith.trigym.Constants.TAG;
import static com.example.charith.trigym.Utils.createEntityDirectory;
import static com.example.charith.trigym.Utils.saveJSONFile;
import static com.example.charith.trigym.Utils.showAlertWithoutTitleDialog;
import static com.example.charith.trigym.Utils.stringToDateTime;

public class SyncAsync extends AsyncTask<Void, Void, Void> {
    Context context;
    SuccessListner listner;
    DatabaseHandler databaseHandler;
    Gson gson;
    AtomicInteger counter = new AtomicInteger(0);
    private int counterValue = 0;
    ProgressDialog progressDialog;

    public SyncAsync(Context context, SuccessListner listner) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");

        this.context = context;
        this.listner = listner;
        databaseHandler = new DatabaseHandler(context);
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        MemberService.getInstance().getAllMembers(context, new ResponseListener() {
            @Override
            public void onSuccess(Context context, String data) {
                List<Member> list = databaseHandler.getAllMembers();
                List<Member> updateServerObjects = new ArrayList<>();

                if (data != null && !data.equals("")) {
                    try {
                        JSONArray response = new JSONArray(data);
                        if (response.length() > 0) {
                            try {

                                for (int i = 0; i < response.length(); i++) {

                                    Member member = gson.fromJson(response.getString(i), Member.class);
                                    Log.d(TAG + " SRVR MMBRS", gson.toJson(member));

                                    Member excistingMember = databaseHandler.getMemberById(String.valueOf(member.getMember_id()));
                                    if (excistingMember.getMember_id() != null) {

                                        if (!stringToDateTime(member.getModified_at()).isEqual(stringToDateTime(excistingMember.getModified_at()))) {
                                            if (stringToDateTime(member.getModified_at()).isAfter(stringToDateTime(excistingMember.getModified_at()))) {

                                                databaseHandler.updateMember(member);

                                                Log.d(TAG + " UP MBRS TO LOCAL", gson.toJson(databaseHandler.getMemberById(String.valueOf(member.getMember_id()))));

                                            } else {

                                                updateServerObjects.add(excistingMember);

                                                Log.d(TAG + " UP MBRS TO SRVR", gson.toJson(excistingMember));
                                            }
                                        } else {
                                            updateServerObjects.add(excistingMember);
                                        }

                                    } else {

                                        if (member.getMember_id() != null) {
                                            Long id = databaseHandler.addMember(member);
                                            updateServerObjects.add(member);

                                            Log.d(TAG + " ADD MMR TO LOCAL", gson.toJson(databaseHandler.getMemberById(String.valueOf(id))));
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (list != null && list.size() != 0) {
                                for (Member obj : list) {
                                    Log.d(TAG + " LOCAL DATABASE ", gson.toJson(obj));
                                    if (response.length() > 0) {
                                        if (!checkMemberAvailabilityFromServer(response, obj)) {
                                            updateServerObjects.add(obj);
                                        }}
                                }
                            }

                        } else {
                            updateServerObjects = list;
                        }



//                        if (updateServerObjects.size() > 0) {
//
//
//                            File mediaStorageDir = createEntityDirectory(context, Constants.MEMBERS);
//
//                            saveJSONFile(gson.toJson(updateServerObjects), mediaStorageDir, new SuccessErrorListener() {
//                                @Override
//                                public void onSuccess() {
//                                    goToMainActivity(context);
//                                }
//
//                                @Override
//                                public void onError(String error) {
//                                    goToMainActivity(context);
//                                }
//                            });
//
//                        } else {
//                            goToMainActivity(context);
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        goToMainActivity(context);
                    }
                } else {
                    updateServerObjects = list;
                }

                if (updateServerObjects.size() > 0) {


                    File mediaStorageDir = createEntityDirectory(context, Constants.MEMBERS);

                    saveJSONFile(gson.toJson(updateServerObjects), mediaStorageDir, new SuccessErrorListener() {
                        @Override
                        public void onSuccess() {
                            goToMainActivity(context);
                        }

                        @Override
                        public void onError(String error) {
                            goToMainActivity(context);
                        }
                    });

                } else {
                    goToMainActivity(context);
                }


            }

            @Override
            public void onError(Context context, String error) {
                goToMainActivity(context);
            }
        });

        PaymentService.getInstance().getPayments(context, new ResponseListener() {
            @Override
            public void onSuccess(Context context, String data) {
                List<Payment> updateServerObjects = new ArrayList<>();
                List<Payment> list = databaseHandler.getAllPayments();

                if (data != null && !data.equals("")) {

                    try {
                        JSONArray response = new JSONArray(data);
                        if (response.length() > 0) {
                            try {

                                for (int i = 0; i < response.length(); i++) {

                                    Payment payment = gson.fromJson(response.getString(i), Payment.class);
                                    Log.d(TAG + " SRVR PAYs", gson.toJson(payment));

                                    Payment excistingPayment = databaseHandler.getPaymentById(String.valueOf(payment.getPayment_id()), String.valueOf(payment.getMember_id()));
                                    Log.d(TAG + " LOCAL PAYMENT", gson.toJson(excistingPayment));

                                    if (excistingPayment.getMember_id() != null) {

                                        if (!stringToDateTime(payment.getModified_at()).isEqual(stringToDateTime(excistingPayment.getModified_at()))) {
                                            if (stringToDateTime(payment.getModified_at()).isAfter(stringToDateTime(excistingPayment.getModified_at()))) {

                                                databaseHandler.updatePayment(payment);

                                                Log.d(TAG + " UP PAY TO LOCAL", gson.toJson(databaseHandler.getPaymentById(String.valueOf(payment.getPayment_id()), String.valueOf(payment.getMember_id()))));

                                            } else {

                                                updateServerObjects.add(excistingPayment);
                                                Log.d(TAG + " UP PAY TO SRVR", gson.toJson(excistingPayment));
//                                    new UpdateMemberAsync(excistingMember).execute();
                                            }
                                        } else {
                                            updateServerObjects.add(excistingPayment);
                                        }

                                    } else {

                                        if (payment.getMember_id() != null) {
                                            Long id = databaseHandler.addPayment(payment);
                                            updateServerObjects.add(excistingPayment);

                                            Log.d(TAG + " ADD PAY TO LOCAL", gson.toJson(databaseHandler.getPaymentById(String.valueOf(id), String.valueOf(payment.getMember_id()))));
                                        }

                                    }
                                }

                                if (list != null && list.size() != 0) {
                                    for (Payment obj : list) {
                                        Log.d(TAG + " LOCAL DATABASE ", gson.toJson(obj));

                                        if (!checkPaymentAvailabilityFromServer(response, obj)) {
                                            updateServerObjects.add(obj);
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            updateServerObjects = list;
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        goToMainActivity(context);

                    }

                } else {
                    updateServerObjects = list;
                }


                if (updateServerObjects.size() > 0) {
                    File mediaStorageDir = createEntityDirectory(context, Constants.PAYMENTS);

                    saveJSONFile(gson.toJson(updateServerObjects), mediaStorageDir, new SuccessErrorListener() {
                        @Override
                        public void onSuccess() {
                            goToMainActivity(context);

                        }

                        @Override
                        public void onError(String error) {
                            goToMainActivity(context);

                        }
                    });
                } else {
                    goToMainActivity(context);
                }

            }

            @Override
            public void onError(Context context, String error) {
                goToMainActivity(context);
            }
        });
        return null;
    }

    private boolean checkMemberAvailabilityFromServer(JSONArray response, Member obj) {
        for (int i = 0; i < response.length(); i++) {
            try {
                Member member = gson.fromJson(response.getString(i), Member.class);
                if (member.getMember_id() == (obj.getMember_id())) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkPaymentAvailabilityFromServer(JSONArray response, Payment obj) {
        for (int i = 0; i < response.length(); i++) {
            try {
                Payment member = gson.fromJson(response.getString(i), Payment.class);
                if (member.getPayment_id() == (obj.getPayment_id())) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void goToMainActivity(Context context) {
        counterValue = counter.incrementAndGet();

        if (counterValue == 2) {
            progressDialog.dismiss();
            listner.onFinished();
        }
    }
}


