package com.example.charith.trigym.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charith.trigym.Convertors.CircleTransform;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.CheckImageClickListner;
import com.example.charith.trigym.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InactiveUserAdapter extends ArrayAdapter<Member> {
    List<Member> memberList;
    Context context;
    CheckImageClickListner listner;
    

    public InactiveUserAdapter(Context context, List<Member> memberList,CheckImageClickListner listner) {
        super(context, 0, memberList);
        this.memberList = memberList;
    this.context=context;
    this.listner=listner;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.matches("");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Member selectParticipant = getItem(position);
        ViewHolder holder = null;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_inactive_member_list_item, parent, false);
            holder = new ViewHolder();
            holder.lawyerName = convertView.findViewById(R.id.tvName);
            holder.lawyerPhone = convertView.findViewById(R.id.tvPhone);
            holder.imageProfile = convertView.findViewById(R.id.imageProfile);
            holder.checkedImage = convertView.findViewById(R.id.checkbox);


            convertView.setTag(holder);
            convertView.setTag(R.id.tvName, holder.lawyerName);
            convertView.setTag(R.id.tvPhone, holder.lawyerPhone);
            convertView.setTag(R.id.imageProfile, holder.imageProfile);
            convertView.setTag(R.id.checkbox, holder.checkedImage);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lawyerName.setText(selectParticipant.getMember_first_name()+selectParticipant.getMember_surname());

        if(selectParticipant.isSelected()){
            holder.checkedImage.setImageDrawable(context.getResources().getDrawable(R.drawable.check_with_white_correct));

        }else {
            holder.checkedImage.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck_bg));

        }

        if (selectParticipant.getMember_mobile_1() != null ) {
            if(isEmpty(selectParticipant.getMember_mobile_1().toString())){
                holder.lawyerPhone.setText("Can't send a message");
            }else {
                holder.lawyerPhone.setText(String.valueOf(selectParticipant.getMember_mobile_1()));
            }
        } else {
            holder.lawyerPhone.setText("Can't send a message");
        }



        if (selectParticipant.getMember_profile_image_url() != null) {
            Picasso.get()
                    .load(selectParticipant.getMember_profile_image_url())
                    .transform(new CircleTransform())
                    .placeholder(R.mipmap.client_phone_icon)
                    .into(holder.imageProfile);
        }


        holder.checkedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(position,memberList.get(position));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView lawyerName;
        TextView lawyerPhone;
        ImageView imageProfile;
        protected ImageView checkedImage;

    }

    @Override
    public int getCount() {
        int size;
        if (memberList != null) {
            size = memberList.size();
        } else {
            size = 0;
        }
        return size;
    }

    @Nullable
    @Override
    public Member getItem(int position) {
        return memberList.get(position);
    }

}
