package com.example.charith.trigym.Test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context,"internet change",Toast.LENGTH_SHORT).show();


//        if(checkNetworkConnection(context)){
//
//            final DbHelper dbHelper=new DbHelper(context);
//
//            final SQLiteDatabase database=dbHelper.getWritableDatabase();
//
//            Cursor cursor=dbHelper.readFromLocalDatabase(database);
//
//            while (cursor.moveToNext()){
//                int sync_status=cursor.getInt(cursor.getColumnIndex(DbContact.SYNC_STATUS));
//
//                if(sync_status==DbContact.SYNC_STATUS_FAILED){
//                    final String name=cursor.getString(cursor.getColumnIndex(DbContact.NAME));
//
//                    StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, DbContact.SERVER_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            try {
//                                JSONObject jsonObject=new JSONObject(response);
//
//                                String responseString=jsonObject.getString("response");
//
//                                if(responseString.equals("OK")){
//                                    dbHelper.updateLocalDatabase(name,DbContact.SYNC_STATUS_OK,database);
//                                    context.sendBroadcast(new Intent(DbContact.UI_UPDATE_BROADCAST));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }){
//
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String,String> params=new HashMap<>();
//                            params.put("name",name);
//                            return params;
//                        }
//                    };
//
//
//                    MySingleton.getInstance(context).addToRequestQueue(stringRequest);
//                }
//
//            }
//
//            database.close();
//
//        }

    }


    public boolean checkNetworkConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
