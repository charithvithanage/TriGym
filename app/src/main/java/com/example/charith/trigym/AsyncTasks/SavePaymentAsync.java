package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.PaymentService;

import org.json.JSONArray;
import org.json.JSONObject;

public class SavePaymentAsync extends AsyncTask<Void,Void,Void> {
    AsyncListner listner;
    Context context;
    Payment payment;

    public SavePaymentAsync(Context context,Payment payment,AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.payment=payment;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        PaymentService.getInstance().savePayment(context, payment, new VolleyCallback() {
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
