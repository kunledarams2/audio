package com.e.audio_sdk.View.UI.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.e.audio_sdk.Service.SinchService;
import com.e.audio_sdk.Service.SinchService2;

public class BaseActivity extends AppCompatActivity implements ServiceConnection {


   public  SinchService.SinchServiceInterface mSinchServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate()");
        getApplicationContext().bindService(new Intent(this, SinchService.class), this,
                BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        log(" onServiceConnected() ");
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            log(" CONNECTING CALL SERVICE ");
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected(iBinder);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

        log("onServiceConnected()");

        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

//    protected void onServiceConnected() {
//        // for subclasses
//    }

    protected void onServiceConnected(IBinder iBinder) {
        mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {

        return mSinchServiceInterface;
    }

    private void log(String string) {
        Log.d("BaseActivity", "--_--_------------___-__--__--_--__-_ " + string);
    }
}
