package com.example.charith.trigym.Test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.charith.trigym.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etNamel;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Contact> arrayList;
    RecycleViewAdapter adapter;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);
        etNamel = findViewById(R.id.etName);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RecycleViewAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        readFromLocalStorage();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (checkNetworkConnection()) {

                    final DbHelper dbHelper = new DbHelper(context);

                    final SQLiteDatabase database = dbHelper.getWritableDatabase();

                    Cursor cursor = dbHelper.readFromLocalDatabase(database);

                    while (cursor.moveToNext()) {
                        int sync_status = cursor.getInt(cursor.getColumnIndex(DbContact.SYNC_STATUS));

                        if (sync_status == DbContact.SYNC_STATUS_FAILED) {
                            final String name = cursor.getString(cursor.getColumnIndex(DbContact.NAME));

                            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, DbContact.SERVER_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        String responseString = jsonObject.getString("response");

                                        if (responseString.equals("OK")) {
                                            dbHelper.updateLocalDatabase(name, DbContact.SYNC_STATUS_OK, database);

                                            

                                            readFromLocalStorage();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("name", name);
                                    return params;
                                }
                            };


                            MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                        }

                    }

                    database.close();

                }
            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter(DbContact.UI_UPDATE_BROADCAST));


    }

    public void submit(View view) {

        String name = etNamel.getText().toString();
        saveToAppServer(name);
        etNamel.setText("");
    }

    private void saveToAppServer(final String name) {


        if (checkNetworkConnection()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContact.SERVER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseString = jsonObject.getString("response");

                        if (responseString.equals("OK")) {
                            saveToLocalStorage(name, DbContact.SYNC_STATUS_OK);
                        } else {
                            saveToLocalStorage(name, DbContact.SYNC_STATUS_FAILED);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    saveToLocalStorage(name, DbContact.SYNC_STATUS_FAILED);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);

                    return params;
                }
            };

            MySingleton.getInstance(TestActivity.this).addToRequestQueue(stringRequest);

        } else {
            saveToLocalStorage(name, DbContact.SYNC_STATUS_FAILED);
        }


    }

    private void readFromLocalStorage() {

        arrayList.clear();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DbContact.NAME));
            int sync_status = cursor.getInt(cursor.getColumnIndex(DbContact.SYNC_STATUS));

            arrayList.add(new Contact(name, sync_status));
        }

        adapter.notifyDataSetChanged();
        cursor.close();
//        dbHelper.close();
    }

    public boolean checkNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void saveToLocalStorage(String name, int sync_status) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        dbHelper.saveToLocalDatabase(name, sync_status, database);
        readFromLocalStorage();
//        dbHelper.close();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
