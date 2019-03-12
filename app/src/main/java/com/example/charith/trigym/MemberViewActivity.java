package com.example.charith.trigym;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

public class MemberViewActivity extends AppCompatActivity {

    TextView tvName, tvmobile, tvAddress, tvNIC, tvDOB, tvAge, tvGender, tvHeight, tvWeight, tvHealthConditiom;

    DatabaseHandler databaseHandler;

    Member member;
    Gson gson;
    Address address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_view);

        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        String memberString = getIntent().getStringExtra("selectedMember");
        member = gson.fromJson(memberString, Member.class);


        init();
    }

    private void init() {
        tvName = findViewById(R.id.tvName);
        tvmobile = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvNIC = findViewById(R.id.tvNIC);
        tvDOB = findViewById(R.id.tvDOB);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvHealthConditiom = findViewById(R.id.tvHealthCondition);

        databaseHandler = new DatabaseHandler(MemberViewActivity.this);

        address = databaseHandler.getAddressById(String.valueOf(member.getAddressId()));
        setValues();


    }

    private void setValues() {
        tvName.setText(member.getFirstName() + " " + member.getLastName());
        tvAddress.setText(getAddress(address));
        tvmobile.setText(getMobile(String.valueOf(member.getMobile1()), String.valueOf(member.getMobile2())));
        tvNIC.setText(member.getNIC());
        tvDOB.setText(member.getDOB());
        tvAge.setText(String.valueOf(member.getAge()));
        tvGender.setText(member.getGender());
        tvHeight.setText(String.valueOf(member.getHeight() + " cm"));
        tvWeight.setText(String.valueOf(member.getWeight() + " kg"));





    }

    private String getAddress(Address address) {

        StringBuilder addressString = new StringBuilder();

        if (address.getLine1() != null) {
            addressString.append(address.getLine1());
        }

        if (address.getLine2() != null) {
            addressString.append(", " + address.getLine2());
        }

        if (address.getLine3() != null) {
            addressString.append(", " + address.getLine3());
        }

        if (address.getCity() != null) {
            addressString.append(", " + address.getCity());
        }

        return addressString.toString();
    }

    private String getMobile(String mobile1, String mobile2) {

        StringBuilder addressString = new StringBuilder();

        if (mobile1 != null) {
            addressString.append(mobile1);
        }

        if (mobile2 != null) {
            addressString.append(", " + mobile2);
        }

        return addressString.toString();
    }
}
