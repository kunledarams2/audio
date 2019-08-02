package com.e.audio_sdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.e.audio_sdk.Api.API;
import com.e.audio_sdk.Service.SinchService;
//import com.e.audio_sdk.Service.SinchService2;
import com.e.audio_sdk.View.UI.Activity.Authm;
import com.e.audio_sdk.View.UI.Activity.BaseActivity;
import com.e.audio_sdk.View.UI.UUitil.Checker;
import com.e.audio_sdk.View.UI.UUitil.IO;
import com.e.audio_sdk.View.UI.UUitil.Permission;
import com.sinch.android.rtc.SinchError;

import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_CUSTOMER_EMAIL;
import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_PROVIDERID;
import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_TYPE;

public class AudioSdk_Setup extends BaseActivity {

    Context context;
    private String sdk_type;
    private String providerId;
    private boolean askedBefore = false;
    private static final int INTERNET_PERMISSION = 100;
    private static final int UUID_PERMISSION = 120;
//    private Bundle bundle;

    public AudioSdk_Setup(Context context, String providerId,String sdk_type ) {
        this.context = context;
        this.providerId=providerId;
        this.sdk_type= sdk_type;
    }


    public void sdk_Setup(String userEmail) {
        setUUID();

        Bundle bundle= new Bundle();
        bundle.putString(SDK_PROVIDERID,providerId);
        bundle.putString(SDK_CUSTOMER_EMAIL,userEmail);
        bundle.putString(SDK_TYPE,sdk_type);
        // check for internet access
        Checker checker = new Checker(context);
        if (checker.isOnline()) {
            Intent intent = new Intent(context, Authm.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            IO.setData(context,API.PROVIDER_ID,providerId);

        } else {
            Toast.makeText(context, "No Internet Access", Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UUID_PERMISSION) {
            setUUID();
        }
    }

    private void setUUID() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String uuid;
            uuid = (Build.VERSION.SDK_INT >= 26) ? manager.getImei() : manager.getDeviceId();
            IO.setData(context, API.MY_UUID, uuid);


        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    Manifest.permission.READ_PHONE_STATE}, UUID_PERMISSION);
        }
    }


}
