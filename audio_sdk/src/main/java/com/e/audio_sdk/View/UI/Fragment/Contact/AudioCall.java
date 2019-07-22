package com.e.audio_sdk.View.UI.Fragment.Contact;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.audio_sdk.R;
import com.e.audio_sdk.View.UI.Activity.ContactActivity;
import com.e.audio_sdk.View.UI.Fragment.FragmentTitled;


/**

 */
public class AudioCall extends FragmentTitled {


    public AudioCall() {
        // Required empty public constructor
    }

    public static AudioCall newInstance() {
        AudioCall fragment = new AudioCall();
        fragment.setTitle(ContactActivity.VOICE_CALL);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_audio_call, container, false);
    }


    public void onButtonPressed(Uri uri) {

    }


}
