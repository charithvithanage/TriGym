package com.example.charith.trigym;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

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

}
