package com.example.charith.trigym;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.charith.trigym.Activities.MainActivity;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.DialogListner;

import java.util.List;

public class Splash extends AppCompatActivity {

    private static final int SMS_SEND_REQUEST_CODE =200 ;
    List<Member> allMembers=null;
    List<Member> inActiveMembers=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        allMembers = databaseHandler.getAllMembers();

        if(allMembers!=null||allMembers.size()!=0){
            inActiveMembers= Utils.getInavtiveUsers(getApplicationContext(),allMembers);
        }

        Utils.displayInactiveUserList(Splash.this, inActiveMembers, new DialogListner() {
            @Override
            public void onSucces(Dialog dialog,String numberList) {
                dialog.dismiss();

                Intent intentMessage = new Intent(android.content.Intent.ACTION_VIEW);
                intentMessage.putExtra("address", numberList.toString());
                intentMessage.putExtra("sms_body", getString(R.string.membership_expire_alert));
                intentMessage.setType("vnd.android-dir/mms-sms");
                startActivityForResult(intentMessage,SMS_SEND_REQUEST_CODE);

            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });



        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SMS_SEND_REQUEST_CODE){
            Intent i = new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
