package com.example.charith.trigym.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.charith.trigym.Adapters.PaymentAdapter;
import com.example.charith.trigym.AsyncTasks.Payment.GetPaymentsByMemberAsync;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncJsonArrayListner;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PaymentListActivity extends AppCompatActivity {

    private static final String TAG = "TriGym";
    private RecyclerView paymentListRecycleView;
    List<Payment> paymentList=new ArrayList<>();
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

        memberId = getIntent().getStringExtra("memberIdString");

        init();

    }

    private void init() {

        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

        databaseHandler = new DatabaseHandler(this);

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

        if (Utils.isDeviceOnline(PaymentListActivity.this)) {
            new GetPaymentsByMemberAsync(PaymentListActivity.this, memberId, new AsyncJsonArrayListner() {
                @Override
                public void onSuccess(Context context, JSONArray jsonArray) {


                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            Payment payment = gson.fromJson(jsonArray.getString(i), Payment.class);
                            paymentList.add(payment);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    paymentAdapter = new PaymentAdapter(paymentList, PaymentListActivity.this);
                    paymentListRecycleView.setAdapter(paymentAdapter);
                    paymentListRecycleView.setLayoutManager(layoutManager);
                }

                @Override
                public void onError(Context context, String error) {
                    Log.d(TAG, error);
                }
            }).execute();
        } else {
            paymentList = databaseHandler.getPaymentsByMemberId(memberId);
            paymentAdapter = new PaymentAdapter(paymentList, PaymentListActivity.this);
            paymentListRecycleView.setAdapter(paymentAdapter);
            paymentListRecycleView.setLayoutManager(layoutManager);
        }




    }

}
