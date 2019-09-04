package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.AddressService;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetAllAddressesAsync extends AsyncTask<Void, Void, Void> {

    Context context;
    AsyncJsonArrayListner listner;

    public GetAllAddressesAsync(Context context, AsyncJsonArrayListner listner) {
        this.context = context;
        this.listner = listner;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        AddressService.getInstance().getAllAddresses(context, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onSuccess(JSONArray response) {
                listner.onSuccess(context,response);
            }

            @Override
            public void onError(String error) {
                listner.onError(context,error);
            }
        });
        return null;
    }
}

