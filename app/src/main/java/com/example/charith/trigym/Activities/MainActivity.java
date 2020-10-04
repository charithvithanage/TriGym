package com.example.charith.trigym.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charith.trigym.Activities.Member.DeactivateMemberListActivity;
import com.example.charith.trigym.Activities.Member.NewMemberActivity;
import com.example.charith.trigym.Adapters.MemberAdapter;
import com.example.charith.trigym.AsyncTasks.Member.DeleteMemberAsync;
import com.example.charith.trigym.AsyncTasks.SyncAsync;
import com.example.charith.trigym.Constants;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.DialogListner;
import com.example.charith.trigym.Interfaces.MemberSelectListner;
import com.example.charith.trigym.Interfaces.SuccessListner;
import com.example.charith.trigym.Interfaces.VolleyCallback;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Services.MemberService;
import com.example.charith.trigym.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.charith.trigym.Constants.REQUEST_WRITE_STORAGE;
import static com.example.charith.trigym.Utils.displayInactiveUserList;
import static com.example.charith.trigym.Utils.getDeactiveMembers;
import static com.example.charith.trigym.Utils.getInavtiveUsers;
import static com.example.charith.trigym.Utils.showAlertWithoutTitleDialog;
import static com.example.charith.trigym.Utils.showWarningMessageInMainActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PERMISSION_REQUEST_CODE = 200;
    private RecyclerView activeMemberListRecycleView;
    private RecyclerView inActiveMemberListRecycleView;
    List<Member> memberList;
    List<Member> inActiveMembers = null;

    Gson gson;
    MemberAdapter memberAdapter;
    MemberAdapter inActiveMemberAdapter;
    SearchView searchView;

    private static final int SMS_SEND_REQUEST_CODE = 200;
    NavigationView navigationView;
    List<String> memberTypeList;

    Button selectActiveBtn, selectInactiveBtn;
    int whiteColorId, blueColorId;

    boolean isActiveListShow;

    private String TAG = "TriGym";

    boolean permissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectMemberType();

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Utils.checkCallSMSPermission(MainActivity.this)) {
                Utils.requestCallSMSPermission(MainActivity.this, PERMISSION_REQUEST_CODE);
            }
        }


        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(false);
        activeMemberListRecycleView = findViewById(R.id.memberList);
        inActiveMemberListRecycleView = findViewById(R.id.inActiveMemberList);
        activeMemberListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        inActiveMemberListRecycleView.setLayoutManager(new LinearLayoutManager(this));

        memberList = new ArrayList<>();
        inActiveMembers = new ArrayList<>();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        gson = builder.create();

