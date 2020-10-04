package com.example.charith.trigym.Activities.Member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.charith.trigym.Activities.MainActivity;
import com.example.charith.trigym.Adapters.DeactivatedMembersAdapter;
import com.example.charith.trigym.AsyncTasks.Member.DeleteMemberAsync;
import com.example.charith.trigym.AsyncTasks.Member.UpdateMemberAsync;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.Interfaces.MemberSelectListner;
import com.example.charith.trigym.Interfaces.SuccessListner;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.charith.trigym.Utils.getDeactiveMembers;

public class DeactivateMemberListActivity extends AppCompatActivity {
    private RecyclerView memberListRecycleView;
    List<Member> memberList;
    Gson gson;
    DeactivatedMembersAdapter memberAdapter;
    SearchView searchView;
    ImageButton backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deactivated_memberlist_activity);

        init();
    }

    private void init() {
        backBtn = findViewById(R.id.backBtn);
        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(false);
        memberListRecycleView = findViewById(R.id.memberList);
        memberListRecycleView.setLayoutManager(new LinearLayoutManager(this));

        memberList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();
        loadMemberList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                memberAdapter.filter(newText);

                return false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadMemberList() {

        final DatabaseHandler databaseHandler = new DatabaseHandler(DeactivateMemberListActivity.this);
        memberList = getDeactiveMembers(databaseHandler.getAllMembers());
        memberAdapter = new DeactivatedMembersAdapter(DeactivateMemberListActivity.this, memberList, new MemberSelectListner() {
            @Override
            public void selectMember(Member member, String changeStatus) {
                if (changeStatus.equals("activate")) {

                    member.setMember_active_status(true);
                    member.setModified_at(Utils.dateTimeToString(DateTime.now()));
                    databaseHandler.updateMember(member);

//                    if (Utils.isDeviceOnline(DeactivateMemberListActivity.this)) {
//                        new UpdateMemberAsync(DeactivateMemberListActivity.this, member, new AsyncListner() {
//                            @Override
//                            public void onSuccess(Context context, JSONObject jsonObject) {
//                                Utils.showSuccessMessage(context, getResources().getString(R.string.successfully_activated_message));
//                            }
//
//                            @Override
//                            public void onError(Context context, String error) {
//                                Utils.showWarningMessage(context, error);
//                            }
//                        }).execute();
//                    }

                    memberList = getDeactiveMembers(databaseHandler.getAllMembers());
                    memberAdapter.updateList(memberList);

                } else {
                    databaseHandler.deleteMember(String.valueOf(member.getMember_id()));

                    new DeleteMemberAsync(DeactivateMemberListActivity.this, member, new SuccessListner() {
                        @Override
                        public void onFinished() {
                            memberAdapter.notifyDataSetChanged();
                        }
                    }).execute();



                }
            }
        });

        memberListRecycleView.setAdapter(memberAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeactivateMemberListActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
