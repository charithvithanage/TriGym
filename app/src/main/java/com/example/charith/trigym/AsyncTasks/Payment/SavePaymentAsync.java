package com.example.charith.trigym.AsyncTasks.Payment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.charith.trigym.Constants;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.SuccessErrorListener;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.PaymentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.charith.trigym.Constants.TAG;
import static com.example.charith.trigym.Utils.createEntityDirectory;
import static com.example.charith.trigym.Utils.readJSONFile;
import static com.example.charith.trigym.Utils.saveJSONFile;

public class SavePaymentAsync extends AsyncTask<Void,Void,Void> {
    AsyncListner listner;
    Context context;
    Payment payment;
    Gson gson;
    public SavePaymentAsync(Context context,Payment payment,AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.payment=payment;

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        File mediaStorageDir = createEntityDirectory(context, Constants.PAYMENTS);

        readJSONFile(context,mediaStorageDir, new ResponseListener() {
            @Override
            public void onSuccess(Context activity,String existingMembers) {
                List<Payment> tempArray = new ArrayList<>();

                if (existingMembers != null||!existingMembers.equals("")) {
                    try {
                        JSONArray jsonArrayExisting = new JSONArray(existingMembers);

                        for (int i = 0; i < jsonArrayExisting.length(); i++) {
                            Payment member = gson.fromJson(jsonArrayExisting.getString(i), Payment.class);
                            Log.d(TAG, gson.toJson(member));
                            tempArray.add(member);
                        }

                        tempArray.add(payment);


                        saveJSONFile(gson.toJson(tempArray), mediaStorageDir, new SuccessErrorListener() {
                            @Override
                            public void onSuccess() {
                                listner.onSuccess(activity, null);

                            }

                            @Override
                            public void onError(String error) {
                                listner.onError(activity, error);

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    tempArray.add(payment);

                    saveJSONFile(gson.toJson(tempArray), mediaStorageDir, new SuccessErrorListener() {
                        @Override
                        public void onSuccess() {
                            listner.onSuccess(activity, null);
                        }

                        @Override
                        public void onError(String error) {
                            listner.onError(activity, error);
                        }
                    });

                }
            }

            @Override
            public void onError(Context activity,String error) {
                listner.onError(activity, error);
            }
        });

//        PaymentService.getInstance().savePayment(context, payment, new VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                listner.onSuccess(context,response);
//            }
//
//            @Override
//            public void onSuccess(JSONArray response) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                listner.onError(context,error);
//            }
//        });
        return null;
    }
}
