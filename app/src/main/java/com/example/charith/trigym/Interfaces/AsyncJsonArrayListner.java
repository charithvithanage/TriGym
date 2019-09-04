package com.example.charith.trigym.Interfaces;

import android.content.Context;

import org.json.JSONArray;

public interface AsyncJsonArrayListner {

    void onSuccess(Context context,JSONArray jsonArray);
    void onError(Context context,String error);
}
