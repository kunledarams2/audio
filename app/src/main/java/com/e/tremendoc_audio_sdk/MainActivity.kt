package com.e.tremendoc_audio_sdk

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.EditText

import com.e.audio_sdk.AudioSdk_Setup
//import kotlinx.android.synthetic.main.activity_authn.*


class MainActivity : AppCompatActivity() {

    lateinit var userEmail:EditText
    private var audiosdk_setup: AudioSdk_Setup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        audiosdk_setup = AudioSdk_Setup(this, "1")

        userEmail=findViewById(R.id.userEmail)
    }

}
