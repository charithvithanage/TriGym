package com.example.charith.trigym.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.charith.trigym.Activities.Member.MemberViewActivity;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Convertors.CircleTransform;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.MemberSelectListner;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class DeactivatedMembersAdapter extends RecyclerSwipeAdapter<DeactivatedMembersAdapter.SimpleViewHolder> {
    List<Member> filteredMembers;
    List<Member> members;
    Context context;
    Member selectedMember;
    int selectedPosition;
    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
    Gson gson = builder.create();
    MemberSelectListner listner;

    public DeactivatedMembersAdapter(Context context, List<Member> members, MemberSelectListner listner) {
        this.filteredMembers = members;
        this.members = members;
        this.context = context;
        this.listner = listner;
    }

    @Override
    public DeactivatedMembersAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.deactivated_memberlist_item, parent, false);
        return new DeactivatedMembersAdapter.SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeactivatedMembersAdapter.SimpleViewHolder viewHolder, final int position) {
        final Member memberObj = filteredMembers.get(position);
        viewHolder.memberName.setText(memberObj.getMember_first_name() + " " + memberObj.getMember_last_name());
        viewHolder.memberPhone.setText(String.valueOf(memberObj.getMember_mobile_1()));

        if (memberObj.getMember_profile_image_url() != null) {
            Picasso.get().load(Uri.parse(memberObj.getMember_profile_image_url())).rotate(90).transform(new CircleTransform()).into(viewHolder.imageProfile);
        }


        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper_2));

        viewHolder.memberStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMember = filteredMembers.get(position);
                selectedPosition = position;
                listner.selectMember(memberObj, "activate");
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.swipeLayout.setBackgroundColor(Color.parseColor("#d7d8da"));

                final Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MemberViewActivity.class);
                        Member member = filteredMembers.get(position);
                        i.putExtra("memberId", String.valueOf(member.getMember_id()));
                        context.startActivity(i);
                        viewHolder.swipeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                };

                handler.postDelayed(r, 100);

            }
        });
        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedMember = filteredMembers.get(position);
                selectedPosition = position;

                listner.selectMember(selectedMember, "delete");

                viewHolder.swipeLayout.close();
            }
        });

        viewHolder.emailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedMember = filteredMembers.get(position);
                selectedPosition = position;
                viewHolder.swipeLayout.close();
            }
        });

        viewHolder.messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedMember = filteredMembers.get(position);
                selectedPosition = position;
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("address", selectedMember.getMember_mobile_1());

                sendIntent.setType("vnd.android-dir/mms-sms");
                context.startActivity(sendIntent);
                viewHolder.swipeLayout.close();
            }
        });

        viewHolder.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedMember = filteredMembers.get(position);
                selectedPosition = position;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + selectedMember.getMember_mobile_1()));
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        Utils.showWarningMessage(context, context.getResources().getString(R.string.no_permission_to_call));
                        return;
                    } else {
                        context.startActivity(callIntent);
                    }
                } else {
                    context.startActivity(callIntent);
                }

                viewHolder.swipeLayout.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        int size;
        if (filteredMembers != null) {
            size = filteredMembers.size();
        } else {
            size = 0;
        }
        return size;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {

        return R.id.swipe;
    }

    public void updateList(List<Member> memberList) {
        this.filteredMembers = memberList;
        this.members=memberList;
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView memberName;
        TextView memberPhone;
        TextView memberStatus;

        ImageView imageProfile, messageIcon, deleteIcon, callIcon, emailIcon;
        SwipeLayout swipeLayout;

        public SimpleViewHolder(@NonNull View convertView) {
            super(convertView);
            memberName = convertView.findViewById(R.id.tvName);
            memberPhone = convertView.findViewById(R.id.tvPhone);
            imageProfile = convertView.findViewById(R.id.imageProfile);
            memberStatus = convertView.findViewById(R.id.tvMemberStatus);
            deleteIcon = convertView.findViewById(R.id.delete);
            messageIcon = convertView.findViewById(R.id.message);
            emailIcon = convertView.findViewById(R.id.email);
            callIcon = convertView.findViewById(R.id.call);
            swipeLayout = convertView.findViewById(R.id.swipe);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredMembers = new ArrayList<>();
        if (charText.length() == 0) {
            filteredMembers.addAll(members);
        } else {
            for (Member wp : members) {
                if (wp.getMember_first_name() != null && wp.getMember_last_name() != null && wp.getMember_surname() != null && wp.getMember_nic() != null) {
                    if (Utils.removeSpacesAndToLowerCase(wp.getMember_first_name()).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_last_name())).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_surname())).contains(charText) || Utils.removeSpacesAndToLowerCase(wp.getMember_nic()).contains(charText)) {
                        filteredMembers.add(wp);
                    } else if (wp.getMember_last_name() != null && wp.getMember_surname() != null) {
                        String str = Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_last_name())) + " " + Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_surname()));
                        if (str.toLowerCase(Locale.getDefault()).contains(charText)) {
                            filteredMembers.add(wp);
                        }
                    }
                } else if (wp.getMember_first_name() != null && wp.getMember_last_name() != null && wp.getMember_surname() != null) {
                    if (Utils.removeSpacesAndToLowerCase(wp.getMember_first_name()).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_last_name())).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_surname())).contains(charText)) {
                        filteredMembers.add(wp);
                    } else if (wp.getMember_last_name() != null && wp.getMember_surname() != null) {
                        String str = Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_last_name())) + " " + Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getMember_surname()));
                        if (str.toLowerCase(Locale.getDefault()).contains(charText)) {
                            filteredMembers.add(wp);
                        }
                    }
                } else if (wp.getMember_first_name() != null && wp.getMember_last_name() != null) {
                    if (wp.getMember_first_name().toLowerCase(Locale.getDefault()).contains(charText) || String.valueOf(wp.getMember_last_name()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getMember_first_name() != null) {
                    if (wp.getMember_first_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getMember_last_name() != null) {
                    if (String.valueOf(wp.getMember_last_name()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getMember_surname() != null) {
                    if (String.valueOf(wp.getMember_surname()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getMember_nic() != null) {
                    if (wp.getMember_nic().toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


}
