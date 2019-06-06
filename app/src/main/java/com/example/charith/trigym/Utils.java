package com.example.charith.trigym;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charith.trigym.Adapters.InactiveUserAdapter;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Interfaces.CheckImageClickListner;
import com.example.charith.trigym.Interfaces.DialogListner;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

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

    public static String removeSpacesAndToLowerCase(String str) {
        return str.trim().toLowerCase();
    }

    public static DateTime stringToDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY/MM/dd");
        return formatter.parseDateTime(dateString);
    }

    public static String dateToString(DateTime dateTime) {
        return dateTime.toString("YYYY/MM/dd");
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

    public static Boolean checkMemberValidStatus(Context context, String memberId) {

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

    public static List<Member> getInavtiveUsers(Context context, List<Member> members) {

        List<Member> inActiveMembers = new ArrayList<>();

        for (Member member : members) {

            if (!checkMemberValidStatus(context, String.valueOf(member.getId()))) {
                inActiveMembers.add(member);
            }

        }

        return inActiveMembers;
    }

    public static void displayInactiveUserList(final Context context, final List<Member> members, final DialogListner listner) {

        allMembers = members;
        final boolean[] selectAllStatus = {false};
        final Dialog dialogMessage = new Dialog(context);
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
                listner.onSucces(dialogMessage,getMobileNumbersList(getSelectedMembers(allMembers)));
            }
        });

        dialogMessage.show();
    }

    private static List<Member> getSelectedMembers(List<Member> allMembers) {
        List<Member> selectedList = new ArrayList<>();

        for (Member member : allMembers) {
            if(member.isSelected()){
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
                if (member.getMobile1() != null) {
                    numbers.append(member.getMobile1() + ";");
                }
            }
        }
        return numbers.toString();
    }


}
