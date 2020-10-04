package com.example.charith.trigym.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charith.trigym.Adapters.ScheduleAdapter;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Schedule;
import com.example.charith.trigym.Entities.Schedule;
import com.example.charith.trigym.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListActivity extends AppCompatActivity {

    private static final String TAG = "TriGym";
    private RecyclerView scheduleListRecycleView;
    List<Schedule> scheduleList=new ArrayList<>();
    Gson gson;
    ScheduleAdapter scheduleAdapter;

    LinearLayoutManager layoutManager;

    DatabaseHandler databaseHandler;

    String memberId;

    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        memberId = getIntent().getStringExtra("memberString");

        init();

    }

    private void init() {

        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

        databaseHandler = new DatabaseHandler(this);

        backBtn = findViewById(R.id.backBtn);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        scheduleListRecycleView = findViewById(R.id.recycleView);
        scheduleListRecycleView.setHasFixedSize(true);

        backBtn.setOnClickListener(v -> onBackPressed());


        setValues();
    }

    private void setValues() {

//        scheduleList = databaseHandler.getSchedulesByMemberId(memberId);
//        scheduleAdapter = new ScheduleAdapter(scheduleList, ScheduleListActivity.this);
//        scheduleListRecycleView.setAdapter(scheduleAdapter);
//        scheduleListRecycleView.setLayoutManager(layoutManager);




    }

}
