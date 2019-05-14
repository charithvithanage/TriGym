package com.example.charith.trigym;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

public class MemberBioActivity extends AppCompatActivity {

    Switch switchDiabetes, switchHighCholesterol, switchHighBloodPressure, switchLowBloodPressure, switchHeartProblem, switchChestPain, switchHeartAttack, switchBreathingProblem, switchFainting, switchBackPain;
    Switch switchMedication, switchIllness, switchPainfulJoints, switchArthritis, switchHernia, switchPayment;
    TextView tvPaymentDate;

    EditText etAmount;
    EditText etSpecialNotes;

    Button btnSave;

    String memberString = null;
    Member member = null;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_bio);
        memberString = getIntent().getStringExtra("memberString");
        memberString = getIntent().getStringExtra("memberString");
        init();
    }

    private void init() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        member = gson.fromJson(memberString, Member.class);
        tvPaymentDate = findViewById(R.id.tvPaymentDate);
        etAmount = findViewById(R.id.etAmount);

        switchPayment = findViewById(R.id.switchPayment);
        switchDiabetes = findViewById(R.id.switchDiabetes);
        switchHighCholesterol = findViewById(R.id.switchHighCholesterol);
        switchHighBloodPressure = findViewById(R.id.switchHighBloodPressure);
        switchLowBloodPressure = findViewById(R.id.switchLowBloodPressure);
        switchHeartProblem = findViewById(R.id.switchHeartProblem);
        switchChestPain = findViewById(R.id.switchChestPain);
        switchHeartAttack = findViewById(R.id.switchHeartAttack);
        switchBreathingProblem = findViewById(R.id.switchBreathingProblem);
        switchFainting = findViewById(R.id.switchFainting);
        switchBackPain = findViewById(R.id.switchBackPain);
        switchMedication = findViewById(R.id.switchMedication);
        switchIllness = findViewById(R.id.switchIllness);
        switchPainfulJoints = findViewById(R.id.switchPainfulJoints);
        switchArthritis = findViewById(R.id.switchArthritis);
        switchHernia = findViewById(R.id.switchHernia);
        etSpecialNotes = findViewById(R.id.etSpecialNotes);
        btnSave = findViewById(R.id.btnSave);

        switchPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                member.setValidStatus(b);

                if (b) {
                    tvPaymentDate.setText(today.toString(getString(R.string.date_pattern)));
                    member.setLastPaymentDate(today.toString(getString(R.string.date_pattern)));
                } else {
                    tvPaymentDate.setText(getString(R.string.date_pattern));
                    member.setLastPaymentDate(null);
                }
            }
        });

        tvPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPaymentDate();
            }
        });

        switchDiabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setDiabetes(isChecked);
            }
        });


        switchHighCholesterol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setCholesterol(isChecked);

            }
        });


        switchHighBloodPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setHighBloodPressure(isChecked);

            }
        });


        switchLowBloodPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setLowBloodPressure(isChecked);

            }
        });


        switchHeartProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setHeartProblem(isChecked);

            }
        });

        switchChestPain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setPainInChestWhenExercising(isChecked);

            }
        });

        switchHeartAttack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setHeartAttackCoronaryBypass(isChecked);

            }
        });

        switchBreathingProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setAnyBreathingDifficultiesAndAsthma(isChecked);

            }
        });

        switchFainting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setFaintingSpells(isChecked);

            }
        });

        switchBackPain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setBackOrSpinePains(isChecked);

            }
        });

        switchMedication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setAreYouOnAnySortOfMedications(isChecked);

            }
        });

        switchIllness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setOtherSignificantIllness(isChecked);

            }
        });

        switchPainfulJoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setSwollen(isChecked);

            }
        });

        switchArthritis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setArthritis(isChecked);

            }
        });

        switchHernia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setHernia(isChecked);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMember();
            }
        });


    }

    DateTime today = new DateTime();

    private void selectPaymentDate() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */

        Bundle args = new Bundle();
        args.putInt("year", today.getYear());
        args.putInt("month", today.getMonthOfYear() - 1);
        args.putInt("day", today.getDayOfMonth());

        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(paymentDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener paymentDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            final DateTime tempDate = new DateTime(year, monthOfYear + 1, dayOfMonth, today.getHourOfDay(), today.getMinuteOfHour());

            tvPaymentDate.setText(tempDate.toString(getString(R.string.date_pattern)));
            member.setLastPaymentDate(tempDate.toString(getString(R.string.date_pattern)));


        }
    };

    private void saveMember() {
        member.setComments(etSpecialNotes.getText().toString());
        final RadioGroup radioGroupType = findViewById(R.id.radioGroupType);

        int selectedTypeRadioButton = radioGroupType.getCheckedRadioButtonId();
        RadioButton memberType = findViewById(selectedTypeRadioButton);

        member.setMembershipType(memberType.getText().toString());

        DatabaseHandler databaseHandler = new DatabaseHandler(MemberBioActivity.this);
        Long memberId = databaseHandler.addMember(member);

        Payment payment = new Payment();

        payment.setMember_id((int) (long) memberId);
        payment.setAmount(Float.valueOf(etAmount.getText().toString()));
        payment.setType(member.getMembershipType());
        payment.setLastPaymentDate(member.getLastPaymentDate());
        payment.setPaymentExpiryDate(member.getMembershipExpiredDate());

        databaseHandler.addPayment(payment);

        Intent intent = new Intent(MemberBioActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
