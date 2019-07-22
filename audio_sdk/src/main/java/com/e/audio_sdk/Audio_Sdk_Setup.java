package com.e.audio_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Audio_Sdk_Setup extends AppCompatActivity {

    private String getuserEmail;
    TextView showuserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio__sdk__setup);

        getuserEmail=getIntent().getStringExtra("userEmail");
        showuserEmail = findViewById(R.id.showuserEmail);
        showuserEmail.setText(getuserEmail);
    }
}
