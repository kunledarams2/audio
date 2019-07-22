package com.e.tremendoc_audio_sdk

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.e.audio_sdk.Audiosdk_Setup
//import kotlinx.android.synthetic.main.activity_authn.*


class MainActivity : AppCompatActivity() {

    lateinit var userEmail:EditText
    private var audiosdk_setup: Audiosdk_Setup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        audiosdk_setup = Audiosdk_Setup(this)

        userEmail=findViewById(R.id.userEmail)
    }


//    fun talktoDoctor(view: View) {
//        var userEmail = userEmail.text.toString().trim()
//
//        if(!TextUtils.isEmpty(userEmail) && Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
//            audiosdk_setup!!.sdk_Setup(userEmail)
//        }
//        else Toast.makeText(this, "Please Enter Valided Email Address",Toast.LENGTH_LONG).show()
//
//
//    }
}
