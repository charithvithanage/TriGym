package com.example.charith.trigym;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.charith.trigym.Entities.Payment;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Payment> list;
    Context context;


    public PaymentAdapter(List<Payment> payments, Context context) {
        list = payments;
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

        ((MyViewHolder) holder).tvType.setText(payment.getType());
        ((MyViewHolder) holder).tvAmount.setText(String.valueOf(payment.getAmount()));
        ((MyViewHolder) holder).tvExpiryDate.setText(payment.getPaymentExpiryDate());
        ((MyViewHolder) holder).tvPaymentDate.setText(payment.getLastPaymentDate());

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
            tvAmount = v.findViewById(R.id.tvType);
            tvExpiryDate = v.findViewById(R.id.tvExpiryDate);
            tvPaymentDate = v.findViewById(R.id.tvPaymentDate);

        }
    }
}