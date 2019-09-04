package com.example.charith.trigym.Interfaces;

import org.json.JSONObject;

public interface AsyncListner {

    void onSuccess(JSONObject jsonObject);
    void onError(String error);
}
