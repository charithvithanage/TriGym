package com.example.charith.trigym.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.charith.trigym.Adapters.PaymentAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.R;
import com.google.gson.Gson;

import java.util.List;

public class PaymentListActivity extends AppCompatActivity {

    private RecyclerView paymentListRecycleView;
    List<Payment> paymentList;
    Gson gson;
    PaymentAdapter paymentAdapter;

    LinearLayoutManager layoutManager;

    DatabaseHandler databaseHandler;

    String memberId;

    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        memberId=getIntent().getStringExtra("memberIdString");

        init();

    }

    private void init() {

        databaseHandler=new DatabaseHandler(this);

        backBtn = findViewById(R.id.backBtn);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        paymentListRecycleView = findViewById(R.id.recycleView);
        paymentListRecycleView.setHasFixedSize(true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setValues();
    }

    private void setValues() {

        paymentList=databaseHandler.getPaymentsByMemberId(memberId);

        paymentAdapter = new PaymentAdapter(paymentList, PaymentListActivity.this);
        paymentListRecycleView.setAdapter(paymentAdapter);
        paymentListRecycleView.setLayoutManager(layoutManager);

    }

}
