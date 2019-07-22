package com.e.audio_sdk.View.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.e.audio_sdk.Api.API;
import com.e.audio_sdk.Api.StringCall;
import com.e.audio_sdk.Api.URLS;
import com.e.audio_sdk.R;
import com.e.audio_sdk.Service.SinchService;
import com.e.audio_sdk.Service.SinchService2;
import com.e.audio_sdk.View.UI.UUitil.DeviceName;
import com.e.audio_sdk.View.UI.UUitil.IO;
import com.e.audio_sdk.View.UI.UUitil.ToastUtili;
import com.sinch.android.rtc.SinchError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import android.support.v7.app.AppCompatActivity;

public class Authm extends BaseActivity implements SinchService.StartFailedListener {

    private EditText email;
    private StringCall call;
    private String getHostuseremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authm);

        email = findViewById(R.id.email);
//        call= new StringCall(this);
        getHostuseremail = getIntent().getStringExtra("userEmail");
//        getEmail();
    }

    /**
     * Host Apps to provide their user email for query
     * to check the existence of the user on the SDK
     */
    public void getEmail() {

        email.setText(getHostuseremail);
        call = new StringCall(this);

        Map<String, String> logParams = new HashMap<>();
        logParams.put("email", getHostuseremail);
        logParams.put("brand", DeviceName.getDeviceName());
        logParams.put("operatingSystem", "ANDROID");
        logParams.put("uuid", IO.getData(this, API.MY_UUID));

        logParams.put("sdkType", "chat");
        logParams.put("provider", "1");


        call.post(URLS.SDK_AUTHENICATION, logParams, response -> {

            try {
                JSONObject obj = new JSONObject(response);

                if (obj.has("code") && obj.getInt("code") == 0) {

                    API.setCredentials(this, response);
                    API.setUserData(this, obj);

                    onConnectSinch();
                    Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, Finddoctor.class);
                    startActivity(intent);
                    finish();

                } else if (obj.has("description")) {

                    Intent intent = new Intent(this, Signup.class);
                    startActivity(intent);
                    finish();

                }

            } catch (JSONException e) {
                ToastUtili.showModal(this, e.getMessage());
                Log.e("Authentication Error", e.getMessage());
            }

        }, error -> {
            if (error.networkResponse == null) {
                Toast.makeText(this, "Please check your internet ", Toast.LENGTH_LONG).show();
            }

            ToastUtili.showModal(this, "Sorry an error occurred. Please try again");

        });


    }


    @Override
    public void onStarted() {

        Log.d("AuthActivity", "Sinch Start");
    }

    @Override
    public void onStartFailed(SinchError error) {

//        Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
        Log.d("Sinch fail AuthActivity", error.getMessage());
    }


    @Override
    protected void onServiceConnected(IBinder iBinder) {
        super.onServiceConnected(iBinder);
        getEmail();
        Toast.makeText(this, "onServiceConnected", Toast.LENGTH_LONG).show();
        getSinchServiceInterface().startListener(this);
    }

    private void onConnectSinch() {
        String userId = IO.getData(this, API.MY_UUID);
        Toast.makeText(this, userId, Toast.LENGTH_LONG).show();
        if(getSinchServiceInterface() !=null || !getSinchServiceInterface().isStarted()){
            getSinchServiceInterface().startClient(userId);
        }


//
//        if (getSinchServiceInterface() != null) {
//        }
//        try {
//
//        }catch (Exception e){
//            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
//            Log.d("getServiceInterface",e.getMessage());
//        }


    }

}
