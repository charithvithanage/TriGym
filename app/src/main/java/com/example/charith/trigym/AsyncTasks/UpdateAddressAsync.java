package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.AddressService;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateAddressAsync extends AsyncTask<Void,Void,Void> {
    AsyncListner listner;
    Context context;
    Address address;

    public UpdateAddressAsync(Context context, Address address, AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.address=address;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        AddressService.getInstance().updateAddress(context, address, new VolleyCallback() {
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
