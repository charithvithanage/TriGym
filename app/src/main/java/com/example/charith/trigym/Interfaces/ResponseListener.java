package com.example.charith.trigym.Interfaces;

import android.content.Context;

public interface ResponseListener {
    void onSuccess(Context context,String data);
    void onError(Context context,String error);
}
