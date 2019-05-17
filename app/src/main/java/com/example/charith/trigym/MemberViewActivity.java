package com.example.charith.trigym;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

public class MemberViewActivity extends AppCompatActivity {

    TextView tvName, tvMobile1, tvMobile2, tvEmail, tvMarriedStatus, tvAddress, tvNIC, tvDOB, tvAge, tvGender, tvHeight, tvWeight, tvComments;
    TextView tvDiabetes, tvHighCholesterol, tvHighBloodPressure, tvLowBloodPressure, tvHeartProblem, tvChestPain, tvHeartAttack, tvBreathingProblem, tvFainting, tvBackPain;
    TextView tvMedication, tvIllness, tvPainfulJoints, tvArthritis, tvHernia;
    DatabaseHandler databaseHandler;

    Member member;
    Gson gson;
    Address address = null;

    LinearLayout mobile1Layout, mobile2Layout, emailLayout;

    View imgCall1, imgCall2, imgMessage1, imgMessage2, imgEmail;

    ImageButton editButton;

    Button btnPaymentHistory;

    ImageView profileImage;

    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_view);


        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        String memberId=getIntent().getStringExtra("memberId");
        DatabaseHandler databaseHandler=new DatabaseHandler(this);

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

        tvDiabetes = findViewById(R.id.tvDiabetes);
        tvHighCholesterol = findViewById(R.id.tvHighCholesterol);
        tvHighBloodPressure = findViewById(R.id.tvHighBloodPressure);
        tvLowBloodPressure = findViewById(R.id.tvComtvLowBloodPressurements);
        tvHeartProblem = findViewById(R.id.tvComtvHeartProblemments);
        tvChestPain = findViewById(R.id.tvChestPain);
        tvHeartAttack = findViewById(R.id.tvHeartAttack);
        tvBreathingProblem = findViewById(R.id.tvCtvBreathingProblemomments);
        tvFainting = findViewById(R.id.tvCommenttvFaintings);
        tvBackPain = findViewById(R.id.tvBackPain);
        tvMedication = findViewById(R.id.tvMedication);
        tvIllness = findViewById(R.id.tvIllness);
        tvPainfulJoints = findViewById(R.id.tvPainfulJoints);
        tvArthritis = findViewById(R.id.tvArthritis);
        tvHernia = findViewById(R.id.tvCommetvHerniants);

        databaseHandler = new DatabaseHandler(MemberViewActivity.this);

        address = databaseHandler.getAddressById(String.valueOf(member.getAddressId()));
        setValues();

        imgCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(member.getMobile1());
            }
        });


        imgCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(member.getMobile2());
            }
        });


        imgMessage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message(member.getMobile1());
            }
        });


        imgMessage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message(member.getMobile2());

            }
        });

        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email(member.getEmail());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        btnPaymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MemberViewActivity.this,PaymentListActivity.class);
                intent.putExtra("memberIdString",String.valueOf(member.getId()));
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberViewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void editProfile() {
        Intent intent = new Intent(MemberViewActivity.this, NewMemberActivity.class);
        intent.putExtra("navigationType","edit");
        intent.putExtra("memberId",String.valueOf(member.getId()));
        intent.putExtra("memberType",member.getType());
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200){

            member=databaseHandler.getMemberById(String.valueOf(member.getId()));
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
        tvName.setText(member.getFirstName() + " " + member.getLastName() + " " + member.getSurName());
        tvAddress.setText(getAddress(address));

        if (member.getMobile1() != null || member.getMobile1() != 0) {
            tvMobile1.setText(String.valueOf(member.getMobile1()));
        } else {
            mobile1Layout.setVisibility(View.GONE);
        }

        if (member.getMobile2() != null || member.getMobile2() != 0) {
            tvMobile2.setText(String.valueOf(member.getMobile2()));

        } else {
            mobile2Layout.setVisibility(View.GONE);
        }

        if (member.getEmail() != null) {
            tvEmail.setText(member.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if(member.getProfileImage()!=null){
            Picasso.get().load(Uri.parse(member.getProfileImage())).transform(new CircleTransform()).into(profileImage);

        }

        tvNIC.setText(member.getNIC());
        tvDOB.setText(member.getDOB());
        tvAge.setText(String.valueOf(member.getAge()));
        tvGender.setText(member.getGender());
        tvMarriedStatus.setText(member.getMarriedStatus());
        tvHeight.setText(String.valueOf(member.getHeight() + " cm"));
        tvWeight.setText(String.valueOf(member.getWeight() + " kg"));

        tvDiabetes.setText(checkCondition(member.getDiabetes()));
        tvHighCholesterol.setText(checkCondition(member.getCholesterol()));
        tvHighBloodPressure.setText(checkCondition(member.getHighBloodPressure()));
        tvLowBloodPressure.setText(checkCondition(member.getLowBloodPressure()));
        tvHeartProblem.setText(checkCondition(member.getHeartProblem()));
        tvChestPain.setText(checkCondition(member.getPainInChestWhenExercising()));
        tvHeartAttack.setText(checkCondition(member.getHeartAttackCoronaryBypass()));
        tvBreathingProblem.setText(checkCondition(member.getAnyBreathingDifficultiesAndAsthma()));
        tvFainting.setText(checkCondition(member.getFaintingSpells()));
        tvBackPain.setText(checkCondition(member.getBackOrSpinePains()));
        tvMedication.setText(checkCondition(member.getAreYouOnAnySortOfMedications()));
        tvIllness.setText(checkCondition(member.getOtherSignificantIllness()));
        tvPainfulJoints.setText(checkCondition(member.getBackOrSpinePains()));
        tvArthritis.setText(checkCondition(member.getArthritis()));
        tvHernia.setText(checkCondition(member.getHernia()));


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

    private String getAddress(Address address) {

        StringBuilder addressString = new StringBuilder();

        if (address.getLine1() != null || address.getLine1() != "") {
            addressString.append(address.getLine1());
        }

        if (address.getLine2() != null || address.getLine2() != "") {
            addressString.append(", " + address.getLine2());
        }

        if (address.getLine3() != null || address.getLine3() != "") {
            addressString.append(", " + address.getLine3());
        }

        if (address.getCity() != null || address.getCity() != "") {
            addressString.append(", " + address.getCity());
        }

        return addressString.toString();
    }

}
