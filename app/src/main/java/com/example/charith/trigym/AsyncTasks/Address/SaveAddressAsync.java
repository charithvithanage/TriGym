package com.example.charith.trigym.AsyncTasks.Address;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.AddressService;

import org.json.JSONArray;
import org.json.JSONObject;

public class SaveAddressAsync extends AsyncTask<Void,Void,Void> {
    AsyncListner listner;
    Context context;
    Address address;

    public SaveAddressAsync(Context context, Address address, AsyncListner listner ) {
        this.listner = listner;
        this.context = context;
        this.address=address;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        AddressService.getInstance().saveAddress(context, address, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                listner.onSuccess(context,response);
            }

            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onError(String error) {
                listner.onError(context,error);
            }
        });
        return null;
    }
}
