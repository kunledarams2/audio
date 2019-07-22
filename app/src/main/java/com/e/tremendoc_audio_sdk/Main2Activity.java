package com.e.tremendoc_audio_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.e.audio_sdk.Audiosdk_Setup;
import com.e.audio_sdk.View.UI.Activity.BaseActivity;

public class Main2Activity extends AppCompatActivity {

   EditText userEmail;
   private Audiosdk_Setup audiosdk_setup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        userEmail=findViewById(R.id.userEmail);
        audiosdk_setup= new Audiosdk_Setup(this );
    }

    public void talktoDoctor(View view) {
        String getuserEmail= userEmail.getText().toString();
        if(!TextUtils.isEmpty(getuserEmail) && Patterns.EMAIL_ADDRESS.matcher(getuserEmail).matches()){
//            audiosdk_setup.sdk_Setup(getuserEmail);
            audiosdk_setup.sdk_Setup(getuserEmail);

        } else
            Toast.makeText(this,"Wrong Email Format",Toast.LENGTH_LONG).show();


    }
}
