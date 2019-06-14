package com.example.charith.trigym.Test;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MyBaseActivity extends Activity {
    public static final long DISCONNECT_TIMEOUT = 5000; // 5 min = 5 * 60 * 1000 ms


    private Handler disconnectHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // todo
            return true;
        }
    });

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            Toast.makeText(getApplicationContext(),"App is idle",Toast.LENGTH_SHORT).show();
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}
