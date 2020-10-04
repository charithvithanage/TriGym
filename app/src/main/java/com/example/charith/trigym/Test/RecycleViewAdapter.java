package com.example.charith.trigym.Test;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charith.trigym.R;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    ArrayList<Contact> arrayList = new ArrayList<>();

    public RecycleViewAdapter(ArrayList<Contact> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Contact contact = arrayList.get(i);
        myViewHolder.tvName.setText(contact.getName());
        int sync_status = contact.getSync_status();

        if (sync_status == DbContact.SYNC_STATUS_OK) {
            myViewHolder.img.setImageResource(R.mipmap.correct);
        } else if(sync_status == DbContact.SYNC_STATUS_FAILED){
            myViewHolder.img.setImageResource(R.mipmap.sync);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.etName);
            img = itemView.findViewById(R.id.img);
        }
    }
}
