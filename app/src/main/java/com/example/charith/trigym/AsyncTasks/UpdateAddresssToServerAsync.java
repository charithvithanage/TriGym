package com.example.charith.trigym.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.Services.AddressService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UpdateAddresssToServerAsync extends AsyncTask<Void,Void,Void> {

    List<Address> updateServerAddresss;

    Context context;
    AsyncJsonArrayListner listner;
    Gson gson;

    public UpdateAddresssToServerAsync( Context context,List<Address> updateServerAddresss, AsyncJsonArrayListner listner) {
        this.updateServerAddresss = updateServerAddresss;
        this.context = context;
        this.listner = listner;
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        AddressService.getInstance().updateAddresses(context, gson.toJson(updateServerAddresss), updateServerAddresss.size(), new VolleyCallback() {
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
