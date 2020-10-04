package com.example.charith.trigym.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Payment> list;
    Context context;


    public ScheduleAdapter(List<Payment> payments, Context context) {
        this.list = payments;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.payment_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

       Payment payment=list.get(position);

        ((MyViewHolder) holder).tvType.setText(payment.getMembership_type());
        ((MyViewHolder) holder).tvAmount.setText(String.valueOf(payment.getPayment_amount()));
        ((MyViewHolder) holder).tvExpiryDate.setText(Utils.dateTimeStringToDateString(payment.getMembership_expiry_date()));
        ((MyViewHolder) holder).tvPaymentDate.setText(Utils.dateTimeStringToDateString(payment.getLast_payment_date()));

    }

    @Override
    public int getItemCount() {
        int size;
        if (list != null) {
            size = list.size();
        } else {
            size = 0;
        }
        return size;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvType;
        TextView tvAmount;
        TextView tvExpiryDate;
        TextView tvPaymentDate;

        public MyViewHolder(View v) {
            super(v);
            tvType = v.findViewById(R.id.tvType);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvExpiryDate = v.findViewById(R.id.tvExpiryDate);
            tvPaymentDate = v.findViewById(R.id.tvPaymentDate);

        }
    }
}