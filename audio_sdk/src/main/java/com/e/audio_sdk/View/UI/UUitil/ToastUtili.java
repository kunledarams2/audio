package com.e.audio_sdk.View.UI.UUitil;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class ToastUtili {

    public static void showModal(Context ctx, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg);
        builder.setNegativeButton("CLOSE", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }
}
