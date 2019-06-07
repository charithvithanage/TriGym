package com.example.charith.trigym.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.charith.trigym.Convertors.DateTimeSerializer;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.DialogListner;
import com.example.charith.trigym.Adapters.MemberAdapter;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView memberListRecycleView;
    List<Member> memberList;
    Gson gson;
    Member member;
    MemberAdapter memberAdapter;
    SearchView searchView;

    private static final int SMS_SEND_REQUEST_CODE = 200;
    List<Member> allMembers = null;
    List<Member> inActiveMembers = null;
    NavigationView navigationView;

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
        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(false);
        memberListRecycleView = findViewById(R.id.memberList);
        memberListRecycleView.setLayoutManager(new LinearLayoutManager(this));

        memberList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
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

    }

    private void selectMemberType() {


        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.member_type_dialog);
        dialog.setCancelable(true);
        Button btnAdult = dialog.findViewById(R.id.btnAdult);
        Button btnStudent = dialog.findViewById(R.id.btnStudent);
        Button btnArobicks = dialog.findViewById(R.id.btnArobicks);
        Button btnYoga = dialog.findViewById(R.id.btnYoga);
        Button btnZumba = dialog.findViewById(R.id.btnZumba);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberType", "Adult");
                startActivity(intent);


            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberType", "Student");
                startActivity(intent);
            }
        });

        btnArobicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();


                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberType", "Arobicks");
                startActivity(intent);
            }
        });

        btnYoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberType", "Yoga");
                startActivity(intent);

            }
        });

        btnZumba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();


                Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
                intent.putExtra("memberType", "Zumba");
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


    private void loadMemberList() {

        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        memberList = databaseHandler.getAllMembers();
        memberAdapter = new MemberAdapter(MainActivity.this, memberList);
        memberListRecycleView.setAdapter(memberAdapter);


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

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            allMembers = databaseHandler.getAllMembers();

            if (allMembers != null || allMembers.size() != 0) {
                inActiveMembers = Utils.getInavtiveUsers(getApplicationContext(), allMembers);
            }

            Utils.displayInactiveUserList(MainActivity.this, inActiveMembers, new DialogListner() {
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

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
}