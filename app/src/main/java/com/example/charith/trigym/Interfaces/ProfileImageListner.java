package com.example.charith.trigym.Interfaces;

import com.orhanobut.dialogplus.DialogPlus;

public interface ProfileImageListner {
    void onSuccess(DialogPlus dialog, String fistName, String nic);

    void onCancel(DialogPlus dialogPlus);
}
