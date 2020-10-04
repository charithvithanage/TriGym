package com.example.charith.trigym.Activities.Member;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.charith.trigym.Activities.MainActivity;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.DatePickerFragment;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.charith.trigym.Constants.REQUEST_WRITE_STORAGE;
import static com.example.charith.trigym.Constants.SUCCESS;
import static com.example.charith.trigym.Utils.checkMemberValidStatus;
import static com.example.charith.trigym.Utils.showAlertWithoutTitleDialog;

public class MemberBioActivity extends AppCompatActivity {

    Switch switchPayment;
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

    DatabaseHandler databaseHandler;

    AtomicInteger atomicInteger;

    Integer counterInteger = 0;

    ProgressDialog progressDialog;

    boolean permissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_bio);
        memberString = getIntent().getStringExtra("memberString");
        navigationType = getIntent().getStringExtra("navigationType");

        databaseHandler = new DatabaseHandler(MemberBioActivity.this);

        init();


    }

    private void requestPermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            permissionGranted = true;
        }
    }

    private void init() {

        progressDialog = new ProgressDialog(MemberBioActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait.....");

        atomicInteger = new AtomicInteger(0);

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
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


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermission();

                if (permissionGranted)
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

//        if (member.getValidStatus()) {
//            switchPayment.setChecked(true);
//        } else {
//            switchPayment.setChecked(false);
//        }

//        etAmount.setText(getValidPaymentAmount());
//        tvPaymentDate.setText(member.getLast_payment_date());
        etSpecialNotes.setText(member.getMember_health_condition());


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

                if (checkMemberValidStatus(MemberBioActivity.this, String.valueOf(memberId))) {
                    member.setMember_valid_status(true);
                } else {
                    member.setMember_valid_status(false);
                }

                String dateString = Utils.dateTimeToString(DateTime.now());
                member.setModified_at(dateString);
                databaseHandler.updateMember(member);
                member = databaseHandler.getMemberById(String.valueOf(member.getMember_id()));
                savePaymentToLocalStorage(member.getMember_id(), databaseHandler, dateString);

                goBackToPreviousActivity();


            } else {
                if (member.getLast_payment_date() != null) {

                    String dateString = Utils.dateTimeToString(DateTime.now());

                    Long id = saveMemberLocalStorage(databaseHandler, dateString);

                    member = databaseHandler.getMemberById(String.valueOf(id));
                    savePaymentToLocalStorage(id, databaseHandler, dateString);

                    goToMainActivity();





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

        if (counterInteger == 2) {
            showAlertWithoutTitleDialog(MemberBioActivity.this, getString(R.string.membersavedsuccessfully), SUCCESS, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    setResult(200, intent);
                    finish();//finishing activity
                }
            });

        }


    }

    private void goToMainActivity() {

        counterInteger = atomicInteger.incrementAndGet();

        if (counterInteger == 2) {

            progressDialog.dismiss();

            Intent intent = new Intent(MemberBioActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }


    private void savePaymentToLocalStorage(Long memberId, DatabaseHandler
            databaseHandler, String dateString) {
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

        }

        if (navigationType.equals("edit")) {
            goBackToPreviousActivity();
        } else {
            goToMainActivity();
        }
    }

    private Long saveMemberLocalStorage(DatabaseHandler databaseHandler, String dateString) {
        member.setMember_valid_status(true);
        member.setMember_active_status(true);
        member.setCreated_at(dateString);
        member.setModified_at(dateString);
        return databaseHandler.addMember(member);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show();
                    permissionGranted = true;
                    // Reload the activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    permissionGranted = false;

                }
            }
        }


    }
}