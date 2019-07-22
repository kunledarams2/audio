package com.e.audio_sdk.View.UI.UUitil;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.e.audio_sdk.R;

import java.io.FileInputStream;
import java.io.IOException;


public class AudioPlayer {

    private final String LOG_TAG = AudioPlayer.class.getSimpleName();
    Context mContext;
    private MediaPlayer mPlayer;
    private AudioTrack  mProgressTone;
    private AudioManager audioManager;

    private final static int SAMPLE_RATE= 16000;


    public AudioPlayer(Context mContext) {
        this.mContext = mContext;
    }


    public void playRinging(){

         audioManager= (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);


        switch (audioManager.getRingerMode()){

            case AudioManager.RINGER_MODE_NORMAL:
                mPlayer= new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_RING);

                try{
                    Uri defaultRingingtone= RingtoneManager.getActualDefaultRingtoneUri(mContext,RingtoneManager.TYPE_RINGTONE);
                    mPlayer.setDataSource(mContext, Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.phone_loud1));
//                    mPlayer.setDataSource(mContext, defaultRingingtone);
                    mPlayer.prepare();
                }catch (Exception e){
                    Log.e(LOG_TAG,"could not setup media player for  ringtone");
                    Log.e(LOG_TAG,e.getMessage());
                    mPlayer=null;
                    return;
                }

                mPlayer.setLooping(true);
                mPlayer.start();
                break;

        }
    }

    public void stopRinging(){

        if(mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer=null;
        }
    }

    public void playProgressTone(){

        stopProgressTone();
        try {
            mProgressTone= createProgressTone(mContext);
            mProgressTone.play();

        }catch (Exception e){
            Log.e(LOG_TAG, "Could not play progress media" + e.getMessage());
        }
    }

    public void stopProgressTone(){
        if (mProgressTone != null) {
            mProgressTone.stop();
            mProgressTone.release();
            mProgressTone = null;
        }

    }


    private static AudioTrack createProgressTone(Context context) throws IOException {
        AssetFileDescriptor fd = context.getResources().openRawResourceFd(R.raw.progress_tone);
        int length = (int) fd.getLength();

        AudioTrack track = new AudioTrack(AudioManager.STREAM_VOICE_CALL, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, length,
                AudioTrack.MODE_STATIC);

        byte[] data = new byte[length];
        readFileToBytes(fd, data);

        track.write(data, 0, data.length);
        track.setLoopPoints(0, data.length / 2, 30);
        return track;
    }

    private static void readFileToBytes(AssetFileDescriptor fd, byte[] data) throws IOException{
        FileInputStream stream = fd.createInputStream();

        int bytes = 0;
        while (bytes < data.length) {
            int res = stream.read(data, bytes, (data.length - bytes));
            if (res == -1) {
                break;
            }
            bytes += res;
        }

    }

    public boolean isMute() {
        return audioManager.isMicrophoneMute();
    }

    public boolean isOnSpeaker(){
        return audioManager.isSpeakerphoneOn();
    }


}
