package com.example.charith.trigym.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.charith.trigym.ApiService;
import com.example.charith.trigym.Convertors.DateTimeSerializer;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.DatePickerFragment;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

public class MemberBioActivity extends AppCompatActivity {

    Switch switchDiabetes, switchHighCholesterol, switchHighBloodPressure, switchLowBloodPressure, switchHeartProblem, switchChestPain, switchHeartAttack, switchBreathingProblem, switchFainting, switchBackPain;
    Switch switchMedication, switchIllness, switchPainfulJoints, switchArthritis, switchHernia, switchPayment;
    TextView tvPaymentDate, tvTitle;

    EditText etAmount;
    EditText etSpecialNotes;

    Button btnSave;

    String memberString = null;
    Member member = null;
    Gson gson;

    String navigationType;

    RadioButton radioButton1Day, radioButton1Week, radioButton1Month, radioButton3Month, radioButton6Month, radioButton12Month;

    ImageButton backBtn;

    LinearLayout paymentSection;

    RadioGroup radioGroupType;
    private String TAG = "TriGym";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_bio);
        memberString = getIntent().getStringExtra("memberString");
        navigationType = getIntent().getStringExtra("navigationType");

        init();
    }

    private void init() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        member = gson.fromJson(memberString, Member.class);
        radioGroupType = findViewById(R.id.radioGroupType);

        backBtn = findViewById(R.id.backBtn);
        paymentSection = findViewById(R.id.paymentSection);
        paymentSection.setVisibility(View.GONE);

        tvTitle = findViewById(R.id.tvTitle);
        radioButton1Day = findViewById(R.id.radioDaily);
        radioButton1Week = findViewById(R.id.radio1Week);
        radioButton1Month = findViewById(R.id.radio1Month);
        radioButton3Month = findViewById(R.id.radio3Month);
        radioButton6Month = findViewById(R.id.radio6Month);
        radioButton12Month = findViewById(R.id.radio12Month);

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

        member = gson.fromJson(memberString, Member.class);


        if (navigationType.equals("edit")) {
            tvTitle.setText(getResources().getString(R.string.edit_user_title));
            setUserValues();
        } else {
            tvTitle.setText(getResources().getString(R.string.new_user_title));

        }


        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedTypeRadioButton = group.getCheckedRadioButtonId();
                RadioButton memberType = findViewById(selectedTypeRadioButton);

                getMembershipAmountFromType(memberType.getText().toString());
            }
        });

        switchPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                member.setMember_valid_status(b);

                if (b) {

                    paymentSection.setVisibility(View.VISIBLE);
                    int selectedTypeRadioButton = radioGroupType.getCheckedRadioButtonId();
                    RadioButton memberType = findViewById(selectedTypeRadioButton);

                    getMembershipAmountFromType(memberType.getText().toString());

                    tvPaymentDate.setText(today.toString(getString(R.string.date_pattern)));
                    member.setLast_payment_date(today.toString(getString(R.string.date_time_pattern)));
                } else {
                    paymentSection.setVisibility(View.GONE);
                    etAmount.setText(null);
                    tvPaymentDate.setText(getString(R.string.date_pattern));
                    member.setLast_payment_date(null);
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
                member.setHigh_blood_pressure(isChecked);

            }
        });


        switchLowBloodPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setLow_blood_pressure(isChecked);

            }
        });


        switchHeartProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setHeart_problem(isChecked);

            }
        });

        switchChestPain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setChest_pain(isChecked);

            }
        });

        switchHeartAttack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setHeart_attack(isChecked);

            }
        });

        switchBreathingProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setAsthma(isChecked);

            }
        });

        switchFainting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setFainting_spells(isChecked);

            }
        });

        switchBackPain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setBack_pain(isChecked);

            }
        });

        switchMedication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setMedication(isChecked);

            }
        });

        switchIllness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setOther_illness(isChecked);

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getMembershipAmountFromType(String membershipType) {
        switch (membershipType) {
            case "Daily":
                if (switchPayment.isChecked())
                    etAmount.setText(getResources().getString(R.string.daily_fee));
                break;

            case "1 Week":
                if (switchPayment.isChecked())

                    etAmount.setText(getResources().getString(R.string.week_fee));
                break;

            case "1 Month":
                if (switchPayment.isChecked())

                    etAmount.setText(getResources().getString(R.string.one_month_fee));
                break;

            case "3 Month":
                if (switchPayment.isChecked())

                    etAmount.setText(getResources().getString(R.string.three_month_fee));
                break;


            case "6 Month":

                if (switchPayment.isChecked())
                    etAmount.setText(getResources().getString(R.string.six_month_fee));
                break;

            case "1 Year":
                if (switchPayment.isChecked())
                    etAmount.setText(getResources().getString(R.string.one_year_fee));
                break;
        }
    }

    private void setUserValues() {

        checkMemberType();
        checkHealthCondition();

//        if (member.getValidStatus()) {
//            switchPayment.setChecked(true);
//        } else {
//            switchPayment.setChecked(false);
//        }

//        etAmount.setText(getValidPaymentAmount());
//        tvPaymentDate.setText(member.getLast_payment_date());
        etSpecialNotes.setText(member.getMember_health_condition());


    }


    private void checkHealthCondition() {
        if (member.getDiabetes()) {
            switchDiabetes.setChecked(true);
        }

        if (member.getCholesterol()) {
            switchHighCholesterol.setChecked(true);
        }

        if (member.getHigh_blood_pressure()) {
            switchHighBloodPressure.setChecked(true);
        }

        if (member.getLow_blood_pressure()) {
            switchLowBloodPressure.setChecked(true);
        }

        if (member.getHeart_problem()) {
            switchHeartProblem.setChecked(true);
        }
        if (member.getChest_pain()) {
            switchChestPain.setChecked(true);
        }
        if (member.getHeart_attack()) {
            switchHeartAttack.setChecked(true);
        }
        if (member.getAsthma()) {
            switchBreathingProblem.setChecked(true);
        }
        if (member.getFainting_spells()) {
            switchFainting.setChecked(true);
        }
        if (member.getBack_pain()) {
            switchBackPain.setChecked(true);
        }

        if (member.getMedication()) {
            switchMedication.setChecked(true);
        }

        if (member.getOther_illness()) {
            switchIllness.setChecked(true);
        }

        if (member.getSwollen()) {
            switchPainfulJoints.setChecked(true);
        }

        if (member.getArthritis()) {
            switchArthritis.setChecked(true);
        }

    }

    private void checkMemberType() {
        switch (member.getMembership_type()) {
            case "Daily":
                radioButton1Day.setChecked(true);
                break;

            case "1 Week":
                radioButton1Week.setChecked(true);
                break;

            case "1 Month":
                radioButton1Month.setChecked(true);

                break;

            case "3 Month":
                radioButton3Month.setChecked(true);

                break;

            case "6 Month":
                radioButton6Month.setChecked(true);

                break;

            case "1 Year":
                radioButton12Month.setChecked(true);
                break;
        }
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
            member.setLast_payment_date(tempDate.toString(getString(R.string.date_time_pattern)));


        }
    };

    private void saveMember() {
        member.setMember_health_condition(etSpecialNotes.getText().toString());

        int selectedTypeRadioButton = radioGroupType.getCheckedRadioButtonId();
        RadioButton memberType = findViewById(selectedTypeRadioButton);

        member.setMembership_type(memberType.getText().toString());

        DatabaseHandler databaseHandler = new DatabaseHandler(MemberBioActivity.this);
        Long memberId;

        if (navigationType != null) {
            if (navigationType.equals("edit")) {
                memberId = new Long(member.getMember_id());

                if (Utils.checkMemberValidStatus(MemberBioActivity.this, String.valueOf(memberId))) {
                    member.setMember_valid_status(true);
                } else {
                    member.setMember_valid_status(false);
                }

                member.setModified_at(Utils.dateTimeToString(DateTime.now()));
                databaseHandler.updateMember(member);

                member=databaseHandler.getMemberById(String.valueOf(member.getMember_id()));

                new UpdateMemberAsync().execute();


                Intent intent = new Intent();
                setResult(200, intent);
                finish();//finishing activity

            } else {
                if (member.getLast_payment_date() != null) {
                    Long id = saveMemberLocalStorage(databaseHandler);

                    member=databaseHandler.getMemberById(String.valueOf(id));

                    if (id != 0) {
                        new SaveMemberAsync().execute();
                    }

                    savePaymentToLocalStorage(id, databaseHandler);
                    Intent intent = new Intent(MemberBioActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Utils.showWarningMessage(MemberBioActivity.this, getString(R.string.payment_date_not_select_warning));
                }
            }
        } else {
            Utils.showWarningMessage(MemberBioActivity.this, getString(R.string.navigation_error_warning));
        }
    }

    private void savePaymentToLocalStorage(Long memberId, DatabaseHandler databaseHandler) {
        if (switchPayment.isChecked()) {

            Payment payment = new Payment();

            payment.setMember_id((int) (long) memberId);
            payment.setAmount(Float.valueOf(etAmount.getText().toString()));
            payment.setType(member.getMembership_type());
            payment.setLast_payment_date(member.getLast_payment_date());
            payment.setPaymentExpiryDate(Utils.getMembershipExpiryDate(member));

            databaseHandler.addPayment(payment);

        }
    }

    private Long saveMemberLocalStorage(DatabaseHandler databaseHandler) {
        member.setMember_valid_status(true);
        member.setMember_active_status(true);
        member.setCreated_at(Utils.dateTimeToString(DateTime.now()));
        member.setModified_at(Utils.dateTimeToString(DateTime.now()));
        return databaseHandler.addMember(member);
    }

    private class SaveMemberAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            ApiService.getInstance().saveMember(MemberBioActivity.this, member, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());
                }

                @Override
                public void onSuccess(JSONArray response) {

                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                }
            });
            return null;
        }
    }
    private class UpdateMemberAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            ApiService.getInstance().updateMember(MemberBioActivity.this, member, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());
                    Intent intent = new Intent();
                    setResult(200, intent);
                    finish();//finishing activity
                }

                @Override
                public void onSuccess(JSONArray response) {

                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                }
            });
            return null;
        }
    }





}
