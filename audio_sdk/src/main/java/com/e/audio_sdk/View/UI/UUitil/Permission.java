package com.e.audio_sdk.View.UI.UUitil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by tremendoc on 7/17/2019.
 */

public class Permission {
    public static final int PERMISSION_REQUEST = 100;
    private static Class callingClass;

    public static final String[] VOICE_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};
    public static final String[] VIDEO_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CAMERA
    };

    public static boolean permissionIsGranted(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean permissionsAreGranted(Context context, String[] permissions) {
        for (String permission: permissions) {
            if (!permissionIsGranted(context, permission)){
                return false;
            }
        }

        return true;
    }

    public static void setCallingClass(Class callingClass) {
        Permission.callingClass = callingClass;
    }

    public static Class getCallingClass() {
        return callingClass;
    }

    public static void  requestPermissions(Activity activity, String[] perms) {
        ActivityCompat.requestPermissions(activity, perms,  101);
    }

    public static void showModal(Context context, String msg, DialogInterface.OnClickListener posClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes, Continue", posClick);
        builder.setNegativeButton("Deny", (dialog, i) -> dialog.cancel());
        builder.create().show();
    }

}
