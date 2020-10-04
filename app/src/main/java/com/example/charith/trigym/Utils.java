package com.example.charith.trigym;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charith.trigym.Activities.Member.MemberBioActivity;
import com.example.charith.trigym.Adapters.InactiveUserAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.CheckImageClickListner;
import com.example.charith.trigym.Interfaces.DialogListner;
import com.example.charith.trigym.Interfaces.ProfileImageListner;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.SuccessErrorListener;
import com.example.charith.trigym.Interfaces.SuccessListner;
import com.example.charith.trigym.Interfaces.WarningMessageListner;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.charith.trigym.Constants.FAIL;
import static com.example.charith.trigym.Constants.SUCCESS;

public class Utils {

    static List<Member> allMembers = null;
    static InactiveUserAdapter messageParticipantsClientAdapter;

    public static DialogPlus showConfirmMesssageWithTitle(Context context) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setPadding(100, 100, 100, 50)
                .setContentBackgroundResource(R.drawable.confirm_message_bg)
                .setContentHolder(new ViewHolder(R.layout.alert_dialog_with_title_layout))
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.CENTER)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        return dialog;

    }

    public static void showWarningMessage(Context context, String string) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentBackgroundResource(R.drawable.warning_message_bg)
                .setContentHolder(new ViewHolder(R.layout.error_dialog_bg))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.TOP)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();

        View view = dialog.getHolderView();

        TextView message = view.findViewById(R.id.message);
        message.setText(string);
    }

    public static void showSuccessMessage(Context context, String string) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentBackgroundResource(R.drawable.success_message_bg)
                .setContentHolder(new ViewHolder(R.layout.error_dialog_bg))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.TOP)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();

        View view = dialog.getHolderView();

        TextView message = view.findViewById(R.id.message);
        message.setText(string);
    }

    public static void showWarningMessageInMainActivity(Context context, String string) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setMargin(0, 100, 0, 0)
                .setContentBackgroundResource(R.drawable.warning_message_bg)
                .setContentHolder(new ViewHolder(R.layout.error_dialog_bg))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.TOP)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();

        View view = dialog.getHolderView();

        TextView message = view.findViewById(R.id.message);
        message.setText(string);
    }

    public static void showSuccessMessageInMainActivity(Context context, String string) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setMargin(0, 100, 0, 0)
                .setContentBackgroundResource(R.drawable.success_message_bg)
                .setContentHolder(new ViewHolder(R.layout.error_dialog_bg))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.TOP)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();

        View view = dialog.getHolderView();

        TextView message = view.findViewById(R.id.message);
        message.setText(string);
    }

    public static void showWarningMessageWithCancelListner(Context context, String string, final WarningMessageListner listner) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setMargin(0, 100, 0, 0)
                .setContentBackgroundResource(R.drawable.warning_message_bg)
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {
                        listner.onCancel(dialog);
                    }
                })
                .setContentHolder(new ViewHolder(R.layout.error_dialog_bg))
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.TOP)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();

        View view = dialog.getHolderView();

        TextView message = view.findViewById(R.id.message);
        message.setText(string);
    }


    public static String removeSpacesAndToLowerCase(String str) {
        return str.trim().toLowerCase();
    }

    public static DateTime stringToDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        return formatter.parseDateTime(dateString);
    }

    public static String dateTimeStringToDateString(String datetimeString) {
        return dateToString(stringToDateTime(datetimeString));
    }


    public static String dateToString(DateTime dateTime) {
        return dateTime.toString("YYYY/MM/dd");
    }

    public static String dateTimeToString(DateTime dateTime) {
        return dateTime.toString("yyyy-MM-dd HH:mm");
    }


    public static String getMembershipExpiryDate(Member member) {

        String expiryDateString = null;
        switch (member.getMembership_type()) {
            case "Daily":
                expiryDateString = Utils.dateTimeToString(Utils.stringToDateTime(member.getLast_payment_date()).plusDays(1));
                break;

            case "1 Week":
                expiryDateString = Utils.dateTimeToString(Utils.stringToDateTime(member.getLast_payment_date()).plusDays(7));
                break;

            case "1 Month":
                expiryDateString = Utils.dateTimeToString(Utils.stringToDateTime(member.getLast_payment_date()).plusMonths(1));
                break;

            case "3 Month":
                expiryDateString = Utils.dateTimeToString(Utils.stringToDateTime(member.getLast_payment_date()).plusMonths(3));
                break;

            case "6 Month":
                expiryDateString = Utils.dateTimeToString(Utils.stringToDateTime(member.getLast_payment_date()).plusMonths(6));
                break;

            case "1 Year":
                expiryDateString = Utils.dateTimeToString(Utils.stringToDateTime(member.getLast_payment_date()).plusYears(1));
                break;
        }

        return expiryDateString;
    }

    public static Boolean checkMemberValidStatus(Context context, String memberId) {

        DateTime today = new DateTime();

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<Payment> paymentList = databaseHandler.getPaymentsByMemberId(memberId);

        for (Payment payment : paymentList) {
            if (Utils.stringToDateTime(payment.getMembership_expiry_date()).isAfter(today)) {
                return true;
            }
        }

        return false;
    }

    //Get payment expired members
    public static List<Member> getInavtiveUsers(Context context, List<Member> members) {

        List<Member> inActiveMembers = new ArrayList<>();

        for (Member member : members) {

            if (!checkMemberValidStatus(context, String.valueOf(member.getMember_id()))) {
                inActiveMembers.add(member);
            }
        }
        return inActiveMembers;
    }

    //Get members list by filtering the active members by gym
    public static List<Member> getActiveMembers(Context context, List<Member> allMembers) {
        List<Member> activeMembers = new ArrayList<>();

        for (Member member : allMembers) {

            if (member.getMember_active_status() && checkMemberValidStatus(context, String.valueOf(member.getMember_id()))) {
                activeMembers.add(member);
            }

        }

        return activeMembers;
    }

    //Get members list by filtering the ignored members by gym
    public static List<Member> getDeactiveMembers(List<Member> allMembers) {
        List<Member> deactiveMembers = new ArrayList<>();

        for (Member member : allMembers) {

            if (!member.getMember_active_status()) {
                deactiveMembers.add(member);
            }

        }

        return deactiveMembers;
    }


    public static void displayInactiveUserList(final Context context, final List<Member> members, final DialogListner listner) {

        allMembers = members;
        final boolean[] selectAllStatus = {false};
        final Dialog dialogMessage = new Dialog(context, android.R.style.Theme_Dialog);
        dialogMessage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMessage.setContentView(R.layout.message_contact_dialog);

        Button btnDialogCancel = dialogMessage.findViewById(R.id.btnDialogCancel);
        Button btnDialogNext = dialogMessage.findViewById(R.id.btnDialogNext);
        TextView title = dialogMessage.findViewById(R.id.messageViewTitle);
        final ListView memberList = dialogMessage.findViewById(R.id.clientList);
        final ImageView selectAll = dialogMessage.findViewById(R.id.selectAllCheckbox);


        title.setText("Inactive User list");
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectAllStatus[0]) {
                    selectAllStatus[0] = false;
                    allMembers = deSelectAllMembers(members);
                    messageParticipantsClientAdapter.notifyDataSetChanged();
                    selectAll.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck_bg));
                } else {
                    selectAllStatus[0] = true;
                    selectAll.setImageDrawable(context.getResources().getDrawable(R.drawable.check_with_white_correct));
                    allMembers = selectAllMembers(members);
                    messageParticipantsClientAdapter.notifyDataSetChanged();
                }

            }
        });


        messageParticipantsClientAdapter = new InactiveUserAdapter(context, members, new CheckImageClickListner() {
            @Override
            public void onItemClick(int position, Member member) {
                if (member.isSelected()) {

                    member.setSelected(false);
                    allMembers.set(position, member);
                    if (checkSelectAllStatus(allMembers)) {
                        selectAllStatus[0] = true;
                        selectAll.setImageDrawable(context.getResources().getDrawable(R.drawable.check_with_white_correct));

                    } else {
                        selectAllStatus[0] = false;
                        selectAll.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck_bg));
                    }
                    messageParticipantsClientAdapter.notifyDataSetChanged();

                } else {
                    member.setSelected(true);
                    allMembers.set(position, member);
                    if (checkSelectAllStatus(allMembers)) {
                        selectAllStatus[0] = true;
                        selectAll.setImageDrawable(context.getResources().getDrawable(R.drawable.check_with_white_correct));

                    } else {
                        selectAllStatus[0] = false;
                        selectAll.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck_bg));

                    }
                    messageParticipantsClientAdapter.notifyDataSetChanged();

                }
            }
        });

        memberList.setAdapter(messageParticipantsClientAdapter);

        dialogMessage.setCancelable(false);

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onCancel(dialogMessage);
            }
        });

        btnDialogNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onSucces(dialogMessage, getMobileNumbersList(getSelectedMembers(allMembers)));
            }
        });

        dialogMessage.show();
    }

    private static List<Member> getSelectedMembers(List<Member> allMembers) {
        List<Member> selectedList = new ArrayList<>();

        for (Member member : allMembers) {
            if (member.isSelected()) {
                selectedList.add(member);
            }
        }

        return selectedList;
    }

    private static List<Member> deSelectAllMembers(List<Member> memberList) {
        List<Member> selectedList = new ArrayList<>();

        for (Member member : memberList) {
            member.setSelected(false);
            selectedList.add(member);
        }

        return selectedList;
    }

    private static List<Member> selectAllMembers(List<Member> memberList) {

        List<Member> selectedList = new ArrayList<>();

        for (Member member : memberList) {
            member.setSelected(true);
            selectedList.add(member);
        }

        return selectedList;
    }

    private static boolean checkSelectAllStatus(List<Member> memberList) {


        for (Member member : memberList) {
            if (!member.isSelected()) {
                return false;
            }
        }

        return true;
    }

    public static String getMobileNumbersList(List<Member> members) {
        StringBuilder numbers = new StringBuilder();
        if (members != null) {
            for (Member member : members) {
                if (member.getMember_mobile_1() != null) {
                    numbers.append(member.getMember_mobile_1() + ";");
                }
            }
        }
        return numbers.toString();
    }

    public static boolean checkCallSMSPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, SEND_SMS);
        int result1 = ContextCompat.checkSelfPermission(context, CALL_PHONE);
        int result2 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCallSMSPermission(Activity activity, int requestCode) {

        ActivityCompat.requestPermissions(activity, new String[]{SEND_SMS, CALL_PHONE, WRITE_EXTERNAL_STORAGE}, requestCode);

    }

    public static void displayPasswordDialog(Context context, String firstName, String NIC, final ProfileImageListner listener) {
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setPadding(50, 50, 50, 50)
                .setMargin(50, 50, 50, 50)
                .setContentBackgroundResource(R.drawable.confirm_message_bg)
                .setContentHolder(new ViewHolder(R.layout.profile_name_dialog_layout))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setCancelable(false)
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.CENTER)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        View viewDialog = dialog.getHolderView();

        final EditText etFirstName = viewDialog.findViewById(R.id.etFirstName);
        final EditText etNIC = viewDialog.findViewById(R.id.etNIC);
        if (firstName != null) {
            etFirstName.setText(firstName);
        }

        if (NIC != null) {
            etNIC.setText(NIC);
        }

        Button dialogButtonOk = viewDialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonCancel = viewDialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etFirstName.getText().toString())) {
                    listener.onSuccess(dialog, etFirstName.getText().toString(), etNIC.getText().toString());
                }
            }
        });


        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancel(dialog);
            }
        });

        dialog.show();
    }

    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void saveMultipleObjectsInSharedPref(Context context, HashMap<String, String> map, SuccessListner listner) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String jsonString = entry.getValue();
            editor.putString(key, jsonString);
        }

        editor.commit();

        listner.onFinished();
    }

    public static void saveObjectInSharedPref(Context context, String key, String
            jsonString, SuccessListner listner) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, jsonString);
        editor.commit();

        listner.onFinished();
    }

    public static void clearAllPref(Context context, SuccessListner listner) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();

        listner.onFinished();
    }

    public static String getObjectFromSharedPref(Context context, String key) {


        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String jsonString = sharedPref.getString(key, null);

        return jsonString;
    }


    /**
     * Display alert dialog without title
     *
     * @param context  Context of the activity
     * @param message  String of the messsage body
     * @param listener OK button click event listner
     */
    public static void showAlertWithoutTitleDialog(Context context, String message, String status,
                                                   final DialogInterface.OnClickListener listener) {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_layout);

        final ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        final TextView btnOK = dialog.findViewById(R.id.btnOK);
        final TextView tvMessage = dialog.findViewById(R.id.tvMessage);

        tvMessage.setText(Html.fromHtml(message), TextView.BufferType.SPANNABLE);

        if (status.equals(Constants.SUCCESS)) {
            dialogIcon.setImageResource(R.mipmap.tick);
        } else if (status.equals(Constants.FAIL)) {
            dialogIcon.setImageResource(R.mipmap.cross_staus);
        } else {
            dialogIcon.setImageResource(R.mipmap.warn);
        }


        btnOK.setOnClickListener(view -> {
            listener.onClick(dialog, 0);
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void readMembersJSONFile(Context context, File file, ResponseListener listener) {
        // Define the File Path and its Name
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onError(context, e.toString());
        }
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = null;
        if (fileReader != null) {
            bufferedReader = new BufferedReader(fileReader);
            try {


                stringBuilder = new StringBuilder();
                String line = null;
                line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                listener.onError(context, e.toString());
            }

// This responce will have Json Format String
            listener.onSuccess(context, stringBuilder.toString());
        } else
            listener.onSuccess(context, "");

    }

    public static void readJSONFile(Context context, File file, ResponseListener listener) {
        // Define the File Path and its Name
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onError(context, e.toString());
        }
        if (fileReader != null) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            try {
                line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                listener.onError(context, e.toString());
            }

// This responce will have Json Format String
            listener.onSuccess(context, stringBuilder.toString());
        } else {
            listener.onSuccess(context, "");

        }

    }

    /**
     * Check permissions
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void saveMemberJSONFile(String jsonString, File file, SuccessErrorListener listener) {
        // Define the File Path and its Name

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonString);
            bufferedWriter.close();
            listener.onSuccess();
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.toString());
        }

    }



    public static void saveJSONFile(String jsonString, File file, SuccessErrorListener listener) {
        // Define the File Path and its Name

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonString);
            bufferedWriter.close();
            listener.onSuccess();
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.toString());
        }

    }

    public static File createMembersDirectory(Context context, String fileName) {
        File mediaFile = null;

        File mediaStorageDir = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Constants.ENTITY_DIRECTORY);
        }
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                Log.d(Constants.TAG, " ");

                Toast.makeText(context, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                fileName + ".json");

        Log.d(Constants.TAG, mediaFile.getName());
        return mediaFile;
    }

    public static File createEntityDirectory(Context context, String fileName) {
        File mediaFile = null;

        File mediaStorageDir = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Constants.ENTITY_DIRECTORY);
        }
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                Log.d(Constants.TAG, " ");

                Toast.makeText(context, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                fileName + ".json");

        Log.d(Constants.TAG, mediaFile.getName());
        return mediaFile;
    }
}
