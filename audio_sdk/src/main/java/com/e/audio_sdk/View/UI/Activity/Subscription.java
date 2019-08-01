package com.e.audio_sdk.View.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.e.audio_sdk.R;

public class Subscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);


    }

    public void mSubscription(View view) {
        Toast.makeText(this,"Not Yet Available",Toast.LENGTH_LONG).show();
    }

    public void mSkip(View view) {

        Intent intent = new Intent(this,Finddoctor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
