package com.example.charith.trigym.Interfaces;

import android.content.Context;

import org.json.JSONObject;

public interface AsyncListner {

    void onSuccess(Context context,JSONObject jsonObject);
    void onError(Context context,String error);
}
