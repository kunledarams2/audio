package com.e.audio_sdk.View.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.e.audio_sdk.Api.API;
import com.e.audio_sdk.Api.StringCall;
import com.e.audio_sdk.Api.URLS;
import com.e.audio_sdk.R;
import com.e.audio_sdk.View.UI.UUitil.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.e.audio_sdk.View.UI.UUitil.AudioParamsSetup.SDK_PROVIDERID;

public class Subscription extends AppCompatActivity {

    private static  String TAG = Subscription.class.getSimpleName();
    private Bundle bundle;
    private String providerId;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

//        bundle=getIntent().getExtras();
//        providerId= bundle.getString(SDK_PROVIDERID);

        dialog= new ProgressDialog(this);

    }

    public void mSubscription(View view) {
//        dialog=ProgressDialog.show(this,"Subscribe","Please Wait....",false, false);
        StringCall call = new StringCall(this);
        Map<String, String> params = new HashMap<>();

        params.put("providerId", IO.getData(this,API.PROVIDER_ID));

        call.post(URLS.SUBSCRIPTION_SDK_CREATE,params,response -> {

            log(response);
            try {
//                dialog.dismiss();
                JSONObject obj = new JSONObject(response);

                if(obj.getInt("code")==0 && !obj.isNull("code")){
                    Intent intent = new Intent(Subscription.this, Finddoctor.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this,obj.getString("description"),Toast.LENGTH_LONG).show();

                }
                else if(obj.getInt("code")==10){
                    Toast.makeText(this,obj.getString("description"),Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){

            }
        },error -> {
//            dialog.dismiss();
            log(error.getMessage());
        });
    }

    public void mSkip(View view) {

        Intent intent = new Intent(this,Finddoctor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private  void log(String msg){
        Log.d(TAG, "______--------_____------______" + msg);
    }
}
