package com.example.charith.trigym.Activities.Member;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charith.trigym.Activities.MainActivity;
import com.example.charith.trigym.Activities.PaymentListActivity;
import com.example.charith.trigym.Activities.ScheduleListActivity;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Convertors.CircleTransform;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

public class MemberViewActivity extends AppCompatActivity {

    TextView tvName, tvMobile1, tvMobile2, tvEmail, tvMarriedStatus, tvAddress, tvNIC, tvDOB, tvAge, tvGender, tvHeight, tvWeight, tvComments, tvType, tvCategory;

    DatabaseHandler databaseHandler;

    Member member;
    Gson gson;

    LinearLayout mobile1Layout, mobile2Layout, emailLayout;

    View imgCall1, imgCall2, imgMessage1, imgMessage2, imgEmail;

    ImageButton editButton;

    Button btnPaymentHistory,btnSchedules;

    ImageView profileImage;

    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_view);


        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

        String memberId = getIntent().getStringExtra("memberId");
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        member = databaseHandler.getMemberById(memberId);
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted you can send sms", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(getApplicationContext(), "Permission not granted you can't send sms", Toast.LENGTH_SHORT).show();

                }
                return;
            }
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted you can call", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(getApplicationContext(), "Permission not granted you can't get call", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }


    private void init() {

        backBtn = findViewById(R.id.backBtn);

        profileImage = findViewById(R.id.profileImage);
        editButton = findViewById(R.id.editBtn);
        btnPaymentHistory = findViewById(R.id.btnHistory);
        btnSchedules = findViewById(R.id.btnShedules);

        mobile1Layout = findViewById(R.id.mobile1Layout);
        mobile2Layout = findViewById(R.id.mobile2Layout);
        emailLayout = findViewById(R.id.emailLayout);

        imgCall1 = findViewById(R.id.imgCall1);
        imgCall2 = findViewById(R.id.imgCall2);
        imgMessage1 = findViewById(R.id.imgMessage1);
        imgMessage2 = findViewById(R.id.imgMessage2);
        imgEmail = findViewById(R.id.imgEmail);

        tvName = findViewById(R.id.tvName);
        tvMobile1 = findViewById(R.id.tvMobile1);
        tvMobile2 = findViewById(R.id.tvMobile2);
        tvEmail = findViewById(R.id.tvEmail);
        tvMarriedStatus = findViewById(R.id.tvMarriedStatus);
        tvAddress = findViewById(R.id.tvAddress);
        tvNIC = findViewById(R.id.tvNIC);
        tvDOB = findViewById(R.id.tvDOB);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvComments = findViewById(R.id.tvComments);
        tvType = findViewById(R.id.tvType);
        tvCategory = findViewById(R.id.tvCategory);

        databaseHandler = new DatabaseHandler(MemberViewActivity.this);

        setValues();

        imgCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(member.getMember_mobile_1());
            }
        });


        imgCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(member.getMember_mobile_2());
            }
        });


        imgMessage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message(member.getMember_mobile_1());
            }
        });


        imgMessage2.setOnClickListener(v -> message(member.getMember_mobile_2()));

        imgEmail.setOnClickListener(v -> email(member.getEmail()));

        editButton.setOnClickListener(v -> editProfile());

        btnPaymentHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MemberViewActivity.this, PaymentListActivity.class);
            intent.putExtra("memberIdString", String.valueOf(member.getMember_id()));
            startActivity(intent);
        });

        btnSchedules.setOnClickListener(v -> {
            Intent intent = new Intent(MemberViewActivity.this, ScheduleListActivity.class);
            intent.putExtra("memberString", gson.toJson(member));
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MemberViewActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }

    private void editProfile() {
        Intent intent = new Intent(MemberViewActivity.this, NewMemberActivity.class);
        intent.putExtra("navigationType", "edit");
        intent.putExtra("memberId", String.valueOf(member.getMember_id()));
        intent.putExtra("memberType", member.getType());
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {

            member = databaseHandler.getMemberById(String.valueOf(member.getMember_id()));
            setValues();
        }

    }

    private void email(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        //need this to prompts email client only
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));

    }

    private void message(Integer mobile1) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.putExtra("address", String.valueOf(mobile1));
            i.setType("vnd.android-dir/mms-sms");
            startActivity(i);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }

    }

    private void call(Integer mobile1) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + String.valueOf(mobile1)));
            startActivity(callIntent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
            }
        }

    }

    private void setValues() {


        tvName.setText(member.getMember_first_name() + " " + member.getMember_last_name() + " " + member.getMember_surname());
        tvAddress.setText(getAddress(member));

        tvCategory.setText(member.getCategory());
        tvType.setText(member.getType());

        if (member.getMember_mobile_1() != null || member.getMember_mobile_1() != 0) {
            tvMobile1.setText(String.valueOf(member.getMember_mobile_1()));
        } else {
            mobile1Layout.setVisibility(View.GONE);
        }

        if (member.getMember_mobile_2() != null) {
            if (member.getMember_mobile_2() != 0) {
                tvMobile2.setText(String.valueOf(member.getMember_mobile_2()));
            } else {
                mobile2Layout.setVisibility(View.GONE);
            }
        } else {
            mobile2Layout.setVisibility(View.GONE);
        }

        if (member.getEmail() != null) {
            tvEmail.setText(member.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if (member.getMember_profile_image_url() != null) {
            Picasso.get().load(Uri.parse(member.getMember_profile_image_url())).transform(new CircleTransform()).rotate(90).into(profileImage);

        }

        tvNIC.setText(member.getMember_nic());
        tvDOB.setText(Utils.dateToString(Utils.stringToDateTime(member.getMember_dob())));
        tvAge.setText(String.valueOf(member.getMember_age()));
        tvGender.setText(member.getMember_gender());
        tvMarriedStatus.setText(member.getMember_married_status());
        tvHeight.setText(member.getMember_height() + " cm");
        tvWeight.setText(member.getMember_weight() + " kg");

    }

    private String checkCondition(Boolean diabetes) {

        String string = "NO";

        if (diabetes != null) {
            if (diabetes) {
                string = "YES";
            }
        }


        return string;
    }

    private String getAddress(Member member) {

        StringBuilder addressString = new StringBuilder();

        if (member.getAddress_line_1() != null && member.getAddress_line_1() != "") {
            addressString.append(member.getAddress_line_1());
        }

        if (member.getAddress_line_2() != null  &&  member.getAddress_line_2() != "") {
            addressString.append(", " + member.getAddress_line_2());
        }

        if (member.getAddress_line_3() != null  &&  member.getAddress_line_3() != "") {
            addressString.append(", " + member.getAddress_line_3());
        }

        if (member.getCity() != null  &&  member.getCity() != "") {
            addressString.append(", " + member.getCity());
        }

        return addressString.toString();
    }

}
