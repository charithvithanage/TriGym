package com.example.charith.trigym.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.example.charith.trigym.AsyncTasks.SaveAddressAsync;
import com.example.charith.trigym.AsyncTasks.SaveHealthConditionAsync;
import com.example.charith.trigym.AsyncTasks.SaveMemberAsync;
import com.example.charith.trigym.AsyncTasks.SavePaymentAsync;
import com.example.charith.trigym.AsyncTasks.UpdateAddressAsync;
import com.example.charith.trigym.AsyncTasks.UpdateHealthConditionAsync;
import com.example.charith.trigym.AsyncTasks.UpdateMemberAsync;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.DatePickerFragment;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.HealthCondition;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.AsyncListner;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class MemberBioActivity extends AppCompatActivity {

    Switch switchDiabetes, switchHighCholesterol, switchHighBloodPressure, switchLowBloodPressure, switchHeartProblem, switchChestPain, switchHeartAttack, switchBreathingProblem, switchFainting, switchBackPain;
    Switch switchMedication, switchIllness, switchPainfulJoints, switchArthritis, switchHernia, switchPayment;
    TextView tvPaymentDate, tvTitle;

    EditText etAmount;
    EditText etSpecialNotes;

    Button btnSave;

    String memberString = null;
    String addressString = null;
    Member member = null;
    Address address = null;
    Gson gson;

    String navigationType;

    RadioButton radioButton1Day, radioButton1Week, radioButton1Month, radioButton3Month, radioButton6Month, radioButton12Month;

    ImageButton backBtn;

    LinearLayout paymentSection;

    RadioGroup radioGroupType;
    private String TAG = "TriGym";

    HealthCondition healthCondition;

    DatabaseHandler databaseHandler;

    AtomicInteger atomicInteger;

    Integer counterInteger = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_bio);
        memberString = getIntent().getStringExtra("memberString");
        addressString = getIntent().getStringExtra("addressString");
        navigationType = getIntent().getStringExtra("navigationType");

        databaseHandler = new DatabaseHandler(MemberBioActivity.this);

        init();
    }

    private void init() {

        atomicInteger = new AtomicInteger(0);

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

        member = gson.fromJson(memberString, Member.class);
        address = gson.fromJson(addressString, Address.class);

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
            healthCondition = new HealthCondition();
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
                healthCondition.setDiabetes(isChecked);
            }
        });


        switchHighCholesterol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setCholesterol(isChecked);

            }
        });


        switchHighBloodPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setHigh_blood_pressure(isChecked);

            }
        });


        switchLowBloodPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setLow_blood_pressure(isChecked);

            }
        });


        switchHeartProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setHeart_problem(isChecked);

            }
        });

        switchChestPain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setChest_pain(isChecked);

            }
        });

        switchHeartAttack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setHeart_attack(isChecked);

            }
        });

        switchBreathingProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setAsthma(isChecked);

            }
        });

        switchFainting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setFainting_spells(isChecked);

            }
        });

        switchBackPain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setBack_pain(isChecked);

            }
        });

        switchMedication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setMedication(isChecked);

            }
        });

        switchIllness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setOther_illness(isChecked);

            }
        });

        switchPainfulJoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setSwollen(isChecked);

            }
        });

        switchArthritis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setArthritis(isChecked);

            }
        });

        switchHernia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                healthCondition.setHernia(isChecked);

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

        healthCondition = databaseHandler.getHealthConditionById(String.valueOf(member.getHealth_condition_id()));

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
        if (healthCondition.getDiabetes()) {
            switchDiabetes.setChecked(true);
        }

        if (healthCondition.getCholesterol()) {
            switchHighCholesterol.setChecked(true);
        }

        if (healthCondition.getHigh_blood_pressure()) {
            switchHighBloodPressure.setChecked(true);
        }

        if (healthCondition.getLow_blood_pressure()) {
            switchLowBloodPressure.setChecked(true);
        }

        if (healthCondition.getHeart_problem()) {
            switchHeartProblem.setChecked(true);
        }
        if (healthCondition.getChest_pain()) {
            switchChestPain.setChecked(true);
        }
        if (healthCondition.getHeart_attack()) {
            switchHeartAttack.setChecked(true);
        }
        if (healthCondition.getAsthma()) {
            switchBreathingProblem.setChecked(true);
        }
        if (healthCondition.getFainting_spells()) {
            switchFainting.setChecked(true);
        }
        if (healthCondition.getBack_pain()) {
            switchBackPain.setChecked(true);
        }

        if (healthCondition.getMedication()) {
            switchMedication.setChecked(true);
        }

        if (healthCondition.getOther_illness()) {
            switchIllness.setChecked(true);
        }

        if (healthCondition.getSwollen()) {
            switchPainfulJoints.setChecked(true);
        }

        if (healthCondition.getArthritis()) {
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

        Long memberId;

        if (navigationType != null) {
            if (navigationType.equals("edit")) {
                memberId = new Long(member.getMember_id());

                if (Utils.checkMemberValidStatus(MemberBioActivity.this, String.valueOf(memberId))) {
                    member.setMember_valid_status(true);
                } else {
                    member.setMember_valid_status(false);
                }

                String dateString=Utils.dateTimeToString(DateTime.now());
                member.setModified_at(dateString);
                databaseHandler.updateMember(member);

                member = databaseHandler.getMemberById(String.valueOf(member.getMember_id()));
                updateHealthConditionToLocalStorage(databaseHandler,dateString);
                updateAddressToLocalStorage(databaseHandler,dateString);

                if (Utils.isDeviceOnline(MemberBioActivity.this)) {
                    new UpdateMemberAsync(MemberBioActivity.this, member, new AsyncListner() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d(TAG, response.toString());

                            goBackToPreviousActivity();

                        }

                        @Override
                        public void onError(String error) {
                            Log.d(TAG, error);
                            goBackToPreviousActivity();
                        }
                    }).execute();
                } else {
                    goBackToPreviousActivity();
                }

            } else {
                if (member.getLast_payment_date() != null) {

                    String dateString = Utils.dateTimeToString(DateTime.now());

                    member.setHealth_condition_id(saveHealthConditionToLocalStorage(databaseHandler, dateString));

                    member.setAddress_id(saveAddressToLocalStorage(databaseHandler, dateString));

                    Long id = saveMemberLocalStorage(databaseHandler, dateString);

                    member = databaseHandler.getMemberById(String.valueOf(id));

                    savePaymentToLocalStorage(id, databaseHandler, dateString);


                    if (Utils.isDeviceOnline(MemberBioActivity.this)) {
                        if (id != 0) {
                            new SaveMemberAsync(MemberBioActivity.this, member, new AsyncListner() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    goToMainActivity();
                                }

                                @Override
                                public void onError(String error) {
                                    Log.d(TAG, error);
                                    goToMainActivity();
                                }
                            }).execute();
                        }
                    } else {
                        goToMainActivity();
                    }


                } else {
                    Utils.showWarningMessage(MemberBioActivity.this, getString(R.string.payment_date_not_select_warning));
                }
            }
        } else {
            Utils.showWarningMessage(MemberBioActivity.this, getString(R.string.navigation_error_warning));
        }
    }

    private void goBackToPreviousActivity() {
        counterInteger = atomicInteger.incrementAndGet();

        if (counterInteger == 3) {
            Intent intent = new Intent();
            setResult(200, intent);
            finish();//finishing activity
        }


    }

    private void updateAddressToLocalStorage(DatabaseHandler databaseHandler, String dateString) {
        address.setModified_at(dateString);
        databaseHandler.updateAddress(address);

        if (Utils.isDeviceOnline(MemberBioActivity.this)) {
            new UpdateAddressAsync(MemberBioActivity.this, address, new AsyncListner() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());

                    goBackToPreviousActivity();

                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                    goBackToPreviousActivity();
                }
            }).execute();
        } else {
            goBackToPreviousActivity();
        }

    }

    private Long saveAddressToLocalStorage(DatabaseHandler databaseHandler, String dateString) {

        address.setCreated_at(dateString);
        address.setModified_at(dateString);
        final Long addressId = databaseHandler.addAddress(address);

        address.setAddress_id(addressId);

        if (Utils.isDeviceOnline(MemberBioActivity.this)) {
            if (addressId != 0) {
                new SaveAddressAsync(MemberBioActivity.this, address, new AsyncListner() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                        goToMainActivity();

                    }

                    @Override
                    public void onError(String error) {
                        Log.d(TAG, error);
                        goToMainActivity();
                    }
                }).execute();
            }
        } else {
            goToMainActivity();
        }

        return addressId;
    }

    private void updateHealthConditionToLocalStorage(DatabaseHandler databaseHandler, String dateString) {

        healthCondition.setModified_at(dateString);

        databaseHandler.updateHealthCondition(healthCondition);

        if (Utils.isDeviceOnline(MemberBioActivity.this)) {
            new UpdateHealthConditionAsync(MemberBioActivity.this, healthCondition, new AsyncListner() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());

                    goBackToPreviousActivity();

                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                    goBackToPreviousActivity();
                }
            }).execute();
        } else {
            goBackToPreviousActivity();
        }
    }

    private void goToMainActivity() {

        counterInteger = atomicInteger.incrementAndGet();

        if (counterInteger == 4) {
            Intent intent = new Intent(MemberBioActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }

    private Long saveHealthConditionToLocalStorage(DatabaseHandler databaseHandler, String dateString) {
        Long healthConditionId = databaseHandler.addHealthCondition(healthCondition);
        healthCondition.setHealth_condition_id(healthConditionId);
        healthCondition.setCreated_at(dateString);
        healthCondition.setModified_at(dateString);


        if (Utils.isDeviceOnline(MemberBioActivity.this)) {
            new SaveHealthConditionAsync(MemberBioActivity.this, healthCondition, new AsyncListner() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    goToMainActivity();
                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                    goToMainActivity();
                }
            }).execute();
        } else {
            goToMainActivity();
        }
        return healthConditionId;
    }

    private void savePaymentToLocalStorage(Long memberId, DatabaseHandler databaseHandler, String dateString) {
        if (switchPayment.isChecked()) {

            Payment payment = new Payment();

            payment.setMember_id(memberId);
            payment.setPayment_amount(Float.valueOf(etAmount.getText().toString()));
            payment.setMembership_type(member.getMembership_type());
            payment.setLast_payment_date(member.getLast_payment_date());
            payment.setMembership_expiry_date(Utils.getMembershipExpiryDate(member));
            payment.setCreated_at(dateString);
            payment.setModified_at(dateString);

            Long id = databaseHandler.addPayment(payment);

            payment.setPayment_id(id);

            if (Utils.isDeviceOnline(MemberBioActivity.this)) {
                new SavePaymentAsync(MemberBioActivity.this, payment, new AsyncListner() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        goToMainActivity();
                    }

                    @Override
                    public void onError(String error) {
                        Log.d(TAG, error);
                        goToMainActivity();
                    }
                }).execute();
            } else {
                goToMainActivity();
            }

        }
    }

    private Long saveMemberLocalStorage(DatabaseHandler databaseHandler, String dateString) {
        member.setMember_valid_status(true);
        member.setMember_active_status(true);
        member.setCreated_at(dateString);
        member.setModified_at(dateString);
        return databaseHandler.addMember(member);
    }


}
