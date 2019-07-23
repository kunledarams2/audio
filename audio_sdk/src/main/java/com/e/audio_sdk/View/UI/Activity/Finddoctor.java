package com.e.audio_sdk.View.UI.Activity;

//import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.e.audio_sdk.R;
import com.e.audio_sdk.View.Callback.FragmentChanger;
import com.e.audio_sdk.View.UI.Fragment.Finddoctor.FindADoctor;
import com.e.audio_sdk.View.UI.Fragment.FragmentTitled;


public class Finddoctor extends BaseActivity implements FragmentChanger {

    public static  final String CHAT_WITH_DOCTOR="chat_with_doctor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findadoctor);

    }


    @Override
    public void ChangeFragment(FragmentTitled fragment) {
        changeView(fragment);
    }

    private void changeView(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();

    }
    private void changeView(String fragmentTitle){
        Fragment fragment;
        setTitle(fragmentTitle);
        switch (fragmentTitle){
            case CHAT_WITH_DOCTOR: fragment= FindADoctor.newInstance(1);
            break;
            default:fragment= FindADoctor.newInstance(1);

        }
        changeView(fragment);

    }

    @Override
    protected void onServiceConnected(IBinder iBinder) {
        super.onServiceConnected(iBinder);
        changeView(CHAT_WITH_DOCTOR);
    }
}