//        new GetAllMembersAsync().execute();

        loadMemberList();

        loadInActiveMemberList();


        selectActiveBtn = findViewById(R.id.selectActiveBtn);
        selectInactiveBtn = findViewById(R.id.selectInactiveBtn);
        whiteColorId = ContextCompat.getColor(getApplicationContext(), R.color.primaryWhite);
        blueColorId = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);

        selectActiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActiveList();
            }
        });


        selectInactiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInActiveList();
            }
        });

        showActiveList();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:

                if (grantResults.length > 0) {
                    boolean send_sms_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean call_accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_storage_accepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                    if (send_sms_accepted && call_accepted && write_storage_accepted) {
                        Utils.showSuccessMessageInMainActivity(MainActivity.this, getString(R.string.permission_granted));

                    } else {
                        showWarningMessageInMainActivity(MainActivity.this, getString(R.string.no_permission_granted));

                    }
                }

                break;

            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show();
                    permissionGranted = true;
                    new SyncAsync(MainActivity.this, () -> {
                        new Handler(Looper.getMainLooper()).post(() -> showAlertWithoutTitleDialog(MainActivity.this, getString(R.string.syncsuccessfully), Constants.SUCCESS, (dialog, which) -> {
                            startActivity(getIntent());
                            finish();
                        }));
                    }).execute();
                    // Reload the activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    permissionGranted = false;

                }

                break;

            }
        }
    }

    private void showActiveList() {

        isActiveListShow = true;

        selectActiveBtn.setTextColor(getResources().getColor(R.color.charcoal));
        selectInactiveBtn.setTextColor(getResources().getColor(R.color.platinum));
        selectActiveBtn.setBackground(getResources().getDrawable(R.drawable.select_btn_bg));
        selectInactiveBtn.setBackground(getResources().getDrawable(R.drawable.deselect_btn_bg));
        inActiveMemberListRecycleView.setVisibility(View.GONE);
        activeMemberListRecycleView.setVisibility(View.VISIBLE);
        searchView.setQuery("", false);
    }

    private void showInActiveList() {

        isActiveListShow = false;

        selectInactiveBtn.setTextColor(getResources().getColor(R.color.charcoal));
        selectActiveBtn.setTextColor(getResources().getColor(R.color.platinum));
        selectInactiveBtn.setBackground(getResources().getDrawable(R.drawable.select_btn_bg));
        selectActiveBtn.setBackground(getResources().getDrawable(R.drawable.deselect_btn_bg));
        inActiveMemberListRecycleView.setVisibility(View.VISIBLE);
        activeMemberListRecycleView.setVisibility(View.GONE);
        searchView.setQuery("", false);
    }

    private void selectMemberType() {

        memberTypeList = new ArrayList<>();

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.member_type_dialog);
        dialog.setCancelable(true);
        final LinearLayout btnAdult = dialog.findViewById(R.id.adultLayout);
        final LinearLayout btnStudent = dialog.findViewById(R.id.studentLayout);
        final LinearLayout btnArobicks = dialog.findViewById(R.id.arobicksLayout);
        final LinearLayout btnYoga = dialog.findViewById(R.id.yogaLayout);
        final LinearLayout btnZumba = dialog.findViewById(R.id.zumbaLayout);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        final ImageView adultCheckBox = dialog.findViewById(R.id.selectAdultCheckbox);
        final ImageView studentCheckBox = dialog.findViewById(R.id.selectStudentCheckbox);
        final ImageView arobicksCheckBox = dialog.findViewById(R.id.selectArobicksCheckbox);
        final ImageView yogaCheckBox = dialog.findViewById(R.id.selectYogaCheckbox);
        final ImageView zumbaCheckBox = dialog.findViewById(R.id.selectZumbaCheckbox);

        btnAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnAdult.isSelected()) {
                    btnAdult.setSelected(false);
                    memberTypeList.remove("Adult");
                    adultCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));

                } else {
                    btnAdult.setSelected(true);
                    btnStudent.setSelected(false);

                    memberTypeList.add("Adult");
                    memberTypeList.remove("Student");
                    studentCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));
                    adultCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.check_with_white_correct));

                }

            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStudent.isSelected()) {
                    btnStudent.setSelected(false);
                    memberTypeList.remove("Student");
                    studentCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));

                } else {
                    btnStudent.setSelected(true);
                    btnAdult.setSelected(false);
                    memberTypeList.add("Student");
                    memberTypeList.remove("Adult");
                    studentCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.check_with_white_correct));
                    adultCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));

                }
            }
        });

        btnArobicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnArobicks.isSelected()) {
                    btnArobicks.setSelected(false);
                    memberTypeList.remove("Arobicks");
                    arobicksCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));

                } else {
                    btnArobicks.setSelected(true);
                    memberTypeList.add("Arobicks");
                    arobicksCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.check_with_white_correct));

                }
            }
        });

        btnYoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (btnYoga.isSelected()) {
                    btnYoga.setSelected(false);
                    memberTypeList.remove("Yoga");
                    yogaCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));

                } else {
                    btnYoga.setSelected(true);
                    memberTypeList.add("Yoga");
                    yogaCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.check_with_white_correct));

                }

            }
        });

        btnZumba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnZumba.isSelected()) {
                    btnZumba.setSelected(false);
                    memberTypeList.remove("Zumba");
                    zumbaCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_bg));

                } else {
                    btnZumba.setSelected(true);
                    memberTypeList.add("Zumba");
                    zumbaCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.check_with_white_correct));

                }

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                selectMemberCategory();
            }
        });

        //close the dialog when pressed the back key
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        dialog.show();

    }

    private void selectMemberCategory() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.member_category_dialog);
        dialog.setCancelable(true);
        final LinearLayout btnSingle = dialog.findViewById(R.id.singleLayout);
        final LinearLayout btnCouple = dialog.findViewById(R.id.coupleLayout);
        final LinearLayout btnFamily = dialog.findViewById(R.id.familyLayout);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();


                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberCategory", "Individual");
                intent.putExtra("memberTypeListString", gson.toJson(memberTypeList));
                startActivity(intent);

            }
        });

        btnCouple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberCategory", "Couple");
                intent.putExtra("memberTypeListString", gson.toJson(memberTypeList));
                startActivity(intent);
            }
        });

        btnFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberCategory", "Family");
                intent.putExtra("memberTypeListString", gson.toJson(memberTypeList));
                startActivity(intent);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        //close the dialog when pressed the back key
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        dialog.show();

    }

    private void loadInActiveMemberList() {
        final DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        inActiveMembers = getInavtiveUsers(MainActivity.this, databaseHandler.getAllMembers());
        inActiveMemberAdapter = new MemberAdapter(MainActivity.this, inActiveMembers, new MemberSelectListner() {
            @Override
            public void selectMember(Member member, String changeStatus) {
                if (changeStatus.equals("deactivate")) {

                    member.setMember_active_status(false);
                    member.setModified_at(Utils.dateTimeToString(DateTime.now()));
                    databaseHandler.updateMember(member);
                } else {
                    databaseHandler.deleteMember(String.valueOf(member.getMember_id()));
                    new DeleteMemberAsync(MainActivity.this, member, () -> {

                    }).execute();

                }
                inActiveMembers = getInavtiveUsers(MainActivity.this, databaseHandler.getAllMembers());
                inActiveMemberAdapter.updateList(inActiveMembers);
            }
        });

        inActiveMemberListRecycleView.setAdapter(inActiveMemberAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            if (inActiveMembers.size() == 0) {

                showWarningMessageInMainActivity(MainActivity.this, getString(R.string.no_inactive_users_alert));

            } else {
                displayInactiveUserList(MainActivity.this, inActiveMembers, new DialogListner() {
                    @Override
                    public void onSucces(Dialog dialog, String numberList) {
                        dialog.dismiss();

                        navigationView.getMenu().findItem(R.id.nav_camera).setCheckable(false);


                        Intent intentMessage = new Intent(android.content.Intent.ACTION_VIEW);
                        intentMessage.putExtra("address", numberList.toString());
                        intentMessage.putExtra("sms_body", getString(R.string.membership_expire_alert));
                        intentMessage.setType("vnd.android-dir/mms-sms");
                        startActivityForResult(intentMessage, SMS_SEND_REQUEST_CODE);

                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                });

            }


        } else if (id == R.id.deactivated_members) {
            final DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);

            List<Member> deActiveMembers = getDeactiveMembers(databaseHandler.getAllMembers());

            if (deActiveMembers.size() == 0) {
                showWarningMessageInMainActivity(MainActivity.this, getString(R.string.no_deactivated_users));

            } else {
                Intent intent = new Intent(MainActivity.this, DeactivateMemberListActivity.class);
                startActivity(intent);
            }


        } else if (id == R.id.sync_members) {
            requestPermission();

            if (permissionGranted)
                new SyncAsync(MainActivity.this, () -> {
                    new Handler(Looper.getMainLooper()).post(() -> showAlertWithoutTitleDialog(MainActivity.this, getString(R.string.syncsuccessfully), Constants.SUCCESS, (dialog, which) -> {
                        startActivity(getIntent());
                        finish();
                    }));
                }).execute();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SMS_SEND_REQUEST_CODE) {

            navigationView.getMenu().findItem(R.id.nav_camera).setCheckable(false);

        }
    }

    private class GetAllMembersAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            MemberService.getInstance().getAllMembers(MainActivity.this, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {

                }

                @Override
                public void onSuccess(JSONArray response) {
                    try {

                        for (int i = 0; i < response.length(); i++) {

                            Member member = gson.fromJson(response.getString(i), Member.class);
                            DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
                            Member excistingMember = databaseHandler.getMemberById(String.valueOf(member.getMember_id()));
                            if (excistingMember.getMember_id() != null) {
                                databaseHandler.updateMember(member);
                            } else {
                                Long id = databaseHandler.addMember(member);

                                savePaymentToLocalStorage(id, databaseHandler);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    loadMemberList();
                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, error);
                }
            });
            return null;
        }
    }

    private void savePaymentToLocalStorage(Long id, DatabaseHandler databaseHandler) {
//        Payment payment = new Payment();
//
//        payment.setMember_id((int) (long) memberId);
//        payment.setAmount(Float.valueOf(etAmount.getText().toString()));
//        payment.setType(member.getMembership_type());
//        payment.setLast_payment_date(member.getLast_payment_date());
//        payment.setPaymentExpiryDate(Utils.getMembershipExpiryDate(member));
//
//        databaseHandler.addPayment(payment);
    }

    private void loadMemberList() {
        final DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);

        memberList = Utils.getActiveMembers(MainActivity.this, databaseHandler.getAllMembers());
        memberAdapter = new MemberAdapter(MainActivity.this, memberList, new MemberSelectListner() {
            @Override
            public void selectMember(Member member, String changeStatus) {
                if (changeStatus.equals("deactivate")) {

                    member.setMember_active_status(false);
                    member.setModified_at(Utils.dateTimeToString(DateTime.now()));
                    databaseHandler.updateMember(member);
                } else {

                    databaseHandler.deleteMember(String.valueOf(member.getMember_id()));

                    new DeleteMemberAsync(MainActivity.this, member, new SuccessListner() {
                        @Override
                        public void onFinished() {

                        }
                    }).execute();

                }
                memberList = Utils.getActiveMembers(MainActivity.this, databaseHandler.getAllMembers());
                memberAdapter.updateList(memberList);
            }
        });

        activeMemberListRecycleView.setAdapter(memberAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (isActiveListShow) {
                    memberAdapter.filter(newText);
                } else {
                    inActiveMemberAdapter.filter(newText);

                }

                return false;
            }
        });
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

}
