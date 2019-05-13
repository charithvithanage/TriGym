package com.example.charith.trigym;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.google.gson.Gson;

import java.util.List;

public class PaymentListActivity extends AppCompatActivity {

    private RecyclerView paymentListRecycleView;
    List<Payment> paymentList;
    Gson gson;
    PaymentAdapter paymentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
    }

}
