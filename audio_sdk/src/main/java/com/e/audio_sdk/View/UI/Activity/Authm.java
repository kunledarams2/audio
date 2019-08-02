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

import com.e.audio_sdk.View.UI.UUitil.DeviceName;
import com.e.audio_sdk.View.UI.UUitil.IO;
import com.e.audio_sdk.View.UI.UUitil.ToastUtili;
import com.sinch.android.rtc.SinchError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_CUSTOMER_EMAIL;
import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_PROVIDERID;
import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_TYPE;

//import android.support.v7.app.AppCompatActivity;

public class Authm extends BaseActivity implements SinchService.StartFailedListener {

    private EditText email;
    private StringCall call;
    private String getHostuseremail;
    private Bundle bundle;
    private String useremail;
    private String providerid;
    private String sdk_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authm);

        email = findViewById(R.id.email);
        bundle = getIntent().getExtras();

        useremail = bundle.getString(SDK_CUSTOMER_EMAIL);
        providerid = bundle.getString(SDK_PROVIDERID);
        sdk_type=bundle.getString(SDK_TYPE);


    }

    /**
     * Host Apps to provide their user email for query
     * to check the existence of the user on the SDK
     */
    public void getEmail() {

        email.setText(useremail);
        call = new StringCall(this);

        Map<String, String> logParams = new HashMap<>();
        logParams.put("email", useremail);
        logParams.put("brand", DeviceName.getDeviceName());
        logParams.put("operatingSystem", "ANDROID");
        logParams.put("uuid", IO.getData(this, API.MY_UUID));

        logParams.put("sdkType", sdk_type);
        logParams.put("provider", IO.getData(this,API.PROVIDER_ID));


        call.post(URLS.SDK_AUTHENICATION, logParams, response -> {

            Log.d("Sign_In", "__________------___________-----" + response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.has("code") && obj.getInt("code") == 0) {
                    API.setCredentials(this, response);
                    API.setUserData(this, obj);
                    onConnectSinch();
                    Intent intent = new Intent(this, Finddoctor.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(this, IO.getData(this, API.PROVIDER_ID),Toast.LENGTH_LONG).show();

                } else if (obj.has("description")) {

                    API.clearCredentials(this);
                    Intent intent = new Intent(this, Signup.class);
                    intent.putExtras(bundle);
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
        Log.d("Sinch fail AuthActivity", error.getMessage());
    }

    @Override
    protected void onServiceConnected(IBinder iBinder) {
        super.onServiceConnected(iBinder);
        getEmail();
        getSinchServiceInterface().startListener(this);
    }

    private void onConnectSinch() {
        String userName = IO.getData(this, API.MY_UUID);
        getSinchServiceInterface().startClient(userName);

    }

}
