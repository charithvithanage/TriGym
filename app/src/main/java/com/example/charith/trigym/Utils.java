package com.example.charith.trigym;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class Utils {
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

    public static String removeSpacesAndToLowerCase(String str) {
        return str.trim().toLowerCase();
    }

    public static DateTime stringToDateTime(String dateString){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY/MM/dd");
        return formatter.parseDateTime(dateString);
    }

    public static String dateToString(DateTime dateTime){
        return dateTime.toString("YYYY/MM/dd");
    }

    public static Boolean checkMemberValidStatus(Context context,String memberId) {

        DateTime today = new DateTime();

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<Payment> paymentList = databaseHandler.getPaymentsByMemberId(memberId);

        for (Payment payment : paymentList) {
            if (Utils.stringToDateTime(payment.getPaymentExpiryDate()).isAfter(today)) {
                return true;
            }
        }

        return false;
    }

    public static String getMembershipExpiryDate(Member member) {

        String expiryDateString = null;
        switch (member.getMembershipType()) {
            case "Daily":
                expiryDateString = Utils.dateToString(Utils.stringToDateTime(member.getLastPaymentDate()).plusDays(1));
                break;

            case "1 Week":
                expiryDateString = Utils.dateToString(Utils.stringToDateTime(member.getLastPaymentDate()).plusDays(7));
                break;

            case "1 Month":
                expiryDateString = Utils.dateToString(Utils.stringToDateTime(member.getLastPaymentDate()).plusMonths(1));
                break;

            case "3 Month":
                expiryDateString = Utils.dateToString(Utils.stringToDateTime(member.getLastPaymentDate()).plusMonths(3));
                break;

            case "6 Month":
                expiryDateString = Utils.dateToString(Utils.stringToDateTime(member.getLastPaymentDate()).plusMonths(6));
                break;

            case "1 Year":
                expiryDateString = Utils.dateToString(Utils.stringToDateTime(member.getLastPaymentDate()).plusYears(1));
                break;
        }

        return expiryDateString;
    }

    public static Payment getValidPayment(Context context,String memberId) {

        DateTime today=new DateTime();

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<Payment> paymentList = databaseHandler.getPaymentsByMemberId(memberId);

        Payment validPayment=new Payment();

        for (Payment payment : paymentList) {
            if (Utils.stringToDateTime(payment.getPaymentExpiryDate()).isAfter(today)) {
                validPayment=payment;
            }
        }

        return validPayment;
    }

}
