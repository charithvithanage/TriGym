package com.example.charith.trigym.Adapters;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.charith.trigym.Activities.MemberViewActivity;
import com.example.charith.trigym.Convertors.CircleTransform;
import com.example.charith.trigym.Convertors.DateTimeSerializer;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.MemberSelectListner;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class DeactivatedMembersAdapter extends RecyclerSwipeAdapter<DeactivatedMembersAdapter.SimpleViewHolder> {
    List<Member> filteredMembers;
    List<Member> members;
    Context context;
    Member selectedMember;
    int selectedPosition;
    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer());
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
        viewHolder.memberName.setText(memberObj.getFirstName() + " " + memberObj.getLastName());
        viewHolder.memberPhone.setText(String.valueOf(memberObj.getMobile1()));

        if (memberObj.getProfileImage() != null) {
            Picasso.get().load(Uri.parse(memberObj.getProfileImage())).transform(new CircleTransform()).into(viewHolder.imageProfile);
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
                        i.putExtra("memberId", String.valueOf(member.getId()));
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
                sendIntent.putExtra("address", selectedMember.getMobile1());

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
                callIntent.setData(Uri.parse("tel:" + selectedMember.getMobile1()));
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
                if (wp.getFirstName() != null && wp.getLastName() != null && wp.getSurName() != null && wp.getNIC() != null) {
                    if (Utils.removeSpacesAndToLowerCase(wp.getFirstName()).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getLastName())).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getSurName())).contains(charText) || Utils.removeSpacesAndToLowerCase(wp.getNIC()).contains(charText)) {
                        filteredMembers.add(wp);
                    } else if (wp.getLastName() != null && wp.getSurName() != null) {
                        String str = Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getLastName())) + " " + Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getSurName()));
                        if (str.toLowerCase(Locale.getDefault()).contains(charText)) {
                            filteredMembers.add(wp);
                        }
                    }
                } else if (wp.getFirstName() != null && wp.getLastName() != null && wp.getSurName() != null) {
                    if (Utils.removeSpacesAndToLowerCase(wp.getFirstName()).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getLastName())).contains(charText) || Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getSurName())).contains(charText)) {
                        filteredMembers.add(wp);
                    } else if (wp.getLastName() != null && wp.getSurName() != null) {
                        String str = Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getLastName())) + " " + Utils.removeSpacesAndToLowerCase(String.valueOf(wp.getSurName()));
                        if (str.toLowerCase(Locale.getDefault()).contains(charText)) {
                            filteredMembers.add(wp);
                        }
                    }
                } else if (wp.getFirstName() != null && wp.getLastName() != null) {
                    if (wp.getFirstName().toLowerCase(Locale.getDefault()).contains(charText) || String.valueOf(wp.getLastName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getFirstName() != null) {
                    if (wp.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getLastName() != null) {
                    if (String.valueOf(wp.getLastName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getSurName() != null) {
                    if (String.valueOf(wp.getSurName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                } else if (wp.getNIC() != null) {
                    if (wp.getNIC().toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredMembers.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


}
